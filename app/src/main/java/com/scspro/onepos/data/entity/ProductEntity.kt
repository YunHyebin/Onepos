package com.scspro.onepos.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
/**
 * Product 테이블 설계 클래스
 */
@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,     //참조할 테이블: CategoryEntity
            parentColumns = ["categoryId"],     //참조할 테이블의 PK 컬럼
            childColumns = ["categoryId"],      //products테이블이 참조할 컬럼
            onDelete = ForeignKey.CASCADE       //categories 컬럼 삭제 시 products도 삭제
        ) // OptionEntity 외래키 제거 (1:N 관계이므로 제거함)
    ],
    indices = [Index(value = ["categoryId"])]   //외래키 컬럼 인덱스 권장
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "productId")     val id: Int = 0,
    @ColumnInfo(name = "productName")   val name: String,
    @ColumnInfo(name = "productPrice")  val price: Long,
    @ColumnInfo(name = "productImg")    val imageRes: Int? = null,
    @ColumnInfo(name = "categoryId")    val categoryId: Int,            //categoryEntity.id 를 참조하는 FK
    @ColumnInfo(name = "optionId")      val optionId: List<Int>? = null //optionEntity.id를 참조하는 FK (TypeConverter가 변환)
    )