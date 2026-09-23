package com.shaaya.kmpdailypluse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shaaya.kmpdailypluse.Platform

@Composable
fun AboutScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            AboutScreenToolbar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        AboutScreenContent(paddingValues)
    }
}

fun prepareSystemData(): List<Pair<String, String>> {
    val platform = Platform()
    return listOf(
        Pair("Operating System", "${platform.osName}, ${platform.osVersion}"),
        Pair("Device", platform.deviceModel),
        Pair("Density", platform.density.toString())
    )
}

@Composable
fun AboutScreenContent(paddingValues: PaddingValues) {
    val systemDataItems = remember {
        prepareSystemData()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = systemDataItems) { row ->
            RowView(row)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreenToolbar(
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text("About") },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        }
    )
}

@Composable
fun RowView(row: Pair<String, String>) {
    Column {
        Text(
            text = row.first,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = row.second,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
