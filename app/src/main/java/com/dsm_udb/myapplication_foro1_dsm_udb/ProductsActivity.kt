package com.dsm_udb.myapplication_foro1_dsm_udb

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsm_udb.myapplication_foro1_dsm_udb.adapters.ProductAdapter
import com.dsm_udb.myapplication_foro1_dsm_udb.database.DatabaseHelper
import com.dsm_udb.myapplication_foro1_dsm_udb.models.Product
import android.view.MenuItem
import android.view.Menu
import android.content.Intent
import android.util.Log



class ProductsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var databaseHelper: DatabaseHelper // Instancia de la base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        supportActionBar?.title = "Lista de Productos"

        // Inicializar la instancia de la base de datos
        databaseHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtén la lista de productos desde la base de datos
        val productList = getProductsFromDatabase()

        productAdapter = ProductAdapter(productList)
        recyclerView.adapter = productAdapter

        productAdapter.setOnItemClickListener(object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product, imageResourceId: Int) {
                // Aquí puedes definir qué hacer cuando se hace clic en un producto.
                Toast.makeText(this@ProductsActivity, "Producto seleccionado: ${product.name}", Toast.LENGTH_SHORT).show()
            }

            override fun onAddToCartClick(product: Product, imageResourceId: Int) {
                // Aquí puedes definir qué hacer cuando se agrega un producto al carrito.
                addToCart(product)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_products -> {
                Log.d("ProductsActivity", "PRODUCTOS seleccionado")
                // Acción para el ítem "PRODUCTOS"
                // Redirige a la misma actividad (ProductsActivity).
                // No se necesita hacer nada aquí, ya estamos en la actividad de productos.
                return true
            }
            R.id.menu_cart -> {
                Log.d("ProductsActivity", "CARRITO seleccionado")
                // Acción para el ítem "CARRITO"
                // Dirige a la pantalla del carrito (CartActivity).
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menu_logout -> {
                Log.d("ProductsActivity", "CERRAR SESIÓN seleccionado")
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


    private fun addToCart(product: Product) {
        // Agrega el producto al carrito en la base de datos.
        val addedToCart = databaseHelper.addToCart(product)

        if (addedToCart) {
            Toast.makeText(this, "Producto agregado al carrito: ${product.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "El producto ya está en el carrito", Toast.LENGTH_SHORT).show()
        }
    }


    // Método para obtener la lista de productos desde la base de datos
    private fun getProductsFromDatabase(): List<Product> {
        // Aquí deberías realizar una consulta a la base de datos utilizando tu DatabaseHelper.
        // Por ejemplo, puedes llamar a una función que consulte la base de datos y devuelva una lista de productos.

        // Reemplaza esta línea con tu lógica de consulta a la base de datos
        return databaseHelper.getProducts() // Asume que tienes una función en DatabaseHelper para obtener productos.
    }
}