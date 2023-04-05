package com.pinguapps.chesstrainer.ui.screens


import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.ui.composables.Chessboard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor

import androidx.compose.ui.text.style.TextAlign
import com.pinguapps.chesstrainer.data.LichessDbMove
import com.pinguapps.chesstrainer.ui.composables.ChessControlsBar
import kotlinx.coroutines.launch
import kotlin.math.max





@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OpeningScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {}
) {
    var visible = true
    var linesVisible  by remember(key1 = visible) { mutableStateOf(visible) }
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        //AutoCompleteOpeningBox(openings = allOpenings)
        Chessboard(openingViewModel.chessgame,
            onMoveMade = { start, end ->
                coroutineScope.launch {
                    openingViewModel.onHumanMoveMade(start,end)
                }
            })
        ChessControlsBar(
            onUndoPressed = { openingViewModel.undoMove()},
            onRedoPressed = { openingViewModel.redoMove()},
            onUndoAllPressed = { openingViewModel.undoAllMoves()},
            onRedoAllPressed = { openingViewModel.redoAllMoves()},
            onHintPressed = { visible = visible.not()},
        )
        if (linesVisible) {
            LinesBox(
                moves = openingViewModel.lichessLines.collectAsState().value,
            )
        }
    }
}

@Composable
fun LinesBox(moves: List<LichessDbMove>){
    if (moves.isNotEmpty()) {
        for (move in moves) {
            if (move.playedPercent > 0.1) {
                LineItem(move = move)
            }
        }
    }
    else {
        Text(text = "No games found for this position")
    }
}

@Composable
fun LineItem(move: LichessDbMove){

    Row(){
        val whitePercent = (move.whiteWinPercent*100).toInt()
        val drawPercent = (move.drawPercent*100).toInt()
        val blackPercent = (move.blackWinPercent*100).toInt()
        val playedPercent = (move.playedPercent*100).toInt()
        Text(text = "${move.san} $playedPercent%",
        modifier = Modifier.padding(end = 8.dp))
        Row(modifier = Modifier.border(BorderStroke(2.dp, SolidColor(Color.Black)))){

            Text(text = "$whitePercent%", textAlign = TextAlign.Center,
                color = Color.Black, modifier = Modifier
                    .weight(max(move.whiteWinPercent, 0.15f))
                    .background(Color.White))

            Text(text = "$drawPercent%", textAlign = TextAlign.Center,
                color = Color.White, modifier = Modifier
                    .weight(max(move.drawPercent, 0.15f))
                    .background(Color.Gray))

            Text(text = "$blackPercent%",textAlign = TextAlign.Center,
                color = Color.White, modifier = Modifier
                    .weight(max(move.blackWinPercent, 0.15f))
                    .background(Color.Black))
        }

    }
    
}



