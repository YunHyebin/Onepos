package com.scspro.onepos.data.repository

import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Product, Category SQL 인터페이스
 */
interface OneposRepository {
    /*카테고리&상품 추가 및 삭제*/
    suspend fun addCategory(category: Category)
    suspend fun removeCategory(id: Int)
    suspend fun addProduct(product: Product)
    suspend fun removeProduct(id: Int)

    /*카테고리 전체 관찰*/
    fun observeCategories(): Flow<List<Category>>
    /*특정 카테고리의 상품 리스트 관찰*/
    fun observeProductsInCategory(categoryId: Int) : Flow<List<Product>>
    //데이터가 변경될 때마다 새로운 카테고리 목록을 자동으로 방출하는 Cold Stream
}