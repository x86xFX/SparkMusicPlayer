package me.theek.spark.core.content_reader

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.Audio

interface ContentResolverHelper {
    fun getAudioData(): Flow<List<Audio>>
}