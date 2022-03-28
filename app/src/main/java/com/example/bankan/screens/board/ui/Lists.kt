package com.example.bankan.screens.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.screens.board.data.CardInfo
import com.example.bankan.screens.board.data.ListData
import com.example.bankan.screens.board.data.ListInfo

@Preview(showSystemUi = true)
@Composable
fun ListPreview() {
    val data = ListData(
        ListInfo("List list"),
        listOf(
            CardInfo("SomeName1", "Some Long Description bla bla bla, ha ha ha"),
            CardInfo("", "Some Long Description"),
            CardInfo("SomeName3", "Some Long Description")
        )
    )
    Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        List1(data)
        Spacer(modifier = Modifier.width(10.dp))
        List2(data)
    }
}

@Composable
fun List1(data: ListData) {
    BankanTheme {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = data.first.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .background(Color.Gray, RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            )
            data.second.forEach { (name, description) ->
                Spacer(modifier = Modifier.height(10.dp))
                if (name.isNotEmpty())
                    Card(name = name, description = description)
                else
                    NameLessCard(description)
            }
        }
    }
}

@Composable
fun List2(data: ListData) {
    BankanTheme {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp)
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = data.first.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .background(Color.Gray, RoundedCornerShape(20.dp))
            )
            data.second.forEach { (name, description) ->
                Spacer(modifier = Modifier.height(10.dp))
                if (name.isNotEmpty())
                    Card(name = name, description = description)
                else
                    NameLessCard(description)
            }
        }
    }
}