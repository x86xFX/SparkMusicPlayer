package me.theek.spark.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import me.theek.spark.core.database.entity.SongEntity

@Database(
    entities = [SongEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SparkDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
}