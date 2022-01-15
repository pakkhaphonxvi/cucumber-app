package com.pakkhaphon.cucumber

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.adapter.FriendsAdapter
import com.pakkhaphon.cucumber.adapter.UserAdapter
import com.pakkhaphon.cucumber.model.Friendsmodel
import com.pakkhaphon.cucumber.model.Usersmodel
import kotlin.math.log


class Home : Fragment() {
    private lateinit var userRecycleView: RecyclerView
    private lateinit var usersList:ArrayList<Usersmodel>
    private lateinit var usersAdapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usersDatabase: DatabaseReference

    private lateinit var friendsList: ArrayList<Friendsmodel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance()
        usersDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        usersList = ArrayList()
        usersAdapter = UserAdapter(context,usersList)
        userRecycleView = view.findViewById(R.id.Recycle_userView)
        userRecycleView.layoutManager = LinearLayoutManager(context)
        userRecycleView.adapter = usersAdapter

        friendsList = ArrayList()
        // call users list
        usersDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                var currentfriend:Friendsmodel? = null
                val ar = arrayListOf<String>()
                for(item1 in snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo").children) {
                    currentfriend = item1.getValue(Friendsmodel::class.java)
                    ar.add(currentfriend?.fid!!)
                }

                for(itemU in snapshot.children) {
                    var notExist: Boolean = true
                    val currentUsers = itemU.getValue(Usersmodel::class.java)
                    val iterator = ar.iterator()
                    if(currentUsers?.uid != FirebaseAuth.getInstance().currentUser!!.uid) {
                        friend@ while (iterator.hasNext()) {
                            val itemF = iterator.next()
                            if(currentUsers?.uid == itemF) {
                                notExist = false
                                iterator.remove()
                                break@friend
                            }
                        }
                        if(notExist) {
                            usersList.add(currentUsers!!)
                        }
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // OnCancel
            }
        })
        return view
    }
}