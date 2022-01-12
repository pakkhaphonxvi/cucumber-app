package com.pakkhaphon.cucumber.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun getItemCount(): Int {
        return useList.size
    }

    class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val users_name_txt = itemView.findViewById<TextView>(R.id.users_name_txt)
    }
}