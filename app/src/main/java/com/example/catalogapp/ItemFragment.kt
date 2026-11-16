package com.example.catalogapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class ItemFragment : Fragment() {

    private val viewModel: CatalogViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val spanCount = resources.getInteger(R.integer.grid_span_count).coerceAtLeast(1)
        val gridLayoutManager = GridLayoutManager(requireContext(), spanCount).apply {
            initialPrefetchItemCount = spanCount * 2
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(12)
        (recyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

        val adapter = CatalogAdapter(
            onItemClicked = { item ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DetailFragment.newInstance(item))
                    .addToBackStack("item-details")
                    .commit()
            },
            onItemLongPressed = { item -> viewModel.toggleFavoriteStatus(item) }
        )
        recyclerView.adapter = adapter

        viewModel.searchedItems.observe(viewLifecycleOwner, adapter::submitList)
    }
}
