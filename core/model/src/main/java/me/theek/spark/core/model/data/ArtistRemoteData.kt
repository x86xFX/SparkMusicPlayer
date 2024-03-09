package me.theek.spark.core.model.data

data class ArtistRemoteData(
    val artists: Artists
) {
    data class Artists(
        val href: String,
        val items: List<ArtistData>,
        val limit: Int,
        val next: String?,
        val offset: Int,
        val previous: String?,
        val total: Int
    ) {
        data class ArtistData(
            val externalUrls: ExternalUrls,
            val followers: Followers,
            val genres: List<String>,
            val href: String,
            val id: String,
            val images: List<ArtistImage>,
            val name: String,
            val popularity: Int,
            val type: String,
            val uri: String
        ) {
            data class ExternalUrls(
                val spotify: String
            )
            data class Followers(
                val href: String?,
                val total: Long
            )
            data class ArtistImage(
                val height: Int,
                val url: String,
                val width: Int,
            )
        }
    }
}
