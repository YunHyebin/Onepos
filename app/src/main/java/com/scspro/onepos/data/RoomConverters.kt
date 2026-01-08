package com.scspro.onepos.data

import android.util.Log
import androidx.room.TypeConverter
import com.scspro.onepos.data.entity.OptionItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    // ==== List<OptionItem> -> JSON 변환============================================================
    // OptionEntity의 options 컬럼용 DB 저장을 위한 OptionItem 리스트 -> JSON 문자열 직렬화
    @TypeConverter
    fun convertOptionItemsToJson(value: List<OptionItem>?): String? {
        //OptionItems 리스트 -> Json 문자열로 직렬화
        return value?.let { json.encodeToString(it) }
    }

    // ==== JSON -> List<OptionItem> 변환===========================================================
    // DB 조회를 위한 JSON 문자열 -> OptionItem 리스트 역직렬화
    @TypeConverter
    fun convertJsonToOptionItems(value: String?): List<OptionItem>? {
        //JSON 문자열 -> OptionItems 리스트 역직렬화
        return value?.let { json.decodeFromString(it) }
    }

    // ==== List<Int> -> JSON 변환 ==================================================================
    @TypeConverter
    fun convertIntListToJson(value: List<Int>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    // ==== JSON ->List<Int> 변환 ==================================================================
    @TypeConverter
    fun convertJsonToIntList(value: String?): List<Int>? {
        return value?.let { json.decodeFromString(it) }
    }
}
