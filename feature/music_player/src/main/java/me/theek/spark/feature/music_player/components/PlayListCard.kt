package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.theek.spark.core.model.data.Playlist
import me.theek.spark.feature.music_player.R

@Composable
internal fun PlayListCard(
    playlist: Playlist,
    onPlaylistViewClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .width(100.dp)
                .height(150.dp)
                .clickable { onPlaylistViewClick(playlist) }
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = 0f,
                                    endY = size.height
                                ),
                                blendMode = BlendMode.Multiply
                            )
                        }
                    },
                model = "https://i1.sndcdn.com/avatars-000544239675-37shxr-t500x500.jpg", // Ariana Grande Blue Color image
                contentDescription = stringResource(R.string.playlist_image),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = playlist.name,
                    color = Color.White,
                    maxLines = 1,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayListCardPreview() {
    PlayListCard(
        playlist = Playlist(0,"Favourites", 0),
        onPlaylistViewClick = {}
    )
}