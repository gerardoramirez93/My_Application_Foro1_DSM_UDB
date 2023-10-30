package com.dsm_udb.myapplication_foro1_dsm_udb.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dsm_udb.myapplication_foro1_dsm_udb.models.Product
import com.dsm_udb.myapplication_foro1_dsm_udb.models.CartItem
import com.dsm_udb.myapplication_foro1_dsm_udb.R

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyStoreDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    val CREATE_TABLE_USERS = """
        CREATE TABLE User (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Username TEXT,
            Password TEXT,
            FirstName TEXT,
            LastName TEXT
        );
    """

    val CREATE_TABLE_PRODUCTS = """
        CREATE TABLE Product (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            Name TEXT,
            Description TEXT,
            Price REAL,
            Image TEXT
        );
    """

    val CREATE_TABLE_CART = """
        CREATE TABLE Cart (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            UserID INTEGER,
            ProductID INTEGER,
            FOREIGN KEY (UserID) REFERENCES User(ID),
            FOREIGN KEY (ProductID) REFERENCES Product(ID)
        );
    """

    val CREATE_TABLE_PURCHASE_HISTORY = """
        CREATE TABLE PurchaseHistory (
            ID INTEGER PRIMARY KEY AUTOINCREMENT,
            UserID INTEGER,
            ProductID INTEGER,
            PurchaseDate TEXT,
            FOREIGN KEY (UserID) REFERENCES User(ID),
            FOREIGN KEY (ProductID) REFERENCES Product(ID)
        );
    """

    override fun onCreate(db: SQLiteDatabase) {
        // Crea las tablas en la base de datos al principio.
        db.execSQL(CREATE_TABLE_USERS)
        db.execSQL(CREATE_TABLE_PRODUCTS)
        db.execSQL(CREATE_TABLE_CART)
        db.execSQL(CREATE_TABLE_PURCHASE_HISTORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Puedes implementar la lógica para actualizar la base de datos si es necesario.
        // En esta implementación, se recrean las tablas en caso de actualización.
        db.execSQL("DROP TABLE IF EXISTS User;")
        db.execSQL("DROP TABLE IF EXISTS Product;")
        db.execSQL("DROP TABLE IF EXISTS Cart;")
        db.execSQL("DROP TABLE IF EXISTS PurchaseHistory;")
        onCreate(db)
    }

    fun isValidUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM User WHERE Username = ? AND Password = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }

    fun getProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val query = "SELECT * FROM Product"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                val price = cursor.getDouble(cursor.getColumnIndex("Price"))
                val image = cursor.getString(cursor.getColumnIndex("Image"))

                val product = Product(id, name, description, price, image)
                productList.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return productList
    }

    fun getCartItems(context: Context): List<CartItem> {
        val cartItemList = mutableListOf<CartItem>()
        val db = readableDatabase
        val query = "SELECT * FROM Cart"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val productId = cursor.getInt(cursor.getColumnIndex("ProductID"))
                val product = getProductById(productId)

                if (product != null) {
                    val imageResourceId = context.resources.getIdentifier(product.image, "drawable", context.packageName)
                    val cartItem = CartItem(
                        product,
                        productId,  // Asegúrate de tener un identificador único para el elemento del carrito
                        product.name, // Puedes obtener el nombre del producto desde 'product'
                        product.price, // Obtén el precio desde 'product'
                        imageResourceId // Aquí obtienes el ID de la imagen del producto
                    )
                    cartItemList.add(cartItem)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return cartItemList
    }


    fun getProductById(productId: Int): Product? {
        val db = readableDatabase
        val query = "SELECT * FROM Product WHERE ID = ?"
        val cursor = db.rawQuery(query, arrayOf(productId.toString()))

        var product: Product? = null

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex("ID"))
            val name = cursor.getString(cursor.getColumnIndex("Name"))
            val description = cursor.getString(cursor.getColumnIndex("Description"))
            val price = cursor.getDouble(cursor.getColumnIndex("Price"))
            val image = cursor.getString(cursor.getColumnIndex("Image"))

            product = Product(id, name, description, price, image)
        }

        cursor.close()
        db.close()

        return product
    }
    fun addToCart(product: Product): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        // Comprueba si el producto ya está en el carrito
        if (isProductInCart(db, product.id)) {
            db.close()
            return false
        }

        values.put("UserID", 1) // Aquí debes establecer el ID del usuario actual (1 en este ejemplo).
        values.put("ProductID", product.id)

        val rowId = db.insert("Cart", null, values)
        db.close()

        return rowId != -1L
    }

    private fun isProductInCart(db: SQLiteDatabase, productId: Int): Boolean {
        val query = "SELECT * FROM Cart WHERE UserID = 1 AND ProductID = ?"  // Ajusta el ID de usuario.
        val cursor = db.rawQuery(query, arrayOf(productId.toString()))
        val isProductInCart = cursor.count > 0
        cursor.close()
        return isProductInCart
    }

    fun removeFromCart(productId: Int): Boolean {
        val db = writableDatabase
        val whereClause = "ProductID = ?"
        val whereArgs = arrayOf(productId.toString())

        val deletedRows = db.delete("Cart", whereClause, whereArgs)
        db.close()

        return deletedRows > 0
    }
    fun removeFromCart(product: Product): Boolean {
        val db = writableDatabase
        val whereClause = "UserID = 1 AND ProductID = ?"
        val whereArgs = arrayOf(product.id.toString())

        val deletedRows = db.delete("Cart", whereClause, whereArgs)
        db.close()

        return deletedRows > 0
    }

    fun clearCart() {
        val db = writableDatabase
        db.execSQL("DELETE FROM Cart WHERE UserID = 1") // Ajusta el ID del usuario.
        db.close()
    }
}
