package me.theek.spark.feature.music_player.components.draggable_player

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.theek.spark.core.design_system.components.BasicPlaybackControls
import me.theek.spark.core.design_system.icons.rememberPause
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableSongRow(
    isPlaying: Boolean,
    currentSelectedSong: Song,
    bodyTextColor: Color,
    titleTextColor: Color,
    onSkipPreviousClick: () -> Unit,
    onSkipNextClick: () -> Unit,
    onPausePlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                error = painterResource(id = R.drawable.round_music_note_24),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(currentSelectedSong)
                    .memoryCacheKey(currentSelectedSong.path)
                    .diskCacheKey(currentSelectedSong.path)
                    .build(),
                contentDescription = stringResource(R.string.current_playing_song_cover_art)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
                text = currentSelectedSong.songName ?: "Unknown",
                color = bodyTextColor,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = titleTextColor,
                text = currentSelectedSong.artistName ?: "Unknown",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.SemiBold,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }

        BasicPlaybackControls(
            onSkipNextClick = onSkipNextClick,
            onSkipPreviousClick = onSkipPreviousClick,
            skipPreviousIconSize = 28.dp,
            skipNextIconSize = 28.dp,
            tint = bodyTextColor,
            horizontalArrangement = Arrangement.End,
            playPauseIcon = {
                IconButton(
                    modifier = Modifier.size(28.dp),
                    onClick = onPausePlayClick
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = if (isPlaying) rememberPause() else Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(me.theek.spark.core.design_system.R.string.play_pause_icon),
                        tint = bodyTextColor
                    )
                }
            }
        )
    }
}