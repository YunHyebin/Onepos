package com.scspro.onepos.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
/**
 * Category 테이블 설계 클래스
*/
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "categoryId")    val id: Int = 0,
    @ColumnInfo(name = "categoryName")  val name: String

)