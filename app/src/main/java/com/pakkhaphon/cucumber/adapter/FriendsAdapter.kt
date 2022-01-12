package com.pakkhaphon.cucumber.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.Chat
import com.pakkhaphon.cucumber.R
import com.pakkhaphon.cucumber.model.Friendsmodel


class FriendsAdapter(val context: Context?,val friendList:ArrayList<Friendsmodel>):
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {
    private var Userdatabase = FirebaseDatabase.getInstance().reference.child("Users")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.friends_list,parent,false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentFriends = friendList[position]
        Userdatabase.child(currentFriends.fid.toString()).addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                holder.friends_name_txt.text = snapshot.child("username").value.toString()
                holder.itemView.setOnClickListener {
                    val i  = Intent(context,Chat::class.java)
                    i.putExtra("name",snapshot.child("username").value.toString())
                    i.putExtra("uid",snapshot.child("uid").value.toString())
                    context!!.startActivity(i)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //On cancelled
            }
        })
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val friends_name_txt = itemView.findViewById<TextView>(R.id.friends_name_txt)
    }
}