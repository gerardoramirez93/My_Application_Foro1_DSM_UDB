package com.dsm_udb.myapplication_foro1_dsm_udb.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dsm_udb.myapplication_foro1_dsm_udb.models.Product
import com.dsm_udb.myapplication_foro1_dsm_udb.R

class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Declaración de la interfaz para los listeners
    interface OnItemClickListener {
        fun onItemClick(product: Product, imageResourceId: Int)
        fun onAddToCartClick(product: Product, imageResourceId: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        val imageName = product.image // Nombre de la imagen sin la extensión
        val imageId = getDrawableResourceId(holder.itemView.context, imageName)

        holder.productImage.setImageResource(imageId)
        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = "Precio: $${product.price}"

        // Llamando a los listeners cuando se hace clic
        holder.itemView.setOnClickListener {
            listener?.onItemClick(product, imageId)
        }

        holder.addToCartButton.setOnClickListener {
            listener?.onAddToCartClick(product, imageId)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productDescription: TextView = itemView.findViewById(R.id.productDescription)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)
    }

    private fun getDrawableResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}