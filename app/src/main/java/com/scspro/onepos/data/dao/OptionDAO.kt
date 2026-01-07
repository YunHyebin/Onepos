package com.scspro.onepos.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.scspro.onepos.data.entity.OptionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Option(상품 옵션) SQL Query 인터페이스
 * RoomConverters (Json converter)가 자동으로 JSON String<->OptionItem 리스트로 변환함
 */
@Dao
interface OptionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(option: OptionEntity)

    @Query("DELETE FROM options WHERE optionId = :id")
    suspend fun removeById(id: Int)

    @Update
    suspend fun update(option: OptionEntity)

    @Query("SELECT * FROM options WHERE optionId = :id")
    suspend fun getById(id: Int): OptionEntity?

    @Query("SELECT * FROM options ORDER BY optionId DESC")
    fun observeAll(): Flow<List<OptionEntity>>


}