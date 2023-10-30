package com.dsm_udb.myapplication_foro1_dsm_udb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm_udb.myapplication_foro1_dsm_udb.adapters.CartAdapter
import com.dsm_udb.myapplication_foro1_dsm_udb.models.CartItem
import com.dsm_udb.myapplication_foro1_dsm_udb.database.DatabaseHelper

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.title = "Carrito de Compras"

        recyclerView = findViewById(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

        // Obtén la lista de productos en el carrito desde la base de datos
        val cartItemList = getCartItems(this).toMutableList() // Convierte a MutableList

        cartAdapter = CartAdapter(cartItemList)
        recyclerView.adapter = cartAdapter

        val totalPriceTextView = findViewById<TextView>(R.id.totalPriceTextView)
        val checkoutButton = findViewById<Button>(R.id.checkoutButton)

        // Calcular el total de la compra
        val totalPrice = calculateTotalPrice(cartItemList)
        totalPriceTextView.text = "Total: $$totalPrice"

        checkoutButton.setOnClickListener {
            // Implementa aquí la lógica para completar la compra y redirigir al usuario a la página de pago (PaymentActivity).
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCartItems(context: Context): List<CartItem> {
        return databaseHelper.getCartItems(context)
    }

    private fun calculateTotalPrice(cartItemList: List<CartItem>): Double {
        var totalPrice = 0.0
        for (item in cartItemList) {
            totalPrice += item.product.price
        }
        return totalPrice
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_products -> {
                // Acción para el ítem "PRODUCTOS"
                // Redirige a la pantalla de productos (ProductsActivity).
                val intent = Intent(this, ProductsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_cart -> {
                // Acción para el ítem "CARRITO"
                // No es necesario hacer nada aquí, ya estamos en la actividad del carrito.
                return true
            }
            R.id.menu_logout -> {
                // Acción para el ítem "CERRAR SESIÓN"
                // Cierra la sesión del usuario y redirige a la pantalla de inicio de sesión (LoginActivity).
                // Aquí debes agregar la lógica para cerrar la sesión.
                // Por ejemplo, puedes eliminar los datos de sesión y redirigir al usuario a la pantalla de inicio de sesión.
                // Líneas de ejemplo para redirigir a LoginActivity (reemplaza con tu lógica real):
                val logoutIntent = Intent(this, MainActivity::class.java)
                logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(logoutIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}

