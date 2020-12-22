package com.mindinventory.autoscrollviewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mindinventory.CircularAdapter
import kotlinx.android.synthetic.main.item_viewpager.view.*

class MyAdapter() : CircularAdapter<Int>() {

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        )
    }

    override fun bindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        item: Int,
        actualPosition: Int,
        position: Int
    ) {
        holder.itemView.ivThumb.setImageResource(item)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}