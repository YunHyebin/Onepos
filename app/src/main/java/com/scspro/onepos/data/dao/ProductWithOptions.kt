package com.scspro.onepos.data.dao

import androidx.room.Embedded
import androidx.room.Relation
import com.scspro.onepos.data.entity.OptionEntity
import com.scspro.onepos.data.entity.ProductEntity

// 상품 정보를 가져올 때 해당 상품의 옵션 리스트도 함께 조회
data class ProductWithOptions(
    @Embedded val product: ProductEntity,   //Product 부모필드를 객체로 포함
    @Relation(
        parentColumn = "productId",         //부모의 pk
        entityColumn = "optionId"           //자식의 pk
    )
    val options: List<OptionEntity>

)
