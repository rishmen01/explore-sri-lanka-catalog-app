package com.example.catalogapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CatalogAdapter(
    private val onItemClicked: (Item) -> Unit,
    private val onItemLongPressed: (Item) -> Unit
) : ListAdapter<Item, CatalogAdapter.ViewHolder>(ItemDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.item_title)
        val descriptionTextView: TextView = view.findViewById(R.id.item_description)
        val imageView: ImageView = view.findViewById(R.id.item_image)
        val favoriteIcon: ImageView = view.findViewById(R.id.favorite_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.imageView.setImageResource(item.thumbnail)
        holder.imageView.contentDescription = item.title

        if (item.isFavorite) {
            holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_filled)
            holder.favoriteIcon.visibility = View.VISIBLE
            holder.favoriteIcon.contentDescription = "Favourite"
        } else {
            holder.favoriteIcon.visibility = View.GONE
            holder.favoriteIcon.contentDescription = null
        }

        holder.itemView.setOnClickListener { onItemClicked(item) }
        holder.itemView.setOnLongClickListener {
            onItemLongPressed(item)
            true
        }
    }

    private companion object {
        val ItemDiffCallback = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }
}
