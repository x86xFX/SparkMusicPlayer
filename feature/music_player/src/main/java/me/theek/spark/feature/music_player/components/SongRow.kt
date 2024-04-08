package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.util.UiState

@Composable
internal fun SongRow(
    index: Int,
    song: Song,
    playlistState: UiState<List<PlaylistData>>,
    onSongClick: (Int) -> Unit,
    onCreatePlaylistClick: (Song) -> Unit,
    onAddToExistingPlaylistClick: (Pair<Long, Long>) -> Unit,
    onSongInfoClick: (Song) -> Unit,
    onShareClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { onSongClick(index) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(10.dp))
//            val coverUri = Uri.parse("content://media/external/audio/media/${song.externalId}/albumart")
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song)
                    .memoryCacheKey(song.path)
//                    .memoryCacheKey(coverUri.path)
//                    .diskCacheKey(coverUri.path)
                    .build(),
                contentDescription = stringResource(R.string.album_art),
                error = painterResource(id = R.drawable.round_music_note_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .weight(7f)
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = song.songName ?: "Unknown",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = song.artistName ?: "Unknown",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            SongOptionMenu(
                modifier = Modifier.weight(1f),
                song = song,
                playlistsState = playlistState,
                onCreatePlaylistClick = { onCreatePlaylistClick(song) },
                onAddToExistingPlaylistClick = onAddToExistingPlaylistClick,
                onSongInfoClick = onSongInfoClick,
                onShareClick = onShareClick
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SongRowPreview() {
    SongRow(
        index = 1,
        song = Song(
            id = 0,
            path = "",
            artistName = "The Weenknd",
            duration = 300,
            songName = "Save Your Tears",
            albumId = 2,
            albumName = "After Hours",
            trackNumber = 8,
            releaseYear = 2020,
            genres = emptyList(),
            mimeType = null,
            lastModified = 0L,
            size = 2,
            isFavourite = false,
            externalId = null
        ),
        playlistState = UiState.Loading,
        onSongClick = {},
        onCreatePlaylistClick = {},
        onSongInfoClick = {},
        onShareClick = {},
        onAddToExistingPlaylistClick = {}
    )
}