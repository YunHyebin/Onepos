package com.scspro.onepos.data

import com.scspro.onepos.data.entity.CategoryEntity
import com.scspro.onepos.data.entity.ProductEntity
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product

/**
 * Entity(Room DB) <-> Domain Model (Ui/비즈니스 로직) 간 변환
 */
fun Category.toEntity(): CategoryEntity = CategoryEntity(id = id, name = name)
fun Product.toEntity(): ProductEntity = ProductEntity(id = id, name = name, price = price, imageRes = imageRes, categoryId = categoryId, optionId = optionId)

fun CategoryEntity.toModel(): Category = Category(id = id, name = name)
fun ProductEntity.toModel(): Product = Product(id = id, name = name, price = price, imageRes = imageRes, categoryId = categoryId, optionId = optionId)
