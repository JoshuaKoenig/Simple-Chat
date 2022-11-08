package com.koenig.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE)

        // IF NO ACCOUNT EXISTS, CALL UP THE CREATE ACCOUNT ACTIVITY
        if(!sharedPreferences.getBoolean(getString(R.string.has_account_key), false))
        {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        // THE LOGIN BUTTON
        val loginButton: Button = findViewById(R.id.buttonLogin)

        // THE EMAIL INPUT
        val emailInput: EditText = findViewById(R.id.textEmail)

        // THE PASSWORD INPUT
        val passwordInput: EditText = findViewById(R.id.textPassword)

        // VALIDATE THE CREDENTIALS ENTERED
        loginButton.setOnClickListener {
            validateCredentials(emailInput.text.toString(), passwordInput.text.toString())
        }
    }

    private fun validateCredentials(emailInput: Any, passwordInput: String)
    {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE)

        // GET THE USER'S CREDENTIALS
        val email = sharedPreferences.getString(getString(R.string.email_key), "")
        val password = sharedPreferences.getString(getString(R.string.pw_key), "")

        // COMPARE USER CREDENTIALS WITH USER INPUT
        if (email == emailInput && password == passwordInput)
        {
            // START THE MESSAGE-ACTIVITY
            val intent = Intent(this, MessageActivity::class.java)
            startActivity(intent)
        }
        else
        {
            // WRONG CREDENTIALS ENTERED
            Toast.makeText(this, "WRONG CREDENTIALS", Toast.LENGTH_LONG).show()
        }

    }
}