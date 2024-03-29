package com.example.bankan.screens.board.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.common.ui.theme.BankanTheme
import com.example.bankan.common.ui.theme.LightColorPalette
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.ListInfo
import com.example.bankan.screens.board.viewmodel.BoardScreenViewModel
import com.example.bankan.screens.destinations.CardEditorScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.getViewModel


@Composable
fun Card(
    modifier: Modifier = Modifier,
    nav: DestinationsNavigator,
    cardInfo: CardInfo
) {
    val vm = getViewModel<BoardScreenViewModel>()

    LocalRecipient.current.onNavResult {
        when (it) {
            is NavResult.Canceled -> Unit
            is NavResult.Value -> {
                Log.d("NNNNNNNNN", "Accepted value ${it.value}")
                vm.updateCard(Json.decodeFromString(it.value))
            }
        }
    }

    val name: String = cardInfo.name
    val description: String = cardInfo.description

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp))
            .width(250.dp)
            .wrapContentHeight(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            LazyRow(
                modifier = Modifier
                    .wrapContentSize()
                    .widthIn(max = 210.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.horizontalGradient(
                                0.95f to Color.Transparent,
                                1f to LightColorPalette.surface,
                            ),
                            topLeft = Offset(190.dp.toPx(), 0.dp.toPx()),
                            size = Size(25.dp.toPx(), 30.dp.toPx()),
                        )
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                eachAndBetween(
                    data = cardInfo.tags, spacerWidth = 5.dp
                ) { tag ->
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.defaultMinSize(1.dp, 1.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = tag.color)
                    ) {
                        Text(
                            text = tag.name,
                            modifier = Modifier,
                            overflow = TextOverflow.Visible,
                            color = Color.Black
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(10.dp))
                }
            }

            IconButton(
                onClick = { nav.navigate(CardEditorScreenDestination(cardInfo)) },
                modifier = Modifier
                    .wrapContentSize()
                    .offset(x = 10.dp)
            ) {
                Icon(
                    Icons.Outlined.Settings,
                    "edit",
                    modifier = Modifier.defaultMinSize(1.dp, 1.dp),
                    tint = Color.DarkGray
                )
            }
        }
        AnimatedVisibility(visible = name.isNotEmpty()) {
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(8.dp, 0.dp),
                color = Color.Black
            )
        }
        AnimatedVisibility(visible = description.isNotEmpty()) {
            Text(
                text = buildAnnotatedString { append(description) },
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(5.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )
        }
    }
}

@Composable
fun AddNewCard(modifier: Modifier = Modifier, listInfo: ListInfo) {
    val vm: BoardScreenViewModel = getViewModel()
    val isEntering by vm.isEnteringNewCardName.collectAsState()
    val newCardName by vm.newCardName.collectAsState()
    val enteringListId by vm.currentListCardFocus.collectAsState()
    val isEnteringMe = isEntering && enteringListId == listInfo.localId
    val scope = rememberCoroutineScope()

    BankanTheme {
        CreateNewButton(
            modifier = modifier,
            isEntering = isEnteringMe,
            name = newCardName,
            onCreateNew = {
                vm.isEnteringNewCardName.value = true; vm.currentListCardFocus.value =
                listInfo.localId
            },
            onNameChanged = { vm.newCardName.value = it },
            onSubmit = {
                vm.addNewCard(CardInfo(name = newCardName, listId = listInfo.localId))
                vm.newCardName.value = ""
                scope.launch {
                    vm.isEnteringNewCardName.value = false
                    delay(300)
                    vm.isEnteringNewCardName.value = true
                }
            }
        )
    }
}

val Modifier.debugBorder get() = this.border(1.dp, color = Color.Blue)