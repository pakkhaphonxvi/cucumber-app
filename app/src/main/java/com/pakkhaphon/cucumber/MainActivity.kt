package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val SignUp_button = findViewById<Button>(R.id.button_2)
        SignUp_button.setOnClickListener{
            val Intent = Intent(this,register_choice::class.java)
//            val Intent = Intent(this,register_choice::class.java)
            startActivity(Intent)
        }
    }
}