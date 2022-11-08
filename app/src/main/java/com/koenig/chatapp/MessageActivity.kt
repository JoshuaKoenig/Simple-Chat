package com.koenig.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.koenig.chatapp.adapters.ChatAdapter
import com.koenig.chatapp.models.MessageModel
import java.lang.Exception

class MessageActivity : AppCompatActivity() {

    // THE MESSAGE MODEL
    private val messages = MutableLiveData<List<MessageModel>>()
    var observableMessages: LiveData<List<MessageModel>>
        get() = messages
        set(value) {messages.value = value.value}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        // THE RECEIVED INTENT
        val receivedIntent: Intent = Intent(Intent.ACTION_SEND)

        // THE MESSAGE INPUT
        val messageInput: EditText = findViewById(R.id.textMessage)

        // THE SEND MESSAGE BUTTON
        val buttonSendMessage: Button = findViewById(R.id.buttonSendMessage)

        // SET THE RECEIVED INTENT AS MESSAGE
        if (receivedIntent.hasExtra(Intent.EXTRA_TEXT))
        {
            messageInput.setText(receivedIntent.getStringExtra(Intent.EXTRA_TEXT))
        }

        // SEND THE MESSAGE
        buttonSendMessage.setOnClickListener {
            sendMessage(messageInput.text.toString())
        }

        // OBSERVE MESSAGES
        observableMessages.observe(this) { messages ->

            messages?.let {
                renderChatAdapter(messages as ArrayList<MessageModel>)
            }
        }
    }

    // RENDER THE MESSAGES
    private fun renderChatAdapter(messages: ArrayList<MessageModel>)
    {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewChat)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ChatAdapter(messages)
    }

    // SEND MESSAGES
    private fun sendMessage(messageInput: String)
    {
        val message = MessageModel()
        message.message = messageInput
        FirebaseMessageManager.sendMessage(message)
    }

    // RETRIEVE ALL MESSAGES
    private fun retrieveMessage()
    {
        try
        {
            FirebaseMessageManager.retrieveMessage(messages)
        }
        catch (e: Exception)
        {
            //TODO: CATCH EXCEPTION
        }
    }

    override fun onResume() {
        super.onResume()
        retrieveMessage()
    }
}