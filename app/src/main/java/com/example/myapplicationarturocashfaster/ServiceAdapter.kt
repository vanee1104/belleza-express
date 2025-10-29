package com.example.myapplicationarturocashfaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ServiceAdapter(
    private val services: List<Service>,
    private val onItemClick: (Service) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: androidx.cardview.widget.CardView = itemView.findViewById(R.id.serviceCard)
        val imageView: ImageView = itemView.findViewById(R.id.service_image)
        val nameTextView: TextView = itemView.findViewById(R.id.service_name)
        val descriptionTextView: TextView = itemView.findViewById(R.id.service_description)
        val priceTextView: TextView = itemView.findViewById(R.id.service_price)

        // Animation properties
        var isAnimating = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = services[position]

        // OPTIMIZACIÓN: Manejo seguro de imágenes
        if (service.imageResId != 0) {
            holder.imageView.setImageResource(service.imageResId)
        } else {
            // Imagen por defecto si no hay recurso o es 0
            holder.imageView.setImageResource(android.R.drawable.ic_dialog_info)
        }

        holder.nameTextView.text = service.name
        holder.descriptionTextView.text = service.description
        holder.priceTextView.text = "$${service.price}"

        // OPTIMIZACIÓN: Animación simplificada y más rápida
        holder.cardView.setOnClickListener {
            if (!holder.isAnimating) {
                holder.isAnimating = true

                // Scale animation optimizada
                holder.cardView.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(50) // Reducido de 100ms a 50ms
                    .withEndAction {
                        holder.cardView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(50) // Reducido de 100ms a 50ms
                            .withEndAction {
                                holder.isAnimating = false
                                onItemClick(service)
                            }
                            .start()
                    }
                    .start()
            }
        }

        // Long press listener for additional options
        holder.cardView.setOnLongClickListener {
            // Show context menu or additional options
            // You can implement a dialog with more actions
            true
        }
    }

    override fun getItemCount(): Int = services.size

    // Utility method to update data if needed
    fun updateServices(newServices: List<Service>) {
        // Note: For this to work, you'd need to make services mutable
        // This is a template for future enhancement
    }
}