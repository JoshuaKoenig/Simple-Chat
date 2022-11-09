package com.koenig.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RedirectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redirect)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE)

        // THE REDIRECT BUTTON
        val redirectButton: Button = findViewById(R.id.buttonRedirect)

        // SET USER ISN'T LOGGED IN
        val editor = sharedPreferences.edit()
        editor.putBoolean(getString(R.string.is_user_logged_in), false)
        editor.apply()

        // NAVIGATE TO LOGIN PAGE
        redirectButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}