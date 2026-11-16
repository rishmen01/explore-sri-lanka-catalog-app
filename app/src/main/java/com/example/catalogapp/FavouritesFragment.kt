package com.example.catalogapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class FavouritesFragment : Fragment() {

    private val viewModel: CatalogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_favourites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.favourites_recycler_view)
        val emptyMessage: TextView = view.findViewById(R.id.empty_favourites_message)
        val spanCount = resources.getInteger(R.integer.grid_span_count).coerceAtLeast(1)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount).apply {
            initialPrefetchItemCount = spanCount * 2
        }
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(12)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        val adapter = CatalogAdapter(
            onItemClicked = { item ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(item))
                    .addToBackStack("favourite-details")
                    .commit()
            },
            onItemLongPressed = { item -> viewModel.toggleFavoriteStatus(item) }
        )
        recyclerView.adapter = adapter

        viewModel.favouriteItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)
            emptyMessage.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
            recyclerView.visibility = if (items.isEmpty()) View.GONE else View.VISIBLE
        }
    }
}
