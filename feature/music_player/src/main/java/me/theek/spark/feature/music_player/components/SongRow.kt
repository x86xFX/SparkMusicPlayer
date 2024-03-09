package me.theek.spark.feature.music_player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.theek.spark.core.model.data.Song
import me.theek.spark.feature.music_player.R

@Composable
internal fun SongRow(
    songWithIndex: Pair<Int, Song>,
    songRetriever: suspend (String) -> ByteArray?,
    onSongClick: (Pair<Int, Song>) -> Unit,
    modifier: Modifier = Modifier
) {
    var image by remember { mutableStateOf<ByteArray?>(null) }

    LaunchedEffect(key1 = songWithIndex) {
        image = songRetriever(songWithIndex.second.path)
    }

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
                .clickable { onSongClick(songWithIndex) },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            AsyncImage(
                model = image,
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
                    text = songWithIndex.second.songName ?: "Unknown",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = songWithIndex.second.artistName ?: "Unknown",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            SongOptionMenu(modifier = Modifier.weight(1f))
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(fraction = 0.9f),
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun SongOptionMenu(modifier: Modifier = Modifier) {
    var shouldExpandDropDownMenu by remember { mutableStateOf(false) }

    Box(modifier = Modifier.padding(end = 5.dp)) {
        IconButton(
            modifier = modifier,
            onClick = { shouldExpandDropDownMenu = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = stringResource(R.string.song_option_icon),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        DropdownMenu(
            expanded = shouldExpandDropDownMenu,
            onDismissRequest = { shouldExpandDropDownMenu = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Add",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = "Share",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        text = "Edit",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = null
                    )
                },
                onClick = { /*TODO*/ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SongRowPreview() {
    SongRow(
        songWithIndex = Pair(
            1,
            Song(
                id = 0,
                path = "",
                artistName = "The Weenknd",
                duration = 300,
                songName = "Save Your Tears",
                albumId = 2,
                trackNumber = 8,
                releaseYear = 2020,
                genres = emptyList(),
                mimeType = null,
                lastModified = 0L,
                size = 2,
                externalId = null
            )
        ),
        songRetriever = { null },
        onSongClick = {}
    )
}