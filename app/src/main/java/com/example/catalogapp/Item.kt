package com.example.catalogapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val title: String,
    val description: String,
    val categories: List<String>,
    val image: Int,
    val externalLink: String,
    val thumbnail: Int = image,
    val isFavorite: Boolean = false
) : Parcelable
