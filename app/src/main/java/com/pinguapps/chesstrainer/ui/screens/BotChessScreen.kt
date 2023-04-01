package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.ui.ChessboardViewModel
import com.pinguapps.chesstrainer.ui.composables.ChessControlsBar
import com.pinguapps.chesstrainer.ui.composables.Chessboard

@Composable
fun BotChessScreen(
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    val resources = LocalContext.current.resources
    val chessboardViewModel = ChessboardViewModel()


    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Chessboard(chessboardViewModel.chessgame)
        ChessControlsBar(chessboardViewModel = chessboardViewModel)
    }
}