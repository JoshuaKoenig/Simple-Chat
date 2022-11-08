package com.koenig.chatapp

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.koenig.chatapp.models.MessageModel
import com.google.firebase.database.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FirebaseMessageManager {

    // THE DATABASE REFERENCE
    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    // SEND MESSAGES
    fun sendMessage(message: MessageModel)
    {
        val key = database.child("messages").push().key ?: return
        message.uid = key

        val messageValues = message.toMap()
        val childAdd = HashMap<String, Any>()

        // ADD MESSAGE TO DB
        childAdd["/messages/$key"] = messageValues

        database.updateChildren(childAdd)
    }

    // GET THE MESSAGES
    fun retrieveMessage(messages: MutableLiveData<List<MessageModel>>)
    {
        database.child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val localMessages = ArrayList<MessageModel>()
                    val children = snapshot.children

                    children.forEach {
                        val message = it.getValue(MessageModel::class.java)
                        localMessages.add(message!!)
                    }
                    messages.value = localMessages
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}