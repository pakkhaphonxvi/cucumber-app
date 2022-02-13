package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
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
        val txt_name = view.findViewById<TextView>(R.id.txt_name)
        val txt_address = view.findViewById<TextView>(R.id.txt_address)
        val txt_info = view.findViewById<TextView>(R.id.txt_info)
        val txt_sex = view.findViewById<TextView>(R.id.txt_sex)
        val image_0 = view.findViewById<ImageView>(R.id.image_location1)
        val image_1 = view.findViewById<ImageView>(R.id.image_location2)
        val image_2 = view.findViewById<ImageView>(R.id.image_location3)

        userdata.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val attention = snapshot.child("attention").value.toString()
                if(attention == "Sender") {
                    val link_pet = snapshot.child("Pet").child("pet_profile").value.toString()
                    val Address = snapshot.child("Local").child("address").value.toString() + ", " +
                            snapshot.child("Local").child("area").value.toString() + ", " +
                            snapshot.child("Local").child("road").value.toString() + ", " +
                            snapshot.child("Local").child("province").value.toString()
                    val link_pet1 = snapshot.child("Pet").child("pet_image1").value.toString()
                    val link_pet2 = snapshot.child("Pet").child("pet_image2").value.toString()
                    val link_pet3 = snapshot.child("Pet").child("pet_image3").value.toString()

                    Picasso.get().load(link_pet).noFade().into(profilimage)
                    txt_name.text = snapshot.child("Pet").child("pet_name").value.toString()
                    txt_address.text = Address
                    txt_info.text = snapshot.child("Pet").child("pet_info").value.toString()
                    txt_sex.text = snapshot.child("Pet").child("pet_sex").value.toString()
                    Picasso.get().load(link_pet1).noFade().into(image_0)
                    Picasso.get().load(link_pet2).noFade().into(image_1)
                    Picasso.get().load(link_pet3).noFade().into(image_2)
                }
                else if(attention == "Receiver") {
                    val link_profile = snapshot.child("image").value.toString()
                    val Address = snapshot.child("Local").child("address").value.toString() + ", " +
                            snapshot.child("Local").child("area").value.toString() + ", " +
                            snapshot.child("Local").child("road").value.toString() + ", " +
                            snapshot.child("Local").child("province").value.toString()
                    val link_location1 = snapshot.child("Local").child("home_image1").value.toString()
                    val link_location2 = snapshot.child("Local").child("home_image2").value.toString()
                    val link_location3 = snapshot.child("Local").child("home_image3").value.toString()

                    Picasso.get().load(link_profile).noFade().into(profilimage)
                    txt_name.text = snapshot.child("username").value.toString()
                    txt_address.text = Address
                    txt_info.text = snapshot.child("Local").child("info").value.toString()
                    txt_sex.text = snapshot.child("gender").value.toString()
                    Picasso.get().load(link_location1).noFade().into(image_0)
                    Picasso.get().load(link_location2).noFade().into(image_1)
                    Picasso.get().load(link_location3).noFade().into(image_2)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //  onCancel
            }
        })
        return view
    }
}