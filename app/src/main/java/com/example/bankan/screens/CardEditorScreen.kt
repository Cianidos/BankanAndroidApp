package com.example.bankan.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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

@OptIn(ExperimentalAnimationApi::class)
object CardEditorScreenStyle : DestinationStyle.Animated {
}

@Destination(style = CardEditorScreenStyle::class)
@Composable
fun CardEditorScreen(
    modifier: Modifier = Modifier,
    resultNav: ResultBackNavigator<String>,
    initialCardInfo: CardInfo
) {
    var card by remember { mutableStateOf(initialCardInfo.copy()) }
    val tagList: SnapshotStateList<CardTag> = remember { card.tags.toMutableStateList() }

    var isEntering by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .animateContentSize { initialValue, targetValue -> },
            ) {
                item {
                    LazyRow(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        eachAndBetween(
                            data = tagList.withIndex().toList(), spacerWidth = 5.dp
                        ) { (idx, tag) ->
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .defaultMinSize(1.dp, 1.dp)
                                    .height(40.dp)
                                    .wrapContentWidth(),
                                color = tag.color
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    BasicTextField(
                                        value = tag.name,
                                        modifier = Modifier.widthIn(min = 10.dp, max = 100.dp),
                                        singleLine = true,
                                        onValueChange = {
                                            tagList[idx] = tagList[idx].copy(name = it)
                                        }
                                    )
                                }
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

                                    tagList += CardTag(newTagName, Color.Green)
                                }
                            )
                        }
                    }
                }

                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize { initialValue, targetValue -> },
                        value = card.name,
                        onValueChange = { card = card.copy(name = it) }
                    )
                }

                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize { initialValue, targetValue -> },
                        value = card.description,
                        onValueChange = { card = card.copy(description = it) }
                    )
                }

                item {
                    Row(
                        modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            modifier = Modifier.heightIn(min = 20.dp),
                            onClick = { resultNav.navigateBack(Json.encodeToString(card.copy(tags = tagList.toList()))) }) {
                            Icon(Icons.Outlined.Check, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
