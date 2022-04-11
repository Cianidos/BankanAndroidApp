package com.example.bankan.screens.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.ListData

@Composable
fun List1(data: ListData, onAddNewCard: (Int) -> Unit) {
    BankanTheme {
        LazyColumn(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Text(
                    text = data.info.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(20.dp))
                        .fillMaxWidth()
                )
            }
            data.content.forEach { (name, description) ->
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (name.isNotEmpty())
                        Card(name = name, description = description)
                    else
                        NameLessCard(description)
                }
            }
            item {
                AddNewCard(onAddNewCard = { onAddNewCard(data.info.localId) })
            }
        }
    }
}

@Composable
fun AddNewList(modifier: Modifier = Modifier, onAddNewList: () -> Unit) {
    BankanTheme {
        Surface(
            modifier = modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
        ) {
            DashOutline(Modifier.fillMaxSize(), cornersSize = RoundedCornerShape(20.dp).topStart) {
                IconButton(onClick = onAddNewList) {
                    Icon(Icons.Outlined.Add, "Add new list button")
                }
            }
        }
    }
}

