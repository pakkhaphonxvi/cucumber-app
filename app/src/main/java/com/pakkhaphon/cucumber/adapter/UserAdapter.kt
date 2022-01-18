package com.pakkhaphon.cucumber.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pakkhaphon.cucumber.R
import com.pakkhaphon.cucumber.model.Friendsmodel
import com.pakkhaphon.cucumber.model.Usersmodel
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class UserAdapter (val context: Context?, val useList:ArrayList<Usersmodel>):
    RecyclerView.Adapter<UserAdapter.UsersViewHolder>() {
    val userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.users_list,parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val currentuser = useList[position]
        holder.users_name_txt.text = currentuser.username
        Picasso.get().load(currentuser.image).noFade().into(holder.imageUser)

        holder.btnAccept.setOnClickListener {
            var acceptid:Long = 0
            val acceptdatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("AcceptTo")
            acceptdatabase.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    acceptid = snapshot.childrenCount
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            val data = HashMap<String,Any>()
            data["uid"] = currentuser.uid.toString()
            acceptdatabase.child(acceptid.toString()).setValue(data)
        }

        holder.btnReject.setOnClickListener {
            var rejectid:Long = 0
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
}