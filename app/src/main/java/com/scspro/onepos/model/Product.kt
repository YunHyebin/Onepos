package com.scspro.onepos.model

data class Product (
    val id: Int = 0,
    val name: String,
    val price: Long,
    val imageRes: Int? = null,
    val categoryId: Int,
    val optionId: List<Int>? = null
)