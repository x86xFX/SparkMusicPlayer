package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.feature.music_player.R
import me.theek.spark.feature.music_player.util.byteToMB
import me.theek.spark.feature.music_player.util.timeStampToDuration
import me.theek.spark.feature.music_player.viewmodels.SongInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSongInfoBottomBar(
    sheetState: SheetState,
    songInfo: SongInfo,
    onDismissRequest: () -> Unit
) {
    if (songInfo.shouldShowSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(songInfo.song)
                            .memoryCacheKey(songInfo.song?.path)
                            .diskCacheKey(songInfo.song?.path)
                            .build(),
                        contentDescription = stringResource(R.string.album_art),
                        error = painterResource(id = R.drawable.round_music_note_24),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth(fraction = 0.9f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.title)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.songName ?: stringResource(id = R.string.unknown),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.artist)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.artistName ?: stringResource(id = R.string.unknown),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.duration)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = timeStampToDuration(songInfo.song?.duration?.toFloat() ?: 0f),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.mime_type)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.mimeType ?: stringResource(id = R.string.unknown),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.release_year)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.releaseYear?.toString() ?: stringResource(id = R.string.unknown),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.location)
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = songInfo.song?.path ?: stringResource(id = R.string.unknown),
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(fraction = 0.35f),
                                text = stringResource(R.string.size)
                            )

                            val bytesToMB = byteToMB(byte = songInfo.song?.size)

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = if (bytesToMB != null) "${bytesToMB}MB" else "~",
                                textAlign = TextAlign.Start,
                                maxLines = 5,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}