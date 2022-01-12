package com.pakkhaphon.cucumber

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pakkhaphon.cucumber.adapter.MessageAdapter
import com.pakkhaphon.cucumber.model.Messagemodel
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlin.collections.ArrayList


class Chat : AppCompatActivity() {

    private lateinit var chatRecycleView:RecyclerView
    private lateinit var messageBox:EditText
    private lateinit var sentButton:ImageView
    private lateinit var titlechathead:TextView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Messagemodel>
    private lateinit var MessageDatabase:DatabaseReference

    var receiveRoom:String = ""
    var senderRoom:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiveUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        MessageDatabase = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiveUid + senderUid
        receiveRoom = senderUid + receiveUid

        titlechathead = findViewById(R.id.title_chat_head)
        titlechathead.text = name

        chatRecycleView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.Sent_btn)

        messageBox.setText("")
        sentButton.isEnabled = false

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)

        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = false

        chatRecycleView.layoutManager = layoutManager
        chatRecycleView.adapter = messageAdapter


        MessageDatabase.child("chat").child(senderRoom).child("message")
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for(item in snapshot.children) {
                        val message = item.getValue(Messagemodel::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                    chatRecycleView.scrollToPosition(messageAdapter.itemCount - 1)
                }
                override fun onCancelled(error: DatabaseError) {
                    //On cancelled
                }
            })

        messageBox.setOnFocusChangeListener(object :View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                messageBox.addTextChangedListener(object :TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        //BeforeTextChanged
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if(messageBox.text.toString() == ""){
                            sentButton.isEnabled = false
                        }
                        else if(!messageBox.text.equals("")){
                            sentButton.isEnabled = true
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        // AfterTextChanged
                    }
                })
                chatRecycleView.scrollToPosition(messageAdapter.itemCount - 1)
            }
        })

        chatRecycleView.addOnLayoutChangeListener(object :View.OnLayoutChangeListener{
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                if(bottom < oldBottom) {
                    chatRecycleView.postDelayed(object :Runnable {
                        override fun run() {
                            chatRecycleView.smoothScrollToPosition(messageAdapter.itemCount)
                        }
                    },0)
                }
            }
        })

        sentButton.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObject = Messagemodel(message,senderUid)
//            val data = HashMap<String,Any>()
//            data["message"] = messageBox.text.toString()
//            data["senderId"] = senderUid!!


            MessageDatabase.child("chat").child(senderRoom).child("message").push().setValue(messageObject).addOnCompleteListener {
                if(it.isSuccessful) {
                    MessageDatabase.child("chat").child(receiveRoom).child("message").push().setValue(messageObject)
                }
            }
            messageBox.setText("")
        }

    }

    fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken,0)
    }
}


