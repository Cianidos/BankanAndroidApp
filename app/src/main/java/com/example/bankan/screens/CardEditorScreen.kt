package com.example.bankan.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.bankan.common.ui.components.CreateNewButton
import com.example.bankan.data.models.CardInfo
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
    Surface(modifier = modifier) {
        Column {
            CreateNewButton(
                isEntering = false,
                name = "",
                onCreateNew = { /*TODO*/ },
                onNameChanged = {}
            ) {
            }
            TextField(value = card.name, onValueChange = { card = card.copy(name = it) })
            TextField(
                value = card.description,
                onValueChange = { card = card.copy(description = it) })

            IconButton(onClick = { resultNav.navigateBack(Json.encodeToString(card)) }) {
                Icon(Icons.Outlined.Check, contentDescription = null)
            }
        }
    }

}
