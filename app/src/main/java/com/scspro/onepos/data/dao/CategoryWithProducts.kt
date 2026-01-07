package com.scspro.onepos.data.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.scspro.onepos.data.entity.CategoryEntity
import com.scspro.onepos.data.entity.ProductEntity

data class CategoryWithProducts(
    @Embedded val category: CategoryEntity, //Category 부모필드를 객체로 포함
    @Relation(
        parentColumn = "categoryId",    //부모의 pk
        entityColumn = "categoryId"     //자식의 FK
    )
    val products: List<ProductEntity>
)
