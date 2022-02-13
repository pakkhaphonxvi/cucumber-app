package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class Intro : AppCompatActivity() {

    private lateinit var firebaseAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        val preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
        val FirstTime: String? = preferences.getString("FirstTimeInstall", "")

        if(FirstTime.equals("Yes")) {
            val intent = Intent(this@Intro, MainActivity::class.java)
            startActivity(intent)
        } else {
            val editor = preferences.edit()
            editor.putString("FirstTimeInstall", "Yes")
            editor.apply()
        }

        val button_welcome = findViewById<Button>(R.id.button_Welcome)
        firebaseAuth = FirebaseAuth.getInstance()
        checkuser()

        button_welcome.setOnClickListener {
            startActivity(Intent(this@Intro, MainActivity::class.java))
        }
    }

    private fun checkuser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this@Intro, HomeActivity::class.java))
            finish()
        }
    }
}
