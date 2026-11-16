package com.example.catalogapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DetailFragment : Fragment() {

    private var item: Item? = null
    private val viewModel: CatalogViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { item = it.getParcelable(ARG_ITEM) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailImage: ImageView = view.findViewById(R.id.detail_image)
        val detailTitle: TextView = view.findViewById(R.id.detail_title)
        val detailDescription: TextView = view.findViewById(R.id.detail_description)
        val closeButton: ImageButton = view.findViewById(R.id.close_button)
        val favouriteButton: ImageButton = view.findViewById(R.id.favourite_button)
        val moreInfoBtn: Button = view.findViewById(R.id.btn_more_info) // <-- plain Button

        // Populate the selected item. The full-size image is used only on this screen.
        item?.let { itItem ->
            detailImage.setImageResource(itItem.image)
            detailImage.contentDescription = itItem.title
            detailTitle.text = itItem.title
            favouriteButton.contentDescription = "Toggle favourite for ${itItem.title}"
            detailDescription.text = itItem.description

            moreInfoBtn.setOnClickListener {
                val url = itItem.externalLink
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(requireContext(), "No app can open the link.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            val current = items.find { it.title == item?.title }
            current?.let {
                val icon = if (it.isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
                favouriteButton.setImageResource(icon)
            }
        }

        closeButton.setOnClickListener { parentFragmentManager.popBackStack() }
        favouriteButton.setOnClickListener { item?.let { viewModel.toggleFavoriteStatus(it) } }
    }

    companion object {
        private const val ARG_ITEM = "item_arg"
        fun newInstance(item: Item) = DetailFragment().apply {
            arguments = bundleOf(ARG_ITEM to item)
        }
    }
}
