package com.pakkhaphon.cucumber.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.Home
import com.pakkhaphon.cucumber.HomeActivity
import com.pakkhaphon.cucumber.ProfileDetail
import com.pakkhaphon.cucumber.R
import com.pakkhaphon.cucumber.model.Acceptmodel
import com.pakkhaphon.cucumber.model.Usersmodel
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.HashMap

class UserAdapter (val context: Context?, val useList:ArrayList<Usersmodel>, val fragmentTransaction: FragmentTransaction): RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {
    var acceptid:Long = 0
    var sendid:Long = 0
    var receiveid:Long = 0
    var receiveid1:Long = 0
    var sendid1:Long = 0
    var rejectid:Long = 0
    lateinit var x: HomeActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.users_list, parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val currentuser = useList[position]
        if(currentuser.attention == "Sender") {
            val petdata = FirebaseDatabase.getInstance().reference.child("Users").child(currentuser.uid.toString()).child("Pet")
            petdata.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    holder.users_name_txt.text = snapshot.child("pet_name").value.toString()
                    var link = snapshot.child("pet_profile").value.toString()
                    Picasso.get().load(link).noFade().into(holder.imageUser)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        else if(currentuser.attention == "Receiver") {
            holder.users_name_txt.text = currentuser.username
            Picasso.get().load(currentuser.image).noFade().into(holder.imageUser)
        }

        holder.imageUser.setOnClickListener {
            val bundle = Bundle()
            val fragmentProfile = ProfileDetail()
            bundle.putString("uid", currentuser.uid.toString())
            fragmentProfile.arguments = bundle
            fragmentTransaction.replace(R.id.fragment_space, fragmentProfile).addToBackStack(null).commit()
        }

        holder.btnAccept.setOnClickListener {
            useList.removeAt(position)
            notifyDataSetChanged()
            val acceptdatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("AcceptTo")
            acceptdatabase.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    acceptid = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            val data = HashMap<String,Any>()
            data["uid"] = currentuser.uid.toString()
            acceptdatabase.child(acceptid.toString()).setValue(data)
            val userdatabase = FirebaseDatabase.getInstance().reference.child("Users")
            val send = FirebaseDatabase.getInstance().reference.child("Users").child(currentuser.uid.toString()).child("ConnectedTo")
            val send1 = FirebaseDatabase.getInstance().reference.child("Users").child(currentuser.uid.toString()).child("AcceptTo")

            send.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    sendid = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            send1.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    sendid1 = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            val receive = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo")
            val receive1 = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("AcceptTo")
            receive.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    receiveid = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            receive1.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    receiveid1 = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            userdatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("TAG", "onBindViewHolder: ${currentuser.uid.toString()}")
                    Log.d("TAG", "onBindViewHolder: ${FirebaseAuth.getInstance().currentUser!!.uid}")
                     for(item in snapshot.child(currentuser.uid.toString()).child("AcceptTo").children) {
                        val itemA = item.getValue(Acceptmodel::class.java)
                        if(itemA?.uid == FirebaseAuth.getInstance().currentUser!!.uid) {
                            Log.d("TAG", "onDataChange: ${sendid1}")
                            Log.d("TAG", "onDataChange: ${receiveid1}")
                            val a = HashMap<String,Any>()
                            a["fid"] = currentuser.uid.toString()
                            val b = HashMap<String,Any>()
                            b["fid"] = FirebaseAuth.getInstance().currentUser!!.uid
                            val c = currentuser.uid.toString()
                            userdatabase.child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo").child(receiveid.toString()).setValue(a)
                            userdatabase.child(currentuser.uid.toString()).child("ConnectedTo").child(sendid.toString()).setValue(b)
                            x = HomeActivity()
                            x.showDialog(context!!, c)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        holder.btnReject.setOnClickListener {
            useList.removeAt(position)
            notifyDataSetChanged()
            val rejectdatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("RejectTo")
            rejectdatabase.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    rejectid = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
            val data = HashMap<String,Any>()
            data["uid"] = currentuser.uid.toString()
            rejectdatabase.child(rejectid.toString()).setValue(data)
        }
    }

    override fun getItemCount(): Int {
        return useList.size
    }

    class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val users_name_txt = itemView.findViewById<TextView>(R.id.users_name_txt)
        val imageUser = itemView.findViewById<ImageView>(R.id.imageUser_home)
        val btnAccept = itemView.findViewById<Button>(R.id.btn_Accept)
        val btnReject = itemView.findViewById<Button>(R.id.btn_Reject)
    }

//    private fun showDialog() {
//        val dialog = Dialog(x.)
//        dialog.setContentView(R.layout.layout_dialog)
//        dialog.show()
//    }
}