package com.oe.saleclient.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oe.saleclient.R
import com.oe.saleclient.databinding.RecyclerviewItemBinding
import com.oe.saleclient.domain.model.Item
import com.oe.saleclient.ui.fragments.RecyclerViewClickListener

class ItemsAdapter(private val items: List<Item>, private val listener: RecyclerViewClickListener) : RecyclerView.Adapter<ItemsAdapter.MoviesViewHolder>() {

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.recyclerviewMovieBinding.item = items[position]
        holder.recyclerviewMovieBinding.root.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.root, position)
        }
    }

    inner class MoviesViewHolder(
        val recyclerviewMovieBinding: RecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)
}