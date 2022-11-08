package com.koenig.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        // CREATE ACCOUNT BUTTON
        val createAccountButton: Button = findViewById(R.id.buttonCreateAccount)

        // EMAIL INPUT
        val emailInput: EditText = findViewById(R.id.textNewEmail)

        // PASSWORD INPUT
        val passwordInput: EditText = findViewById(R.id.textNewPassword)

        // CREATE THE ACCOUNT WHEN INPUT FIELDS ARE NOT EMPTY
        createAccountButton.setOnClickListener {

            if (!emailInput.text.isNullOrBlank() && !passwordInput.text.isNullOrBlank())
            {
                createAccount(emailInput.text.toString(), passwordInput.text.toString())
            }
        }
    }

    private fun createAccount(emailInput: String, passwordInput: String)
    {
        // SHARED PREFERENCES
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(getString(R.string.shared_prefs_key), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // SET HAS ACCOUNT
        editor.putBoolean(getString(R.string.has_account_key), true)

        // SET EMAIL
        editor.putString(getString(R.string.email_key), emailInput)

        // SET PW
        editor.putString(getString(R.string.pw_key), passwordInput)

        editor.apply()

        // NAVIGATE TO THE LOGIN SCREEN
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}