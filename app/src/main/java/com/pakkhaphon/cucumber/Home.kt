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



        // call users list
        usersDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                var currentfriend:Friendsmodel? = null
                val ar = arrayListOf<String>()
                for(item1 in snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo").children){
                    currentfriend = item1.getValue(Friendsmodel::class.java)
                    ar.add(currentfriend?.fid!!)
                }


                for(item in snapshot.children) {
                    val currentUsers = item.getValue(Usersmodel::class.java)
                    Log.d("home", "2: ${currentUsers?.uid}")

                    if((currentUsers?.uid != FirebaseAuth.getInstance().currentUser!!.uid) && currentUsers?.uid != ar[0] ) {
                        usersList.add(currentUsers!!)

                    }
                }
                usersAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

        return view
    }

}