package me.theek.spark.feature.music_player.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.core.model.data.ArtistDetails
import me.theek.spark.feature.music_player.R

@Composable
internal fun ArtistsComposable(
    artistDetailsStream: Map<String, ArtistDetails>,
    onArtistClick: (ArtistDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(artistDetailsStream.keys.toList()) { artistName ->
            ArtistItem(
                artistDetails = artistDetailsStream.getValue(artistName),
                onArtistClick = onArtistClick
            )
        }
    }
}

@Composable
internal fun ArtistItem(
    artistDetails: ArtistDetails,
    onArtistClick: (ArtistDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onArtistClick(artistDetails) }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1.5f),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .aspectRatio(1f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artistDetails)
                    .memoryCacheKey(artistDetails.artistName)
                    .diskCacheKey(artistDetails.artistName)
                    .build(),
                placeholder = painterResource(id = R.drawable.artist_placeholder),
                error = painterResource(id = R.drawable.artist_placeholder),
                contentDescription = stringResource(R.string.playlist_image),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .weight(8.5f)
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = artistDetails.artistName,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Text(
                text = "Tracks ${artistDetails.songs.size}",
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        }
    }
}

@Preview
@Composable
private fun ArtistsComposablePreview() {
    ArtistItem(
        artistDetails = ArtistDetails("The Weeknd", emptyList()),
        onArtistClick = {}
    )
}