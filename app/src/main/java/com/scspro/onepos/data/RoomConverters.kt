package com.scspro.onepos.data

import android.util.Log
import androidx.room.TypeConverter
import com.scspro.onepos.data.entity.OptionItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    // DB 저장을 위한 OptionItem 리스트 -> JSON 문자열 직렬화
    @TypeConverter
    fun convertOptionItemsToJson(value: List<OptionItem>): String {
        if(value.isEmpty()) {
            throw IllegalArgumentException("OptionItems cannot be empty")
        }

        //OptionItems 리스트 -> Json 문자열로 직렬화
        Log.d("OPTION ITEMS", json.encodeToString(value))
        return json.encodeToString(value)
    }

    // DB 조회를 위한 JSON 문자열 -> OptionItem 리스트 역직렬화
    @TypeConverter
    fun convertJsonToOptionItems(value: String): List<OptionItem> {
        if(value.isEmpty()) {
            throw IllegalArgumentException("OptionItems cannot be empty")
        }
        
        //JSON 문자열 -> OptionItems 리스트 역직렬화
        Log.d("OPTION ITEMS", json.decodeFromString(value))
        return json.decodeFromString(value)
    }
}
