package me.theek.spark.feature.music_player.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.theek.spark.feature.music_player.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SparkPlayerTopAppBar(onSearch: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Spark Player")
        },
        actions = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_icon)
                )
            }
        }
    )
}