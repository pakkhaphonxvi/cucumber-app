package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
            changeFragment(Chat())
        }

    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this@HomeActivity,MainActivity::class.java))
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        when(item.itemId){
            R.id.nav_home -> {
                changeFragment(Home())
            }
            R.id.nav_setting ->{
                changeFragment(Setting())
            }
            R.id.nav_profile -> {
                changeFragment(Profile())
            }
            R.id.nav_signout -> {
                googleSignInClient.signOut()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@HomeActivity,MainActivity::class.java))
                finish()
            }
        }
        return true
    }

    fun changeFragment(frag : Fragment){
        val fragment = supportFragmentManager.beginTransaction()
        fragment.replace(R.id.fragment_space, frag).addToBackStack(null).commit()
    }

}

