package com.example.bankan.screens.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.ListData
import com.example.bankan.data.models.ListInfo

@Preview(showSystemUi = true)
@Composable
fun ListPreview() {
    val data = ListData(
        ListInfo(name = "List list"),
        listOf(
            CardInfo(
                name = "SomeName1",
                description = "Some Long Description bla bla bla, ha ha ha"
            ),
            CardInfo(name = "", description = "Some Long Description"),
            CardInfo(name = "SomeName3", description = "Some Long Description")
        )
    )
    LazyRow(modifier = Modifier) {
        item { List1(data) {} }
        item { Spacer(modifier = Modifier.width(10.dp)) }
        item { List2(data) }
    }
}

@Composable
fun List1(data: ListData, onAddNewCard: (Int) -> Unit) {
    BankanTheme {
        LazyColumn(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(10.dp)
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

@Composable
fun List2(data: ListData) {
    BankanTheme {
        LazyColumn(
            modifier = Modifier
                .wrapContentHeight()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp)
                .background(Color.DarkGray, RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = data.info.name,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(20.dp))
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
        }
    }
}
