package com.pakkhaphon.cucumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.adapter.UserAdapter
import com.pakkhaphon.cucumber.model.Acceptmodel
import com.pakkhaphon.cucumber.model.Friendsmodel
import com.pakkhaphon.cucumber.model.Rejectmodel
import com.pakkhaphon.cucumber.model.Usersmodel

class Home : Fragment() {
    private lateinit var userRecycleView: RecyclerView
    private lateinit var usersList:ArrayList<Usersmodel>
    private lateinit var usersAdapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var usersDatabase: DatabaseReference
    private lateinit var snapHelper: PagerSnapHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mAuth = FirebaseAuth.getInstance()
        usersDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        val fragment:FragmentTransaction = fragmentManager?.beginTransaction()!!
        usersList = ArrayList()
        usersAdapter = UserAdapter(context, usersList, fragment)
        userRecycleView = view.findViewById(R.id.Recycle_userView)
        userRecycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        userRecycleView.adapter = usersAdapter
        snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(userRecycleView)

        // call users list
        usersDatabase.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                var currentfriend:Friendsmodel? = null
                val ar = arrayListOf<String>()
                val ar1 = arrayListOf<String>()
                val ar2 = arrayListOf<String>()
                val UserAttention = snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("attention").value
                for(item in snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("ConnectedTo").children) {
                    currentfriend = item.getValue(Friendsmodel::class.java)
                    ar.add(currentfriend?.fid!!)
                }

                for(item in snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("RejectTo").children) {
                    val RejectUser = item.getValue(Rejectmodel::class.java)
                    ar1.add(RejectUser?.uid!!)
                }

                for(item in snapshot.child(FirebaseAuth.getInstance().currentUser!!.uid).child("AcceptTo").children) {
                    val AcceptUser = item.getValue(Acceptmodel::class.java)
                    ar2.add(AcceptUser?.uid!!)
                }

                for(itemU in snapshot.children) {
                    var notExist: Boolean = true
                    val currentUsers = itemU.getValue(Usersmodel::class.java)
                    val iterator = ar.iterator()
                    val iterator1 = ar1.iterator()
                    val iterator2 = ar2.iterator()
                    if((currentUsers?.uid != FirebaseAuth.getInstance().currentUser!!.uid)&&(currentUsers?.attention != UserAttention)) {
                        friend@ while (iterator.hasNext()) {
                            val itemF = iterator.next()
                            if(currentUsers?.uid == itemF) {
                                notExist = false
                                iterator.remove()
                                break@friend
                            }
                        }
                        reject@ while (iterator1.hasNext()) {
                            val itemR = iterator1.next()
                            if(currentUsers?.uid == itemR) {
                                notExist = false
                                iterator1.remove()
                                break@reject
                            }
                        }
                        accept@ while (iterator2.hasNext()) {
                            val itemA = iterator2.next()
                            if(currentUsers?.uid == itemA) {
                                notExist = false
                                iterator2.remove()
                                break@accept
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