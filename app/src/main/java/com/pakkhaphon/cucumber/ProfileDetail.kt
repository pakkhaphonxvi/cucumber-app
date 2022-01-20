package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileDetail : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)


        val uid = arguments?.getString("uid")

        val userdata = FirebaseDatabase.getInstance().reference.child("Users").child(uid.toString())
        val displaytest = view.findViewById<TextView>(R.id.displaytest)

        userdata.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val attention = snapshot.child("attention").value.toString()
                if(attention == "Sender"){
                    displaytest.text = "Sender"
                }
                else if(attention == "Receiver"){
                    displaytest.text = "Receiver"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return view
    }


}