package com.example.bankan.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.common.ui.components.DashOutline
import com.example.bankan.common.ui.eachAndBetween
import com.example.bankan.data.models.CardInfo
import com.example.bankan.data.models.CardTag
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalAnimationApi::class)
object CardEditorScreenStyle : DestinationStyle.Animated {
}

fun <S, T> List<S>.cartesianProduct(other: List<T>): List<Pair<S, T>> =
    flatMap { s -> List(other.size) { s }.zip(other) }

@OptIn(ExperimentalMaterialApi::class)
@Destination(style = CardEditorScreenStyle::class)
@Composable
fun CardEditorScreen(
    modifier: Modifier = Modifier,
    resultNav: ResultBackNavigator<String>,
    initialCardInfo: CardInfo
) {

    val focusManager = LocalFocusManager.current

    var card by remember { mutableStateOf(initialCardInfo.copy()) }

    val tagList: SnapshotStateList<CardTag> = remember { card.tags.toMutableStateList() }

    var isEntering by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }

    var currentColorIndex by remember { mutableStateOf(0) }

    var currentSelectedTag by remember { mutableStateOf<Int?>(null) }

    val colorsList = listOf(
        Color.Red,
        Color.Magenta,
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color.Cyan,
//        Color.White,
//        Color.Black
    ).let { it.cartesianProduct(it) }.map { (f, s) ->
        Color(
            (f.red + s.red) / 2,
            (f.green + s.green) / 2,
            (f.blue + f.blue) / 2
        )
    }.toSet().flatMap { c ->
        List(4) {
            val t = it + 1
            Color(
                c.red.plus(c.red / t) / 2,
                c.green.plus(c.green / t) / 2,
                c.blue.plus(c.blue / t) / 2
            )
        }
    }.toSet().toList()

    Surface(
        onClick = { isEntering = false; currentSelectedTag = null; newTagName = ""; focusManager.clearFocus() },
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
                                    .height(30.dp)
                                    .wrapContentWidth(),
                                color = tag.color
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    BasicTextField(
                                        value = tag.name,
                                        modifier = Modifier
                                            .width(IntrinsicSize.Min)
                                            .onFocusEvent {
                                                if (it.isFocused) {
                                                    currentColorIndex =
                                                        colorsList.indexOf(tag.color)
                                                    currentSelectedTag = idx
                                                }
                                            },
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
                                modifier = Modifier.width(IntrinsicSize.Min),
                                isEntering = isEntering,
                                name = newTagName,
                                onCreateNew = { newTagName = ""; isEntering = true },
                                onNameChanged = { newTagName = it },
                                onSubmit = {
                                    isEntering = false
                                    tagList += CardTag(
                                        newTagName,
                                        colorsList[currentColorIndex]
                                    )
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
                item {
                    FlowRow {
                        colorsList.forEachIndexed { idx, color ->
                            if (idx == currentColorIndex) {
                                DashOutline {
                                    Surface(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(5.dp),
                                        shape = CircleShape,
                                        color = color
                                    ) { }
                                }
                            } else {
                                Surface(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(5.dp),
                                    shape = CircleShape,
                                    color = color,
                                    onClick = {
                                        currentColorIndex = idx
                                        currentSelectedTag?.let {
                                            tagList[it] =
                                                tagList[it].copy(color = colorsList[currentColorIndex])
                                        }
                                    }
                                ) { }
                            }
                        }
                    }
                }
            }
        }
    }
}
