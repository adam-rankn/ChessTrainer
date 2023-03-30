package com.pinguapps.chesstrainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val resources = LocalContext.current.resources

    Column (
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(onClick = {
            navController.navigate("Pawn")
        },modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Pawn Puzzle",
            fontSize = 24.sp)
        }

        Button(onClick = {
            navController.navigate("Knight")
        },modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Knight Puzzle",
                fontSize = 24.sp)
        }

        Button(onClick = {
            navController.navigate("Chess")
        },modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Play Chess",
                fontSize = 24.sp)
        }

        Button(onClick = {
            navController.navigate("Pawn")
        },modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Study Openings",
                fontSize = 24.sp)
        }
    }
}