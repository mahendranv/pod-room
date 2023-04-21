package com.github.mahendranv.podroom.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mahendranv.podroom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

abstract class GenericListFragment<T> : Fragment(), GenericListAdapter.OnItemClickListener<T>,
    IPopupHandler<T>, IStringifier<T> {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View

    protected val genericAdapter: GenericListAdapter<T> = GenericListAdapter(this, this, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.generic_listing_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolBar)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        toolbar.title = getTitle()

        recyclerView = view.findViewById(R.id.generic_recycler_view)
        emptyView = view.findViewById(R.id.empty_view)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = genericAdapter
    }

    protected fun fetchItems(flow: Flow<List<T>>) {
        lifecycleScope.launch {
            flow.flowOn(Dispatchers.Main)
                .flowWithLifecycle(lifecycle)
                .collect {
                    updateItems(it)
                }
        }
    }

    protected fun updateItems(item: List<T>) {
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

    abstract fun getTitle(): CharSequence
}