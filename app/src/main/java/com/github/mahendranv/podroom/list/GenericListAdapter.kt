package com.github.mahendranv.podroom.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.github.mahendranv.podroom.R

class GenericListAdapter(
    private val clickListener: OnItemClickListener?,
    private val popupHandler: IPopupHandler?
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
        holder.textView.text = PodItemStringifier.stringify(item)
        holder.textView.setOnClickListener { clickListener?.onItemClick(item) }
        if (popupHandler != null) {
            holder.itemView.setOnLongClickListener { anchor ->
                showPopup(anchor, item)
                true
            }
        }
    }

    private fun showPopup(anchorView: View, item: Any) {
        val itemList = popupHandler?.getActions() ?: return
        val popup = ListPopupWindow(anchorView.context)
        val adapter =
            ArrayAdapter(anchorView.context, android.R.layout.simple_list_item_1, itemList)
        popup.anchorView = anchorView
        popup.setAdapter(adapter)
        popup.setOnItemClickListener { _, _, position, _ ->
            popupHandler.onActionClicked(position, item)
            popup.dismiss()
        }
        popup.show()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: Any?)
    }
}