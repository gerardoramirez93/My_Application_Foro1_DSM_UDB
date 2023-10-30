package com.dsm_udb.myapplication_foro1_dsm_udb.models

data class CartItem(
    val product: Product, // Producto asociado al elemento del carrito
    val id: Int, // Identificador único del producto en el carrito
    val name: String, // Nombre del producto
    val price: Double, // Precio del producto
    val imageResourceId: Int // ID de recursos de imagen del producto
)