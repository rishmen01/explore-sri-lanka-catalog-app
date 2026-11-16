package com.example.catalogapp

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private val viewModel: CatalogViewModel by viewModels()
    private lateinit var favouritesButton: MaterialButton

    private val categories = listOf(
        "UNESCO", "History", "Religious", "Coastal", "Nature", "Safari",
        "Beach", "Surfing", "Hills", "Landmark", "Ruins"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        applySavedTheme()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        applySystemBarInsets()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ItemFragment())
                .commit()
        }

        val searchView: SearchView = findViewById(R.id.search_view)
        val filterButton: MaterialButton = findViewById(R.id.btn_filter)
        favouritesButton = findViewById(R.id.btn_favourites)
        val themeButton: MaterialButton = findViewById(R.id.btn_theme)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })

        viewModel.getSearchQuery().observe(this) { query ->
            if (searchView.query?.toString() != query) searchView.setQuery(query, false)
        }

        filterButton.setOnClickListener { showFilterDialog() }
        favouritesButton.setOnClickListener { toggleFavouritesScreen() }
        supportFragmentManager.addOnBackStackChangedListener { refreshFavouritesButton() }

        refreshThemeButtonText(themeButton)
        themeButton.setOnClickListener {
            toggleTheme()
            refreshThemeButtonText(themeButton)
        }
        refreshFavouritesButton()
    }


    private fun applySystemBarInsets() {
        val root: View = findViewById(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            windowInsets
        }
        ViewCompat.requestApplyInsets(root)
    }

    private fun toggleFavouritesScreen() {
        val current = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (current is FavouritesFragment) {
            supportFragmentManager.popBackStack()
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FavouritesFragment())
            .addToBackStack("favourites")
            .commit()
        favouritesButton.isEnabled = false
        favouritesButton.post { favouritesButton.isEnabled = true }
    }

    private fun refreshFavouritesButton() {
        if (!::favouritesButton.isInitialized) return
        val showingFavourites =
            supportFragmentManager.findFragmentById(R.id.fragment_container) is FavouritesFragment
        favouritesButton.text = if (showingFavourites) "All places" else "Favourites"
        favouritesButton.contentDescription =
            if (showingFavourites) "Return to all places" else "Open favourite places"
    }

    private fun applySavedTheme() {
        val night = getSharedPreferences("settings", MODE_PRIVATE)
            .getBoolean("night", false)
        AppCompatDelegate.setDefaultNightMode(
            if (night) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun isNightModeActive(): Boolean {
        val current = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return current == Configuration.UI_MODE_NIGHT_YES
    }

    private fun toggleTheme() {
        val newNight = !isNightModeActive()
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit()
            .putBoolean("night", newNight)
            .apply()
        AppCompatDelegate.setDefaultNightMode(
            if (newNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun refreshThemeButtonText(button: MaterialButton) {
        button.text = ""
        button.setIconResource(
            if (isNightModeActive()) R.drawable.ic_sun else R.drawable.ic_moon
        )
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_filters, null, false)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.rg_categories)
        val favouritesSwitch = dialogView.findViewById<SwitchMaterial>(R.id.switch_favourites)
        val idToLabel = mutableMapOf<Int, String?>()

        fun addRadio(label: String?, checked: Boolean) {
            val radioButton = RadioButton(this).apply {
                text = label ?: "All categories"
                id = View.generateViewId()
                isChecked = checked
            }
            radioGroup.addView(radioButton)
            idToLabel[radioButton.id] = label
        }

        val currentCategory = viewModel.getSelectedCategory().value
        addRadio(null, currentCategory.isNullOrBlank())
        categories.forEach { category ->
            addRadio(category, currentCategory?.equals(category, ignoreCase = true) == true)
        }
        favouritesSwitch.isChecked = viewModel.getShowFavouritesOnly().value == true

        MaterialAlertDialogBuilder(this)
            .setTitle("Filters")
            .setView(dialogView)
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Reset") { _, _ ->
                viewModel.setSelectedCategory(null)
                viewModel.setShowFavouritesOnly(false)
            }
            .setPositiveButton("Apply") { _, _ ->
                viewModel.setSelectedCategory(idToLabel[radioGroup.checkedRadioButtonId])
                viewModel.setShowFavouritesOnly(favouritesSwitch.isChecked)
            }
            .show()
    }
}
