package com.seravian.feat_local.data.db.utils

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class ListStringConverters {
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return Json.decodeFromString(ListSerializer(String.serializer()), value)
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return Json.encodeToString(ListSerializer(String.serializer()), list)
    }
}