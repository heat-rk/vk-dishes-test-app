package com.nlmk.libs.vkdishestestapp.domain.models

data class Dish(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
)
