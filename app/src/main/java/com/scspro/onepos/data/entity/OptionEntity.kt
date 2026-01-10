package com.scspro.onepos.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "options",
)
data class OptionEntity(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    @ColumnInfo(name = "optionId")          val id: Int = 1,
    @ColumnInfo(name = "optionName")        val name: String,
    @ColumnInfo(name = "isRequired")        val isRequired: Boolean,
    @ColumnInfo(name = "hasQuantity")       val hasQuantity: Boolean,
    @ColumnInfo(name = "optionItems")       val optionItems: List<OptionItem>
)

/**
 * 개별 옵션 상세 정보를 담는 데이터 클래스 (JsonObject 역할)
 * Json변환을 위해 직렬화 어노테이션 추가.
 * DB에 저장 시 직렬화하여 저장
 */

@Serializable
data class OptionItem(
    val optionItemName: String,
    val optionItemPrice: Long
)