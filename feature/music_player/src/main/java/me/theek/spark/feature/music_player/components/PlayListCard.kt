package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.core.model.data.PlaylistData
import me.theek.spark.feature.music_player.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlayListCard(
    playlist: PlaylistData,
    isInSelectionMode: Boolean,
    onChangingToSelectionMode: (Long) -> Unit,
    onPlaylistAddToSelection: (Long) -> Unit,
    onPlaylistRemoveFromSelection: (Long) -> Unit,
    onPlaylistViewClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var isSelected by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isInSelectionMode) {
        if (!isInSelectionMode) isSelected = false
    }

    Card(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .width(100.dp)
                .height(150.dp)
                .border(
                    width = 2.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerLowest,
                    shape = CardDefaults.shape
                )
                .combinedClickable(
                    onLongClick = {
                        if (isInSelectionMode.not()) {
                            onChangingToSelectionMode(playlist.playlistId)
                            isSelected = true
                        }
                    },
                    onClick = {
                        if (isInSelectionMode) {
                            isSelected = if (isSelected) {
                                onPlaylistRemoveFromSelection(playlist.playlistId)
                                false
                            } else {
                                onPlaylistAddToSelection(playlist.playlistId)
                                true
                            }
                        } else {
                            onPlaylistViewClick(playlist.playlistId)
                        }
                    }
                )
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black,
                                    ),
                                    startY = 0f,
                                    endY = size.height
                                ),
                                blendMode = BlendMode.Multiply
                            )
                        }
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(playlist.songForCoverArt)
                    .memoryCacheKey(playlist.songForCoverArt.path)
                    .build(),
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
                    text = playlist.playlistName,
                    color = Color.White,
                    maxLines = 1,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}