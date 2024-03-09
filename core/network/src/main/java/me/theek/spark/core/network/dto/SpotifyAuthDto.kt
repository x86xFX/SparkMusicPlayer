package me.theek.spark.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyAuthDto(
    val clientId: String,
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
    val isAnonymous: Boolean
)