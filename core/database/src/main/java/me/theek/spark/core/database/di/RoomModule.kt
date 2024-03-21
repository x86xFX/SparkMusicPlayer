package me.theek.spark.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.theek.spark.core.database.SparkDatabase
import me.theek.spark.core.database.dao.ArtistDao
import me.theek.spark.core.database.dao.PlaylistDao
import me.theek.spark.core.database.dao.SongDao
import me.theek.spark.core.database.dao.SongInPlaylistDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideSparkDatabase(@ApplicationContext context: Context) : SparkDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = SparkDatabase::class.java,
            name = "spark.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSongDao(sparkDatabase: SparkDatabase) : SongDao {
        return sparkDatabase.songDao()
    }

    @Provides
    @Singleton
    fun provideSongInPlaylistDao(sparkDatabase: SparkDatabase) : SongInPlaylistDao {
        return sparkDatabase.songInPlaylistDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(sparkDatabase: SparkDatabase) : PlaylistDao {
        return sparkDatabase.playlistDao()
    }

    @Provides
    @Singleton
    fun provideArtistDao(sparkDatabase: SparkDatabase) : ArtistDao {
        return sparkDatabase.artistDao()
    }
}