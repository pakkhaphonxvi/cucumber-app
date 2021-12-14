package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class register_choice : AppCompatActivity() {

    private lateinit var databaseref: DatabaseReference
    var id:Long = 0
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_choice)

        val btsingup = findViewById<Button>(R.id.buttonsingup)


        btsingup.setOnClickListener {
            register()
        }

    }

    private fun register() {

        mAuth = FirebaseAuth.getInstance()

        val inputusername = findViewById<EditText>(R.id.editTextUsername)
        val inputpassword = findViewById<EditText>(R.id.editTextPassword)
        val inputemail = findViewById<EditText>(R.id.editTextEmail)
        val inputrbmale = findViewById<RadioButton>(R.id.radioButtonMale)
        val inputrbfemale = findViewById<RadioButton>(R.id.radioButtonFemale)
        val inputrbsender = findViewById<RadioButton>(R.id.radioButtonSender)
        val inputrbrevice = findViewById<RadioButton>(R.id.radioButtonReceiver)

        val username = inputusername.text.toString().trim()
        val password = inputpassword.text.toString().trim()
        val email = inputemail.text.toString().trim()
        var gender = ""
        var attention = ""

        if(inputrbmale.isChecked){
            gender = inputrbmale.text.toString()
        }
        else if(inputrbfemale.isChecked){
            gender = inputrbfemale.text.toString()
        }

        if(inputrbrevice.isChecked){
            attention = inputrbrevice.text.toString()
        }
        else if(inputrbsender.isChecked){
            attention = inputrbsender.text.toString()
        }


        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){

                    databaseref = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth.currentUser!!.uid)

                    val data = HashMap<String,Any>()
                    data["uid"] = mAuth.currentUser!!.uid
                    data["image"] = ""
                    data["username"] = username
                    data["password"] = password
                    data["email"] = email
                    data["gender"] = gender
                    data["attention"] = attention

                    databaseref.updateChildren(data).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            startActivity(Intent(this@register_choice,MainActivity::class.java))
                        }
                    }

                }
            }

    }
}