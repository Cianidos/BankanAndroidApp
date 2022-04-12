package com.example.bankan.screens.board.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.ListInfo
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import org.koin.androidx.compose.viewModel


@Composable
fun NameLessCard(
    modifier: Modifier = Modifier,
    description: String
) {
    BankanTheme {
        val tagText = "#jjjj"
        Column(
            modifier = modifier
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
fun Card(modifier: Modifier = Modifier, name: String, description: String) {
    BankanTheme {
        val tagText = "#jjjj"
        Column(
            modifier = modifier
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .width(250.dp)
                .aspectRatio(1.6f)
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
fun AddNewCard(modifier: Modifier = Modifier, listInfo: ListInfo) {
    val vm: BoardScreenViewModel by viewModel()
    val isEntering by vm.isEnteringNewCardName.collectAsState()
    val newCardName by vm.newCardName.collectAsState()
    val enteringListId by vm.currentListCardFocus.collectAsState()
    val isEnteringMe = isEntering && enteringListId == listInfo.localId

    BankanTheme {
        CreateNewButton(
            modifier = modifier,
            isEntering = isEnteringMe,
            name = newCardName,
            onCreateNew = { vm.isEnteringNewCardName.value = true; vm.currentListCardFocus.value = listInfo.localId},
            onNameChanged = { vm.newCardName.value = it },
            onSubmit = {
                vm.addNewCard(CardInfo(name = newCardName, listId = listInfo.localId))
                vm.newCardName.value = ""
                vm.isEnteringNewCardName.value = true
            }
        )
    }
}
