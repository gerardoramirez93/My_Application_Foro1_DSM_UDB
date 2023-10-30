package com.dsm_udb.myapplication_foro1_dsm_udb

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsm_udb.myapplication_foro1_dsm_udb.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PaymentActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        databaseHelper = DatabaseHelper(this)

        val cardNumberEditText = findViewById<EditText>(R.id.cardNumberEditText)
        val expirationDateEditText = findViewById<EditText>(R.id.expirationDateEditText)
        val cvvEditText = findViewById<EditText>(R.id.cvvEditText)
        val payButton = findViewById<Button>(R.id.payButton)

        payButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString()
            val expirationDate = expirationDateEditText.text.toString()
            val cvv = cvvEditText.text.toString()

            if (validateCardData(cardNumber, expirationDate, cvv)) {
                // La tarjeta es válida, realiza la compra.
                performPurchase()

                // Muestra un mensaje de confirmación.
                Toast.makeText(this, "¡Pago exitoso!", Toast.LENGTH_SHORT).show()

                // Navega de regreso a la actividad ProductsActivity.
                val intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)
            } else {
                // La validación falló, muestra un mensaje de error.
                Toast.makeText(this, "Error en los datos de la tarjeta. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateCardData(cardNumber: String, expirationDate: String, cvv: String): Boolean {
        // Verifica que los campos no estén vacíos.
        if (cardNumber.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty()) {
            return false
        }

        // Verifica que la fecha de vencimiento sea válida y posterior a la fecha actual.
        if (!isValidExpirationDate(expirationDate)) {
            return false
        }

        return true
    }

    private fun isValidExpirationDate(expirationDate: String): Boolean {
        try {
            val currentDate = SimpleDateFormat("MM/yy", Locale.getDefault()).format(Date())
            val inputDate = SimpleDateFormat("MM/yy", Locale.getDefault()).parse(expirationDate)
            val currentDateParsed = SimpleDateFormat("MM/yy", Locale.getDefault()).parse(currentDate)

            if (inputDate != null && currentDateParsed != null) {
                // Compara la fecha ingresada con la fecha actual.
                return inputDate.after(currentDateParsed)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun performPurchase() {
        // Obtiene la lista de productos en el carrito antes de realizar la compra.
        val cartItems = databaseHelper.getCartItems(this)

        // Vacía el carrito después de la compra.
        databaseHelper.clearCart()
    }
}


