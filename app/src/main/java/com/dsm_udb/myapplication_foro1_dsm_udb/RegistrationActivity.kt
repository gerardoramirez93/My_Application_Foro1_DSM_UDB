package com.dsm_udb.myapplication_foro1_dsm_udb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.ContentValues
import android.content.Intent
import com.dsm_udb.myapplication_foro1_dsm_udb.database.DatabaseHelper



class RegistrationActivity : AppCompatActivity() {

    private lateinit var edFirstName: EditText
    private lateinit var edLastName: EditText
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        edFirstName = findViewById(R.id.edFirstName)
        edLastName = findViewById(R.id.edLastName)
        edUsername = findViewById(R.id.edUsername)
        edPassword = findViewById(R.id.edPassword)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val firstName = edFirstName.text.toString()
            val lastName = edLastName.text.toString()
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()

            // Crea una instancia de DatabaseHelper para acceder a la base de datos
            val dbHelper = DatabaseHelper(this)

            // Inserta el nuevo usuario en la base de datos
            val newUserValues = ContentValues()
            newUserValues.put("Username", username)
            newUserValues.put("Password", password)
            newUserValues.put("FirstName", firstName)
            newUserValues.put("LastName", lastName)
            val db = dbHelper.writableDatabase
            val result = db.insert("User", null, newUserValues)

            if (result != -1L) {
                // Registro exitoso
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                // Redirige al usuario a la pantalla de inicio de sesión después del registro exitoso
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Error en el registro
                Toast.makeText(this, "Error en el registro. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
            }

            db.close()
        }
    }
}

