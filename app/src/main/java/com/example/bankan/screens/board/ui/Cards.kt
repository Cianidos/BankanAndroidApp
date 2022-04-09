package com.example.bankan.screens.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.theme.BankanTheme

@Preview
@Composable
fun CardExample() {
    Column {
        NameLessCard(
            description = "Some longer text. It  must be longer? and cause of this i need to write it. I dont know why but it doesn't work without meaningfull text"
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            name = "Some Simple name",
            description = "Some longer text. It  must be longer? and cause of this i need to write it. I dont know why but it doesn't work without meaningfull text"
        )
    }
}

@Composable
fun NameLessCard(description: String) {
    BankanTheme {
        val tagText = "#jjjj"
        Column(
            modifier = Modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .width(300.dp)
                .aspectRatio(3.0f)
        ) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Text(text = tagText, modifier = Modifier, overflow = TextOverflow.Visible)
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        "edit",
                        modifier = Modifier,
                        tint = Color.DarkGray
                    )
                }
            }
            ClickableText(
                text = buildAnnotatedString { append(description) },
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(5.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            ) {}
        }
    }
}

@Composable
fun Card(name: String, description: String) {
    BankanTheme {
        val tagText = "#jjjj"
        Column(
            modifier = Modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .width(250.dp)
                .aspectRatio(1.6f)
                .sizeIn(maxHeight = (250 * 1.6f).dp, maxWidth = 250.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Text(text = tagText, modifier = Modifier, overflow = TextOverflow.Visible)
                }
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1.0f)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Outlined.Settings,
                        "edit",
                        modifier = Modifier,
                        tint = Color.DarkGray
                    )
                }
            }
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(8.dp, 0.dp)
            )
            ClickableText(
                text = buildAnnotatedString { append(description) },
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(5.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            ) {}
        }
    }
}

@Composable
fun AddNewCard(modifier: Modifier = Modifier, onAddNewCard: () -> Unit) {
    BankanTheme {
        Surface(
            modifier = modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .width(250.dp)
                .aspectRatio(1.6f)
        ) {
            DashOutline(modifier = Modifier.fillMaxSize(), cornersSize = RoundedCornerShape(20.dp).topStart) {
                IconButton(onClick = onAddNewCard) {
                    Icon(Icons.Outlined.Add, "Add new card button")
                }
            }
        }
    }
}
