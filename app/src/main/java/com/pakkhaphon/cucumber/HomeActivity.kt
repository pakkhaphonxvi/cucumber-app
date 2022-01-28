package com.pakkhaphon.cucumber

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_space,Home()).commitAllowingStateLoss()
        }

        val drawerlayout = findViewById<DrawerLayout>(R.id.drawerlayout)
        firebaseAuth = FirebaseAuth.getInstance()
        checkuser()

        toggle = ActionBarDrawerToggle(this, drawerlayout,findViewById(R.id.toolbar),R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        val menu_button = findViewById<NavigationView>(R.id.menu_button)
        menu_button.setNavigationItemSelectedListener(this)

        val chat_button = findViewById<Button>(R.id.button_chat)
        chat_button.setOnClickListener {
            changeFragment(FriendsList())
//            showDialog(this)
        }
    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null) {
            startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        when(item.itemId) {
            R.id.nav_home -> {
                changeFragment(Home())
            }
            R.id.nav_setting -> {
                changeFragment(Setting())
            }
            R.id.nav_profile -> {
                changeFragment(Profile())
            }
            R.id.nav_signout -> {
                googleSignInClient.signOut()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                finish()
            }
        }
        return true
    }

    fun changeFragment(frag: Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_space, frag).addToBackStack(null).commit()
    }

    fun showDialog(context: Context, fid:String ) {
        val Userdatabase = FirebaseDatabase.getInstance().reference.child("Users")
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_dialog)
        var x = dialog.findViewById<Button>(R.id.ready_button)
        x.setOnClickListener {
            Userdatabase.child(fid).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val i  = Intent(context, Chat::class.java)
                    i.putExtra("name", snapshot.child("username").value.toString())
                    i.putExtra("uid", snapshot.child("uid").value.toString())
                    i.putExtra("image", snapshot.child("image").value.toString())
                    context!!.startActivity(i)
                }
                override fun onCancelled(error: DatabaseError) {
                    //On Cancelled
                }
            })
            dialog.dismiss()
        }
        dialog.show()
    }
}

