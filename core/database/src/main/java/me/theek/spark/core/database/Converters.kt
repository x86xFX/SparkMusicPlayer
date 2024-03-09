package me.theek.spark.core.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun String.listFromString(): List<String> {
        return this.split(";").filter { it.isNotEmpty() }.map { it }
    }

    @TypeConverter
    fun List<String>.listToString(): String {
        return this.joinToString(separator = ";")
    }
}