package com.github.mahendranv.podroom.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mahendranv.podroom.R

class GenericItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView: TextView

    init {
        textView = itemView.findViewById(R.id.item_text_view)
    }
}