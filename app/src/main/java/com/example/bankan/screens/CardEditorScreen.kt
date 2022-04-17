package com.example.bankan.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.CardTag
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Destination(style = DestinationStyle.Dialog.Default::class)
@Composable
fun CardEditorScreen(
    modifier: Modifier = Modifier,
    resultNav: ResultBackNavigator<String>,
    initialCardInfo: CardInfo
) {
    var card by remember { mutableStateOf(initialCardInfo.copy()) }
    var isEntering by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize { initialValue, targetValue -> }) {
        Column {
            LazyRow(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                eachAndBetween(
                    data = card.tags.withIndex().toList(), spacerWidth = 5.dp
                ) { (idx, tag) ->
                    Button(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .defaultMinSize(1.dp, 1.dp)
                            .height(40.dp)
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = tag.color)
                    ) {
                        TextField(
                            value = tag.name,
                            modifier = Modifier.widthIn(min = 10.dp, max = 100.dp),
                            singleLine = true,
                            onValueChange = {
                                card = card.copy(tags = card.tags.apply {
                                    toMutableList()[idx] = CardTag(it, tag.color)
                                })
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(10.dp))
                }
                item {
                    CreateNewButton(
                        isEntering = isEntering,
                        name = newTagName,
                        onCreateNew = { newTagName = ""; isEntering = true },
                        onNameChanged = { newTagName = it },
                        onSubmit = {
                            isEntering = false
                            card = card.copy(
                                tags = card.tags + CardTag(
                                    newTagName,
                                    Color.Green
                                )
                            )
                        }
                    )
                }
            }

            TextField(
                value = card.name,
                onValueChange = { card = card.copy(name = it) }
            )

            TextField(
                value = card.description,
                onValueChange = { card = card.copy(description = it) }
            )

            Row(horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { resultNav.navigateBack(Json.encodeToString(card)) }) {
                    Icon(Icons.Outlined.Check, contentDescription = null)
                }
            }
        }
    }

}
