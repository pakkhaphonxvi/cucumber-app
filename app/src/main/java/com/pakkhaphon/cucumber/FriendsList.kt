package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.adapter.FriendsAdapter
import com.pakkhaphon.cucumber.model.Friendsmodel
import com.pakkhaphon.cucumber.model.Usersmodel

class FriendsList : Fragment() {
    private lateinit var friendRecycleView:RecyclerView
    private lateinit var friendsList:ArrayList<Friendsmodel>
    private lateinit var friendsAdapter: FriendsAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var friendsDatabase:DatabaseReference
    private lateinit var usersList:ArrayList<Usersmodel>
    private lateinit var usersDatabase: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friendslist, container, false)
        var UserAttention = ""

        mAuth = FirebaseAuth.getInstance()
        friendsDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo")
        friendsList = ArrayList()
        friendsAdapter = FriendsAdapter(context, friendsList)
        friendRecycleView = view.findViewById(R.id.Recycle_friendsList)
        friendRecycleView.layoutManager = LinearLayoutManager(context)
        friendRecycleView.adapter = friendsAdapter
        usersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        usersList = ArrayList()

        usersDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                UserAttention = snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("attention").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // On Cancel
            }
        })

        // call friends list
        friendsDatabase.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendsList.clear()
                var show: Boolean = false
                if(UserAttention == "Sender") {
                    for(item in snapshot.children) {
                        if(show != true) {
                            val currentFriends = item.getValue(Friendsmodel::class.java)
                            friendsList.add(currentFriends!!)
                            show = true
                        }
                    }
                }
                else {
                    for(item in snapshot.children) {
                        val currentFriends = item.getValue(Friendsmodel::class.java)
                        friendsList.add(currentFriends!!)
                    }
                }
                friendsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // On Cancel
            }
        })
        return view
    }
}