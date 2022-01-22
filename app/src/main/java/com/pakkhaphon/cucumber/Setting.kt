package com.pakkhaphon.cucumber

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class Setting : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        val attention_target = arrayListOf<String>("Sender","Receiver")
        val spinnerAdapterAttention = context?.let {
            ArrayAdapter(it,R.layout.spinnerdesign,R.id.text_spinner_item,attention_target)
        }

        val spinner_attention = view.findViewById<Spinner>(R.id.spinner_attention)
        spinner_attention.adapter = spinnerAdapterAttention

        val gender_target = arrayListOf<String>("Male","Female")
        val spinnerAdapterGender = context?.let {
            ArrayAdapter(it,android.R.layout.simple_spinner_item,gender_target)
        }

        val spinner_gender = view.findViewById<Spinner>(R.id.spinner_gender)
        spinner_gender.adapter = spinnerAdapterGender

        var attention = ""
        spinner_attention.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, root: View?, p2: Int, p3: Long) {
//                val root =  root?.rootView
//                root?.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK))
//                root?.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_BACK))

                if(attention_target[p2] == "Sender"){
                    attention = "Sender"
                }
                else if(attention_target[p2] == "Receiver"){
                    attention = "Receiver"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }


        var gender = ""
        spinner_gender.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(gender_target[p2] == "Male"){
                    gender = "Male"
                }
                else if(gender_target[p2] == "Female"){
                    gender = "Female"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val button_save = view.findViewById<Button>(R.id.button_save_setting)
        val userdatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        button_save.setOnClickListener {
            val data = HashMap<String,Any>()
            data["attention"] = attention
            data["gender"] = gender
            userdatabase.updateChildren(data)
        }

        val list = resources.getStringArray(R.array.test)
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,list)

        val spinner_test = view.findViewById<AutoCompleteTextView>(R.id.spinner_test)
        spinner_test.setAdapter(adapter)
        spinner_test.setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(resources,R.drawable.sent_round,null))


        return view
    }
}