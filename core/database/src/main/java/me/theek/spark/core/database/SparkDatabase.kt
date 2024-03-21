package me.theek.spark.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.theek.spark.core.database.dao.ArtistDao
import me.theek.spark.core.database.dao.PlaylistDao
import me.theek.spark.core.database.dao.SongDao
import me.theek.spark.core.database.dao.SongInPlaylistDao
import me.theek.spark.core.database.entity.ArtistProfileEntity
import me.theek.spark.core.database.entity.PlaylistEntity
import me.theek.spark.core.database.entity.PlaylistWithSongsEntity
import me.theek.spark.core.database.entity.SongEntity

@Database(
    entities = [SongEntity::class, PlaylistEntity::class, PlaylistWithSongsEntity::class, ArtistProfileEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SparkDatabase : RoomDatabase() {
    abstract fun songDao() : SongDao
    abstract fun songInPlaylistDao() : SongInPlaylistDao
    abstract fun playlistDao() : PlaylistDao
    abstract fun artistDao() : ArtistDao
}