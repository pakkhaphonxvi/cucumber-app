package com.pakkhaphon.cucumber.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.Chat
import com.pakkhaphon.cucumber.R
import com.pakkhaphon.cucumber.model.Friendsmodel
import com.squareup.picasso.Picasso


class FriendsAdapter(val context: Context?, val friendList:ArrayList<Friendsmodel>):
    RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {
    private var Userdatabase = FirebaseDatabase.getInstance().reference.child("Users")
    private var mAuth = FirebaseAuth.getInstance().currentUser!!.uid
    private var friendAttention:String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.friends_list, parent,false)
        return FriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val currentFriends = friendList[position]
        FirebaseDatabase.getInstance().reference.child("Users").child(currentFriends.fid.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendAttention = snapshot.child("attention").value.toString()
                Log.d("friend", "attn1 = $friendAttention")
                if(friendAttention == "Receiver") {
                    Userdatabase.child(currentFriends.fid.toString()).addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            holder.friends_name_txt.text = snapshot.child("username").value.toString()
                            val link = snapshot.child("image").value.toString()
                            Picasso.get().load(link).noFade().into(holder.friends_image)
                            holder.itemView.setOnClickListener {
                                val i = Intent(context, Chat::class.java)
                                i.putExtra("name", snapshot.child("username").value.toString())
                                i.putExtra("uid", snapshot.child("uid").value.toString())
                                i.putExtra("image", snapshot.child("image").value.toString())
                                context!!.startActivity(i)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            //On Cancelled
                        }
                    })
                }
                else if(friendAttention.toString() == "Sender") {
                    val petdata = FirebaseDatabase.getInstance().reference.child("Users").child(currentFriends.fid.toString()).child("Pet")
                    petdata.addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            holder.friends_name_txt.text =  snapshot.child("pet_name").value.toString()
                            val link = snapshot.child("pet_profile").value.toString()
                            Picasso.get().load(link).noFade().into(holder.friends_image)
                            holder.itemView.setOnClickListener {
                                val i = Intent(context, Chat::class.java)
                                i.putExtra("name", snapshot.child("username").value.toString())
                                i.putExtra("uid", snapshot.child("uid").value.toString())
                                i.putExtra("image", snapshot.child("image").value.toString())
                                context!!.startActivity(i)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //On Cancelled
            }
        })
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    class FriendsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val friends_name_txt = itemView.findViewById<TextView>(R.id.friends_name_txt)
        val friends_image = itemView.findViewById<ImageView>(R.id.friends_image)
    }
}