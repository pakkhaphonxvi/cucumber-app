package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var Auth: FirebaseAuth
    lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        checkuser()

        val SignUp_button = findViewById<Button>(R.id.button_signup)
        val SignIn_button = findViewById<Button>(R.id.button_signin)

        Auth = FirebaseAuth.getInstance()
        SignUp_button.setOnClickListener{
            val i = Intent(this,register_choice::class.java)
            startActivity(i)
        }

        SignIn_button.setOnClickListener {
            signin()
        }
    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this@MainActivity,HomeActivity::class.java))
            finish()
        }
    }

    private fun signin() {
        val email_text = findViewById<EditText>(R.id.editTextTextPersonName)
        val pass_text = findViewById<EditText>(R.id.editTextTextPassword)
        val email:String = email_text.text.toString()
        val password:String = pass_text.text.toString()

//        if(email.isEmpty())
//        {
//
//        }else

        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                //to do
                startActivity(Intent(this@MainActivity,HomeActivity::class.java))
            }
        }
    }
}

