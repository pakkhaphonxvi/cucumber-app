package com.pakkhaphon.cucumber

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet


class Profile : Fragment() {

    var selectphotouri: Uri? = null
    lateinit var uploadprofils:CircleImageView
    lateinit var userDatabase:DatabaseReference
    lateinit var uploadlocation:CircleImageView
    lateinit var userlocationDatabase:DatabaseReference
    lateinit var userpetDatabase:DatabaseReference
    lateinit var uploadpet:CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        userlocationDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Local")
        userpetDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Pet")

        //retrieved data from users db
        userDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("image").value.toString()
                val showimage = view.findViewById<CircleImageView>(R.id.profile_image)
                Picasso.get().load(link).noFade().into(showimage)
                val txt_username = view.findViewById<TextView>(R.id.txt_username)
                txt_username.text = snapshot.child("username").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //retrieved data from userlocal db
        userlocationDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("image").value.toString()
                val showimagelocal = view.findViewById<CircleImageView>(R.id.image_location)
                Picasso.get().load(link).noFade().into(showimagelocal)
                val txt_address = view.findViewById<TextInputEditText>(R.id.edit_address)
                txt_address.setText(snapshot.child("address").value.toString())
                val txt_road = view.findViewById<TextInputEditText>(R.id.edit_road)
                txt_road.setText(snapshot.child("road").value.toString())
                val txt_area = view.findViewById<TextInputEditText>(R.id.edit_area)
                txt_area.setText(snapshot.child("area").value.toString())
                val txt_province = view.findViewById<TextInputEditText>(R.id.edit_province)
                txt_province.setText(snapshot.child("province").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        //uploadimageperson
        uploadprofils = view.findViewById(R.id.profile_image)
        uploadprofils.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent,0)
        }

        val layout_person = view.findViewById<LinearLayout>(R.id.layout_Person)
        layout_person.isVisible = false
        val layout_pet = view.findViewById<LinearLayout>(R.id.layout_Pet)
        layout_pet.isVisible = false

        val button_person = view.findViewById<Button>(R.id.button_person)
        button_person.setOnClickListener {
            layout_person.isVisible = true
            layout_pet.isVisible = false
        }
        val button_pet = view.findViewById<Button>(R.id.button_pet)
        button_pet.setOnClickListener {
            layout_person.isVisible = false
            layout_pet.isVisible = true
        }

        //uploadlocation
        uploadlocation = view.findViewById(R.id.image_location)
        uploadlocation.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        val button_save_person = view.findViewById<Button>(R.id.button_save_person)
        button_save_person.setOnClickListener {

            val address = view.findViewById<TextInputEditText>(R.id.edit_address)
            val road = view.findViewById<TextInputEditText>(R.id.edit_road)
            val area = view.findViewById<TextInputEditText>(R.id.edit_area)
            val province = view.findViewById<TextInputEditText>(R.id.edit_province)

            val data = HashMap<String,Any>()
            data["address"] = address.text.toString()
            data["road"] = road.text.toString()
            data["province"] = province.text.toString()
            data["area"] = area.text.toString()

            userlocationDatabase.updateChildren(data)

        }

        //uploadpet
        uploadpet = view.findViewById(R.id.image_pet)
        uploadpet.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,2)
        }

        val pet_sex_target = arrayListOf<String>("Sex","Male","Female")
        val spinnerAdapterSex = context?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,pet_sex_target) }
        val spinner_sex = view.findViewById<Spinner>(R.id.spinner_gender_pet)
        spinner_sex.adapter = spinnerAdapterSex
        var pet_sex = ""

        spinner_sex.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(pet_sex_target[position] == "Male")
                {
//                   spinnerAdapterSex?.remove("Sex")
                   pet_sex = "Male"
                }
                else if(pet_sex_target[position] == "Female")
                {
//                    spinnerAdapterSex?.remove("Sex")
                    pet_sex = "Female"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val button_save_pet = view.findViewById<Button>(R.id.button_save_pet)
        button_save_pet.setOnClickListener {

            val petname = view.findViewById<TextInputEditText>(R.id.edit_petname)
            val petinfo = view.findViewById<TextInputEditText>(R.id.edit_info_pet)

            val data = HashMap<String,Any>()
            data["petname"] = petname.text.toString()
            data["sex"] = pet_sex
            data["info"] = petinfo.text.toString()

            userpetDatabase.updateChildren(data)

        }

        //retrieved data from userpet db
        userpetDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("image").value.toString()
                val showimagepet = view.findViewById<CircleImageView>(R.id.image_pet)
                Picasso.get().load(link).noFade().into(showimagepet)
                val txt_petname = view.findViewById<TextInputEditText>(R.id.edit_petname)
                txt_petname.setText(snapshot.child("petname").value.toString())
                val txt_info = view.findViewById<TextInputEditText>(R.id.edit_info_pet)
                txt_info.setText(snapshot.child("info").value.toString())
                val txt_sex = snapshot.child("sex").value.toString()
                if(txt_sex == "Male")
                {
                    spinner_sex.setSelection(spinnerAdapterSex!!.getPosition("Male"))
                }
                else if(txt_sex == "Female")
                {
                    spinner_sex.setSelection(spinnerAdapterSex!!.getPosition("Female"))
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null)
        {
            selectphotouri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectphotouri)
            val bitmaDrawable = BitmapDrawable(bitmap)
            uploadprofils.setImageIcon(null)
            uploadprofils.setBackgroundDrawable(bitmaDrawable)

            uploadimageperson()

        }
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadlocation.setImageIcon(null)
            uploadlocation.setBackgroundDrawable(bitmapDrawable)

            uploadimagelocation(uri)
        }
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadpet.setImageIcon(null)
            uploadpet.setBackgroundDrawable(bitmapDrawable)

            uploadimagepet(uri)
        }

    }

    private fun uploadimagepet(uri: Uri?) {
        if(uri == null){ return }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userpet = HashMap<String,Any>()
                userpet["image"] = it.toString()

                userpetDatabase.updateChildren(userpet)
            }
        }

    }

    private fun uploadimagelocation(uri: Uri?) {
        if(uri == null){ return }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userlocation = HashMap<String,Any>()
                userlocation["image"] = it.toString()

                userlocationDatabase.updateChildren(userlocation)
            }
        }

    }

    private fun uploadimageperson() {
        if(selectphotouri == null){ return }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectphotouri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val user = HashMap<String,Any>()
                user["image"] = it.toString()

                userDatabase.updateChildren(user)
            }
        }
    }




}