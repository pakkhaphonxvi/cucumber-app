package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ProfileDetail : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_detail, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)


        val uid = arguments?.getString("uid")

        val userdata = FirebaseDatabase.getInstance().reference.child("Users").child(uid.toString())
        val profilimage = view.findViewById<CircleImageView>(R.id.profile_image)
        val txt_name = view.findViewById<TextView>(R.id.txt_username)
        val txt_address = view.findViewById<TextView>(R.id.txt_address)
        val txt_info = view.findViewById<TextView>(R.id.txt_info)
        val image_location = view.findViewById<ImageView>(R.id.image_location)
        val image_location1 = view.findViewById<ImageView>(R.id.image_location1)
        val image_location2 = view.findViewById<ImageView>(R.id.image_location2)

        userdata.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val attention = snapshot.child("attention").value.toString()
                if(attention == "Sender"){
                    val link_pet = snapshot.child("Pet").child("pet_profile").value.toString()
                    Picasso.get().load(link_pet).noFade().into(profilimage)
                    txt_name.text = snapshot.child("Pet").child("pet_name").value.toString()
                    val Address = snapshot.child("Location").child("address").value.toString() +
                            snapshot.child("Location").child("area").value.toString() +
                            snapshot.child("Location").child("road").value.toString() +
                            snapshot.child("Location").child("province").value.toString()
                    txt_address.text = Address
                    txt_info.text = snapshot.child("Pet").child("info").value.toString()
                    val link_pet1 = snapshot.child("Pet").child("pet_image").value.toString()
                    Picasso.get().load(link_pet1).noFade().into(image_location)
                    val link_pet2 = snapshot.child("Pet").child("pet_image1").value.toString()
                    Picasso.get().load(link_pet2).noFade().into(image_location1)
                    val link_pet3 = snapshot.child("Pet").child("pet_image2").value.toString()
                    Picasso.get().load(link_pet3).noFade().into(image_location2)
                }
                else if(attention == "Receiver"){
                    val link_profile = snapshot.child("image").value.toString()
                    Picasso.get().load(link_profile).noFade().into(profilimage)
                    txt_name.text = snapshot.child("username").value.toString()
                    val Address = snapshot.child("Location").child("address").value.toString() +
                            snapshot.child("Location").child("area").value.toString() +
                            snapshot.child("Location").child("road").value.toString() +
                            snapshot.child("Location").child("province").value.toString()

                    txt_address.text = Address
                    txt_info.text = snapshot.child("Location").child("info").value.toString()
                    val link_location = snapshot.child("Location").child("home_image").value.toString()
                    Picasso.get().load(link_location).noFade().into(image_location)
                    val link_location1 = snapshot.child("Location").child("home_image1").value.toString()
                    Picasso.get().load(link_location1).noFade().into(image_location1)
                    val link_location2 = snapshot.child("Location").child("home_image2").value.toString()
                    Picasso.get().load(link_location2).noFade().into(image_location2)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return view
    }


}