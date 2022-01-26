package com.pakkhaphon.cucumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        val btsignup = findViewById<Button>(R.id.buttonsingup)
        btsignup.setOnClickListener {
            register()
        }
    }

    private fun register() {

        mAuth = FirebaseAuth.getInstance()

        val inputUsername = findViewById<EditText>(R.id.editTextUsername)
        val inputEmail = findViewById<EditText>(R.id.editTextEmail)
        val inputPassword = findViewById<EditText>(R.id.editTextPassword)
        val inputConfirm = findViewById<EditText>(R.id.editTextConfirm)
        val inputMale = findViewById<RadioButton>(R.id.radioButtonMale)
        val inputFemale = findViewById<RadioButton>(R.id.radioButtonFemale)
        val inputSender = findViewById<RadioButton>(R.id.radioButtonSender)
        val inputReceiver = findViewById<RadioButton>(R.id.radioButtonReceiver)
        val inputGender = findViewById<RadioGroup>(R.id.GenderGroup)
        val inputAttention = findViewById<RadioGroup>(R.id.AttentionGroup)

        val username = inputUsername.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val password = inputPassword.text.toString().trim()
        val confirm = inputConfirm.text.toString().trim()
        var gender = ""
        var attention = ""

        if(inputMale.isChecked) {
            gender = inputMale.text.toString()
        }
        else if(inputFemale.isChecked) {
            gender = inputFemale.text.toString()
        }

        if(inputSender.isChecked) {
            attention = inputSender.text.toString()
        }
        else if(inputReceiver.isChecked) {
            attention = inputReceiver.text.toString()
        }

        // check null
        if(inputUsername.text.toString().isEmpty()) {
            inputUsername.setError("Please Enter Username")
        }

        if(inputEmail.text.toString().isEmpty()) {
            inputEmail.setError("Please Enter E-mail")
        }

        if(inputPassword.text.toString().isEmpty()) {
            inputPassword.setError("Please Enter Password")
        }

        if(inputConfirm.text.toString().isEmpty()) {
            inputConfirm.setError("Please Confirm Password")
        } else if(inputPassword.text.toString() != inputConfirm.text.toString()) {
            inputConfirm.setError("Password not match")
        }

        if(inputGender.getCheckedRadioButtonId() == -1) {
            inputFemale.setError("Please Select Gender")
        }

        if(inputAttention.getCheckedRadioButtonId() == -1) {
            inputReceiver.setError("Please Select Attention")
        }


        if((inputUsername.text.toString().isNotEmpty()) && (inputEmail.text.toString().isNotEmpty()) && (inputPassword.text.toString().isNotEmpty())) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task -> if(task.isSuccessful) {
                        databaseref = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth.currentUser!!.uid)
                        val data = HashMap<String,Any>()
                        data["uid"] = mAuth.currentUser!!.uid
                        data["image"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fperson-icon.png?alt=media&token=70867895-3dce-42c1-ad78-2778d3a08dd1"
                        data["username"] = username
                        data["password"] = password
                        data["email"] = email
                        data["gender"] = gender
                        data["attention"] = attention
                        databaseref.updateChildren(data).addOnCompleteListener {
                                task -> if(task.isSuccessful) {
                                    startActivity(Intent(this@register_choice,MainActivity::class.java))
                                    val userdata = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth.currentUser!!.uid).child("Local")
                                    val data = HashMap<String,Any>()
                                    data["address"] = ""
                                    data["area"] = ""
                                    data["info"] = ""
                                    data["home_image"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fhome-icon.jpg?alt=media&token=fa484be3-75e8-4ab5-b8de-dbe2a062068f"
                                    data["home_image1"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fhome-icon.jpg?alt=media&token=fa484be3-75e8-4ab5-b8de-dbe2a062068f"
                                    data["home_image2"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fhome-icon.jpg?alt=media&token=fa484be3-75e8-4ab5-b8de-dbe2a062068f"
                                    data["province"] = ""
                                    data["road"] = ""
                                    userdata.setValue(data)
                                    val petdata = FirebaseDatabase.getInstance().reference.child("Users").child(mAuth.currentUser!!.uid).child("Pet")
                                    val data1 = HashMap<String,Any>()
                                    data1["pet_profile"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fdog-icon.png?alt=media&token=d67c854c-09ae-4274-a893-2784348f821e"
                                    data1["pet_image"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fdog-icon.png?alt=media&token=d67c854c-09ae-4274-a893-2784348f821e"
                                    data1["pet_image1"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fdog-icon.png?alt=media&token=d67c854c-09ae-4274-a893-2784348f821e"
                                    data1["pet_image2"] = "https://firebasestorage.googleapis.com/v0/b/cucumber-db.appspot.com/o/image%2Fdog-icon.png?alt=media&token=d67c854c-09ae-4274-a893-2784348f821e"
                                    data1["info"] = ""
                                    data1["petname"] = ""
                                    data1["sex"] = ""
                                    petdata.setValue(data1)
                                }
                        }
                    }
            }
        }
    }
}