package com.example.bankan.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bankan.common.ui.theme.BankanTheme

@Preview(showSystemUi = true)
@Composable
fun ListPreview() {
    //Row (/*modifier = Modifier.width(500.dp)*/) {
//        List1(
//            listOf(
//                "SomeName1" to "Some Long Description bla bla bla, ha ha ha",
//                "SomeName2" to "Some Long Description",
//                "SomeName3" to "Some Long Description"
//            )
//        )
//        Spacer(modifier = Modifier.width(10.dp))
    List2(
        listOf(
            "SomeName1" to "Some Long Description bla bla bla, ha ha ha",
            "SomeName2" to "Some Long Description",
            "SomeName3" to "Some Long Description"
        )
    )
    //}
}

@Composable
fun List1(cards: List<Pair<String, String>>) {
    BankanTheme {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(280.dp)
                .height((cards.size * 180).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "List1",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .background(Color.Gray, RoundedCornerShape(20.dp))
                    .fillMaxWidth()
            )
            cards.forEach { (name, description) ->
                Spacer(modifier = Modifier.height(10.dp))
                CardPreview2(name, description)
            }
        }
    }
}

@Composable
fun List2(cards: List<Pair<String, String>>) {
    BankanTheme {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .height((cards.size * 180).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "List1",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .background(Color.Gray, RoundedCornerShape(20.dp))
            )
            cards.forEach { (name, description) ->
                //Spacer(modifier = Modifier.height(10.dp))
                CardPreview1(name, description)
            }
        }
    }
}
