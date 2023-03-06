package com.github.mahendranv.podroom.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mahendranv.podroom.R

abstract class GenericListFragment : Fragment(), GenericListAdapter.OnItemClickListener,
    GenericListAdapter.OnItemLongClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View

    protected val genericAdapter: GenericListAdapter = GenericListAdapter(this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.generic_listing_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.generic_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView.adapter = genericAdapter
    }

    protected fun updateItems(item: List<Any>) {
        if (item.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            genericAdapter.setItems(item)
            genericAdapter.notifyDataSetChanged()
        }
    }
}