package com.scspro.onepos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.scspro.onepos.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
/**
 * Product SQL Query 인터페이스
*/
@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Query("DELETE FROM products WHERE productId = :id")
    suspend fun removeById(id: Int)

    @Query("SELECT * FROM products WHERE productId = :id")
    suspend fun getById(id: Int): ProductEntity?

    //특정 카테고리의 상품들을 스트림으로 관찰 (UI에서 collectAsState로 바인딩)
    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    fun observeByCategory(categoryId: Int): Flow<List<ProductEntity>>
}