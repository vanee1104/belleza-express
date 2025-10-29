package com.example.myapplicationarturocashfaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(private val activities: List<ActivityItem>) :
    RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivActivityIcon)
        val title: TextView = itemView.findViewById(R.id.tvActivityTitle)
        val description: TextView = itemView.findViewById(R.id.tvActivityDescription)
        val time: TextView = itemView.findViewById(R.id.tvActivityTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]

        holder.icon.setImageResource(activity.icon)
        holder.title.text = activity.title
        holder.description.text = activity.description
        holder.time.text = activity.time

        // Color basado en el tipo de actividad
        // Color basado en el tipo de actividad
        val color = when (activity.type) {
            ActivityType.BOOKING -> R.color.purple_500
            ActivityType.PROFILE -> R.color.teal_700
            ActivityType.SYSTEM -> R.color.black
            ActivityType.PAYMENT -> R.color.green_700
        }

        holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.context, color))
    }

    override fun getItemCount() = activities.size
}