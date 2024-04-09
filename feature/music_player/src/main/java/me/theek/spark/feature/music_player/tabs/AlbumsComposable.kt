package me.theek.spark.feature.music_player.tabs

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import me.theek.spark.core.model.data.Album
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.components.EmptyAlbumComposable
import me.theek.spark.feature.music_player.util.timeStampToDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AlbumsComposable(
    albums: List<Album>,
    onAlbumClick: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (albums.isEmpty()) {
        EmptyAlbumComposable(modifier = Modifier.fillMaxSize())
    } else {
        val lazyColumnState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        Box(modifier = modifier) {
            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)
                    )
            ) {
                itemsIndexed(
                    items = albums,
                    key = { index, _ -> index }
                ) { _, album ->
                    AlbumItem(
                        onAlbumClick = onAlbumClick,
                        album = album
                    )
                }
            }

            if (lazyColumnState.canScrollBackward) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 70.dp)
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.tertiaryContainer),
                        onClick = { scope.launch { lazyColumnState.animateScrollToItem(index = 0) } }
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(5.dp),
                            imageVector = Icons.Rounded.KeyboardArrowUp,
                            contentDescription = stringResource(R.string.to_up),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumItem(
    onAlbumClick: (Int?) -> Unit,
    album: Album,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onAlbumClick(album.albumId) }
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .aspectRatio(1f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse("content://media/external/audio/media/${album.songs[0].externalId}/albumart"))
                    .build(),
                placeholder = painterResource(id = R.drawable.artist_placeholder),
                error = painterResource(id = R.drawable.artist_placeholder),
                contentDescription = stringResource(R.string.album_image),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = album.albumName ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Clip,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Tracks ${album.songs.count()}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
                Text(
                    text = "Duration ${timeStampToDuration(album.songs.sumOf { it.duration?.toLong() ?: 0L }.toFloat())}",
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize
                )
            }
        }
    }
}