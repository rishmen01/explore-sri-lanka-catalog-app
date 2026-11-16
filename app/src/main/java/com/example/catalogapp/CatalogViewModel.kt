package com.example.catalogapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CatalogViewModel : ViewModel() {

    private val _items = MutableLiveData(ItemList.getItems())
    val items: LiveData<List<Item>> = _items

    private val _searchQuery = MutableLiveData("")
    private val _selectedCategory = MutableLiveData<String?>(null)
    private val _showFavouritesOnly = MutableLiveData(false)

    val searchedItems = MediatorLiveData<List<Item>>()
    val favouriteItems = MediatorLiveData<List<Item>>()

    init {
        val updateCatalogue: () -> Unit = {
            searchedItems.value = filterItems(
                source = _items.value.orEmpty(),
                query = _searchQuery.value,
                category = _selectedCategory.value,
                favouritesOnly = _showFavouritesOnly.value == true
            )
        }
        val updateFavourites: () -> Unit = {
            val query = _searchQuery.value.orEmpty().trim()
            favouriteItems.value = _items.value.orEmpty().filter { item ->
                item.isFavorite && matchesQuery(item, query)
            }
        }

        searchedItems.addSource(_items) { updateCatalogue() }
        searchedItems.addSource(_searchQuery) { updateCatalogue() }
        searchedItems.addSource(_selectedCategory) { updateCatalogue() }
        searchedItems.addSource(_showFavouritesOnly) { updateCatalogue() }

        favouriteItems.addSource(_items) { updateFavourites() }
        favouriteItems.addSource(_searchQuery) { updateFavourites() }
    }

    fun setSearchQuery(query: String) {
        if (_searchQuery.value != query) _searchQuery.value = query
    }

    fun getSearchQuery(): LiveData<String> = _searchQuery

    fun setSelectedCategory(category: String?) {
        if (_selectedCategory.value != category) _selectedCategory.value = category
    }

    fun getSelectedCategory(): LiveData<String?> = _selectedCategory

    fun setShowFavouritesOnly(enabled: Boolean) {
        if (_showFavouritesOnly.value != enabled) _showFavouritesOnly.value = enabled
    }

    fun getShowFavouritesOnly(): LiveData<Boolean> = _showFavouritesOnly

    fun toggleFavoriteStatus(itemToToggle: Item) {
        val currentList = _items.value ?: return
        _items.value = currentList.map { item ->
            if (item.title == itemToToggle.title) {
                item.copy(isFavorite = !item.isFavorite)
            } else {
                item
            }
        }
    }

    private fun filterItems(
        source: List<Item>,
        query: String?,
        category: String?,
        favouritesOnly: Boolean
    ): List<Item> {
        val normalizedQuery = query.orEmpty().trim()
        val normalizedCategory = category?.trim().orEmpty()
        return source.filter { item ->
            matchesQuery(item, normalizedQuery) &&
                (normalizedCategory.isBlank() || item.categories.any {
                    it.equals(normalizedCategory, ignoreCase = true)
                }) &&
                (!favouritesOnly || item.isFavorite)
        }
    }

    private fun matchesQuery(item: Item, query: String): Boolean =
        query.isBlank() || item.title.contains(query, ignoreCase = true) ||
            item.description.contains(query, ignoreCase = true)
}
