package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.logic.KnightPuzzleGame
import com.pinguapps.chesstrainer.ui.composables.Chessboard

@Composable
fun KnightScreen(
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    val resources = LocalContext.current.resources


    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Chessboard(KnightPuzzleGame())
    }
}