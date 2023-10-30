package com.dsm_udb.myapplication_foro1_dsm_udb

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.dsm_udb.myapplication_foro1_dsm_udb.database.DatabaseHelper
import com.dsm_udb.myapplication_foro1_dsm_udb.ProductsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var edUser: EditText
    private lateinit var edPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerButton: Button
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edUser = findViewById(R.id.edUser)
        edPassword = findViewById(R.id.edPassword)
        loginBtn = findViewById(R.id.loginBtn)
        registerButton = findViewById(R.id.registerButton)
        databaseHelper = DatabaseHelper(this)

        loginBtn.setOnClickListener {
            val username = edUser.text.toString()
            val password = edPassword.text.toString()

            if (isValidUser(username, password)) {
                // Inicio de sesión exitoso
                Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                // Redirige al usuario a la actividad ProductsActivity
                val intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)
            } else {
                // Inicio de sesión fallido
                Toast.makeText(this, "Inicio de sesión fallido. Verifica tus credenciales.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isValidUser(username: String, password: String): Boolean {
        val isUserValid = databaseHelper.isValidUser(username, password)
        return isUserValid
    }
}

