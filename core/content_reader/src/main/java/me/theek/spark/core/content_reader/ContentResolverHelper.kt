package me.theek.spark.core.content_reader

import kotlinx.coroutines.flow.Flow
import me.theek.spark.core.model.data.Song

interface ContentResolverHelper {
    fun getAudioData(): SongFlow
}

typealias SongFlow = Flow<FlowEvent<List<Song>, RetrieveProgress>>