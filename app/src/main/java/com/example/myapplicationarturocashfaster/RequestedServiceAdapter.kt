package com.example.myapplicationarturocashfaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class RequestedServiceAdapter(
    private val requestedServices: List<RequestedService>
) : RecyclerView.Adapter<RequestedServiceAdapter.RequestedServiceViewHolder>() {

    class RequestedServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.requestedServiceCard)
        val nameTextView: TextView = itemView.findViewById(R.id.requestedServiceName)
        val statusTextView: TextView = itemView.findViewById(R.id.requestedServiceStatus)
        val dateTextView: TextView = itemView.findViewById(R.id.requestedServiceDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestedServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_requested_service, parent, false)
        return RequestedServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestedServiceViewHolder, position: Int) {
        val service = requestedServices[position]

        holder.nameTextView.text = service.serviceName
        holder.statusTextView.text = service.status
        holder.dateTextView.text = "Requested: ${service.requestDate}"
    }

    override fun getItemCount(): Int = requestedServices.size
}