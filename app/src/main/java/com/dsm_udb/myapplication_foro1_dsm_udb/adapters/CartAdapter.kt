// CartAdapter.kt
package com.dsm_udb.myapplication_foro1_dsm_udb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dsm_udb.myapplication_foro1_dsm_udb.models.CartItem
import com.dsm_udb.myapplication_foro1_dsm_udb.R

class CartAdapter(private val cartItemList: MutableList<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.cartProductImage)
        val productName: TextView = itemView.findViewById(R.id.cartProductName)
        val productPrice: TextView = itemView.findViewById(R.id.cartProductPrice)
        val removeFromCartButton: Button = itemView.findViewById(R.id.removeFromCartButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItemList[position]

        holder.productImage.setImageResource(cartItem.imageResourceId)
        holder.productName.text = cartItem.name
        holder.productPrice.text = "Precio: $${cartItem.price}"

        holder.removeFromCartButton.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return cartItemList.size
    }

    // Agrega una función para eliminar un producto del carrito
    fun removeItem(position: Int) {
        if (position in 0 until cartItemList.size) {
            // Eliminar el producto del carrito
            val removedCartItem = cartItemList[position]
            cartItemList.removeAt(position)
            notifyItemRemoved(position)

            // Notificar a la actividad para actualizar la vista del total de compra
            listener?.onItemRemoved(removedCartItem)
        }
    }

    private var listener: OnCartItemRemovedListener? = null

    fun setOnCartItemRemovedListener(listener: OnCartItemRemovedListener) {
        this.listener = listener
    }

    interface OnCartItemRemovedListener {
        fun onItemRemoved(removedCartItem: CartItem)
    }
}



