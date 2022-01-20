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

    private var selectphotouri: Uri? = null
    private lateinit var uploadprofils:CircleImageView
    private lateinit var userDatabase:DatabaseReference
    private lateinit var uploadlocation:ImageView
    private lateinit var uploadlocation1:ImageView
    private lateinit var uploadlocation2:ImageView
    private lateinit var userlocationDatabase:DatabaseReference
    private lateinit var userpetDatabase:DatabaseReference
    private lateinit var uploadpetprofile:CircleImageView
    private lateinit var uploadpetimage:ImageView
    private lateinit var uploadpetimage1:ImageView
    private lateinit var uploadpetimage2:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
        userlocationDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Local")
        userpetDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("Pet")

        //retrieved data from users db
        userDatabase.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("image").value.toString()
                val showimage = view.findViewById<CircleImageView>(R.id.profile_image)
                Picasso.get().load(link).noFade().into(showimage)
                val txt_username = view.findViewById<TextView>(R.id.txt_username)
                txt_username.text = snapshot.child("username").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // onCancel
            }
        })

        //retrieved data from userlocal db
        userlocationDatabase.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("home_image").value.toString()
                val link1 = snapshot.child("home_image1").value.toString()
                val link2 = snapshot.child("home_image2").value.toString()
                val showimagelocal = view.findViewById<ImageView>(R.id.image_location)
                val showimagelocal1 = view.findViewById<ImageView>(R.id.image_location1)
                val showimagelocal2 = view.findViewById<ImageView>(R.id.image_location2)
                Picasso.get().load(link).noFade().into(showimagelocal)
                Picasso.get().load(link1).noFade().into(showimagelocal1)
                Picasso.get().load(link2).noFade().into(showimagelocal2)
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
                // onCancel
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
        uploadlocation1 = view.findViewById(R.id.image_location1)
        uploadlocation1.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,2)
        }
        uploadlocation2 = view.findViewById(R.id.image_location2)
        uploadlocation2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,3)
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
        uploadpetprofile = view.findViewById(R.id.pet_profile)
        uploadpetprofile.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,4)
        }
        uploadpetimage = view.findViewById(R.id.pet_image)
        uploadpetimage.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,5)
        }
        uploadpetimage1 = view.findViewById(R.id.pet_image1)
        uploadpetimage1.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,6)
        }
        uploadpetimage2 = view.findViewById(R.id.pet_image2)
        uploadpetimage2.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            startActivityForResult(i,7)
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
                // onNothingSelected
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
        userpetDatabase.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val link = snapshot.child("pet_profile").value.toString()
                val link1 = snapshot.child("pet_image").value.toString()
                val link2 = snapshot.child("pet_image1").value.toString()
                val link3 = snapshot.child("pet_image2").value.toString()
                val showimagepet = view.findViewById<CircleImageView>(R.id.pet_profile)
                val showimagepet1 = view.findViewById<ImageView>(R.id.pet_image)
                val showimagepet2 = view.findViewById<ImageView>(R.id.pet_image1)
                val showimagepet3 = view.findViewById<ImageView>(R.id.pet_image2)
                Picasso.get().load(link).noFade().into(showimagepet)
                Picasso.get().load(link1).noFade().into(showimagepet1)
                Picasso.get().load(link2).noFade().into(showimagepet2)
                Picasso.get().load(link3).noFade().into(showimagepet3)
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
                // onCancelled
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
            uploadlocation1.setImageIcon(null)
            uploadlocation1.setBackgroundDrawable(bitmapDrawable)
            uploadimagelocation1(uri)
        }
        if(requestCode == 3 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadlocation2.setImageIcon(null)
            uploadlocation2.setBackgroundDrawable(bitmapDrawable)
            uploadimagelocation2(uri)
        }
        if(requestCode == 4 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadpetprofile.setImageIcon(null)
            uploadpetprofile.setBackgroundDrawable(bitmapDrawable)
            uploadimagepet(uri)
        }
        if(requestCode == 5 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadpetimage.setImageIcon(null)
            uploadpetimage.setBackgroundDrawable(bitmapDrawable)
            uploadimagepet1(uri)
        }
        if(requestCode == 6 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadpetimage1.setImageIcon(null)
            uploadpetimage1.setBackgroundDrawable(bitmapDrawable)
            uploadimagepet2(uri)
        }
        if(requestCode == 7 && resultCode == Activity.RESULT_OK && data != null)
        {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            uploadpetimage2.setImageIcon(null)
            uploadpetimage2.setBackgroundDrawable(bitmapDrawable)
            uploadimagepet3(uri)
        }
    }

    private fun uploadimagepet(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userpet = HashMap<String,Any>()
                userpet["pet_profile"] = it.toString()
                userpetDatabase.updateChildren(userpet)
            }
        }
    }

    private fun uploadimagepet1(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userpet = HashMap<String,Any>()
                userpet["pet_image"] = it.toString()
                userpetDatabase.updateChildren(userpet)
            }
        }
    }

    private fun uploadimagepet2(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userpet = HashMap<String,Any>()
                userpet["pet_image1"] = it.toString()
                userpetDatabase.updateChildren(userpet)
            }
        }
    }

    private fun uploadimagepet3(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userpet = HashMap<String,Any>()
                userpet["pet_image2"] = it.toString()
                userpetDatabase.updateChildren(userpet)
            }
        }
    }

    private fun uploadimagelocation(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userlocation = HashMap<String,Any>()
                userlocation["home_image"] = it.toString()
                userlocationDatabase.updateChildren(userlocation)
            }
        }
    }

    private fun uploadimagelocation1(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userlocation = HashMap<String,Any>()
                userlocation["home_image1"] = it.toString()
                userlocationDatabase.updateChildren(userlocation)
            }
        }
    }
    private fun uploadimagelocation2(uri: Uri?) {
        if(uri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {
                val userlocation = HashMap<String,Any>()
                userlocation["home_image2"] = it.toString()
                userlocationDatabase.updateChildren(userlocation)
            }
        }
    }

    private fun uploadimageperson() {
        if(selectphotouri == null) {
            return
        }

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