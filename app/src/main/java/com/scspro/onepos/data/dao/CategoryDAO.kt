package com.scspro.onepos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.scspro.onepos.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
/**
 * DB에 Category SQL 쿼리 날리는 인터페이스
*/
@Dao
interface CategoryDAO {

    @Query("SELECT * FROM categories WHERE categoryId = :id")
    suspend fun getById(id: Int): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(category: CategoryEntity)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE categoryId = :id")
    suspend fun removeById(id: Int)

//    @Query("SELECT * FROM CategoryEntity")
//    fun getAllCategories(): LiveData<List<CategoryEntity>>

    //Flow 반환하면 Room이 자동으로 백그라운드로 관찰/쿼리 실행
    @Query("SELECT * FROM categories")
    fun observeAll(): Flow<List<CategoryEntity>>

    //Category & 그 하위 Products 함께 관찰 (Flow)
    @Transaction
    @Query("SELECT * FROM categories")
    fun observeCategoriesWithProducts(): Flow<List<CategoryWithProducts>>
}