package com.scspro.onepos.model

data class Product (
    val id: Int,
    val name: String,
    val price: Long,
    val imageRes: Int? = null,
    val categoryId: Int,
    val optionId: Int? = null
)