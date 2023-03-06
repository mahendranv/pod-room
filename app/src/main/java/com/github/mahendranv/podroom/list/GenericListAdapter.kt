package com.github.mahendranv.podroom.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.mahendranv.podroom.R

class GenericListAdapter(
    private val clickListener: OnItemClickListener?,
    private val longClickListener: OnItemLongClickListener?
) : RecyclerView.Adapter<GenericItemHolder>() {

    private var itemList: List<Any> = emptyList()

    fun setItems(_items: List<Any>) {
        itemList = _items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return GenericItemHolder(view)
    }

    override fun onBindViewHolder(holder: GenericItemHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text = PodItemStrigifier.stringify(item)
        holder.textView.setOnClickListener { clickListener?.onItemClick(item) }
        holder.textView.setOnLongClickListener {
            longClickListener?.onItemLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(item: Any?): Boolean
    }
}