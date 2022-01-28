package com.pakkhaphon.cucumber

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class Setting : Fragment() {

    private lateinit var userDatabase: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val inputMale = view.findViewById<RadioButton>(R.id.radioMale)
        val inputFemale = view.findViewById<RadioButton>(R.id.radioFemale)
        val inputSender = view.findViewById<RadioButton>(R.id.radioSender)
        val inputReceiver = view.findViewById<RadioButton>(R.id.radioReceiver)
        val button_save = view.findViewById<Button>(R.id.button_save_setting)
//        val userdatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        var attention = ""
        var gender = ""

        userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        userDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                attention = snapshot.child("attention").value.toString()
                gender = snapshot.child("gender").value.toString()

                if (attention == "Sender") {
                    inputSender.setChecked(true)
                } else if (attention == "Receiver") {
                    inputReceiver.setChecked(true)
                }
                if (gender == "Male") {
                    inputMale.setChecked(true)
                } else if (gender == "Female") {
                    inputFemale.setChecked(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // onCancel
            }
        })

        button_save.setOnClickListener {
            val data = HashMap<String,Any>()

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

            data["attention"] = attention
            data["gender"] = gender
            userDatabase.updateChildren(data)
        }
        return view
    }
}