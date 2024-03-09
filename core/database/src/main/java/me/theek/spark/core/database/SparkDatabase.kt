package me.theek.spark.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.theek.spark.core.database.dao.PlaylistDao
import me.theek.spark.core.database.dao.SongDao
import me.theek.spark.core.database.dao.SongInPlaylistDao
import me.theek.spark.core.database.entity.PlaylistEntity
import me.theek.spark.core.database.entity.SongEntity
import me.theek.spark.core.database.entity.SongInPlaylistEntity

@Database(
    entities = [SongEntity::class, PlaylistEntity::class, SongInPlaylistEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SparkDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
    abstract fun songInPlaylistDao() : SongInPlaylistDao
    abstract fun playlistDao() : PlaylistDao
}