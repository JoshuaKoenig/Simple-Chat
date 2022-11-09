package com.koenig.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE)

        // IF USER IS NOT LOGGED IN, CALL UP THE REDIRECT ACTIVITY
        if(!sharedPreferences.getBoolean(getString(R.string.is_user_logged_in), false))
        {
            // USER IS NOT LOGGED IN
            val intent = Intent(this, RedirectActivity::class.java)
            startActivity(intent)
        }

        // THE MESSAGE INPUT
        val messageInput: EditText = findViewById(R.id.textMessage)

        // THE SEND MESSAGE BUTTON
        val buttonSendMessage: Button = findViewById(R.id.buttonSendMessage)

        // SET THE RECEIVED INTENT AS MESSAGE
        if (intent.hasExtra(Intent.EXTRA_TEXT))
        {
            messageInput.setText(intent.getStringExtra(Intent.EXTRA_TEXT))
        }

        // WHEN THIS ACTIVITY IS CALLED FROM A THIRD PARTY APP WITHOUT AN INTENT EXTRA
        // THIS IS A SUSPICIOUS BEHAVIOR => CALL UP THE REDIRECT ACTIVITY
        validateSuspiciousBehaviour(sharedPreferences, intent)

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

    // VALIDATE SUSPICIOUS BEHAVIOUR
    private fun validateSuspiciousBehaviour(sharedPreferences: SharedPreferences, receivedIntent: Intent)
    {
        if (!sharedPreferences.getBoolean(getString(R.string.is_called_from_login_activity), false)
            && !receivedIntent.hasExtra(Intent.EXTRA_TEXT))
        {
            // SUSPICIOUS BEHAVIOR
            val intent = Intent(this, RedirectActivity::class.java)
            startActivity(intent)
        }

        // SET THE FLAG TO FALSE AGAIN
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.is_called_from_login_activity), false)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        retrieveMessage()
    }
}