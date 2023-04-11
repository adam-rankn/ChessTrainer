package com.pinguapps.chesstrainer.ui.screens


import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinguapps.chesstrainer.data.LichessDbMove
import com.pinguapps.chesstrainer.data.allOpenings
import com.pinguapps.chesstrainer.ui.composables.ChessControlsBar
import com.pinguapps.chesstrainer.ui.composables.Chessboard
import kotlinx.coroutines.launch
import kotlin.math.max


@Composable
fun OpeningScreen(
    modifier: Modifier = Modifier,
    onCancelButtonClicked: () -> Unit = {}
) {
    var linesVisible by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row {
                Chessboard(openingViewModel.chessgame,
                    onMoveMade = remember {
                        { start, end ->
                            coroutineScope.launch {
                                openingViewModel.onHumanMoveMade(start, end)
                            }
                        }
                    })
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {

                    AnimatedVisibility(
                        visible = (openingViewModel.humanMoveLichessStats.collectAsState().value.totalGames > 0),
                        enter = expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(initialAlpha = 0.3f),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        LastMoveStats(move = openingViewModel.humanMoveLichessStats.collectAsState().value)
                    }
                    AnimatedVisibility(
                        visible = linesVisible,
                        enter = expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(initialAlpha = 0.3f),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        LinesBox(
                            moves = openingViewModel.lichessLines.collectAsState().value
                        )
                    }
                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f))
                    ChessControlsBar(
                        context = context,
                        onUndoPressed = openingViewModel::undoMove,
                        onRedoPressed = openingViewModel::redoMove,
                        onUndoAllPressed = openingViewModel::undoAllMoves,
                        onRedoAllPressed = openingViewModel::redoAllMoves,
                        onHintPressed = { linesVisible = linesVisible.not() },
                    )
                }

            }
        }
        else -> {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Chessboard(openingViewModel.chessgame,
                    onMoveMade = remember {
                        { start, end ->
                            coroutineScope.launch {
                                openingViewModel.onHumanMoveMade(start, end)
                            }
                        }
                    })
                ChessControlsBar(
                    context = context,
                    onUndoPressed = openingViewModel::undoMove,
                    onRedoPressed = openingViewModel::redoMove,
                    onUndoAllPressed = openingViewModel::undoAllMoves,
                    onRedoAllPressed = openingViewModel::redoAllMoves,
                    onHintPressed = { linesVisible = linesVisible.not() },
                )
                if (openingViewModel.humanMoveLichessStats.collectAsState().value.totalGames > 0) {
                    LastMoveStats(move = openingViewModel.humanMoveLichessStats.collectAsState().value)
                }

                AnimatedVisibility(
                    visible = linesVisible,
                    enter = expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(initialAlpha = 0.3f),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    LinesBox(
                        moves = openingViewModel.lichessLines.collectAsState().value
                    )
                }
            }
        }
    }


}

@Composable
fun LinesBox(moves: List<LichessDbMove>) {
    val error = openingViewModel.lichessLinesErrorMessage.collectAsState().value
    if (error == "") {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            for (move in moves) {
                if (move.playedPercent > 0.005) { //todo prefs for % cutoff
                    LineItem(move = move)
                }
            }
        }
    } else {
        Text(text = error)
    }
}

@Composable
fun LineItem(move: LichessDbMove) {

    Row {
        val whitePercent = (move.whiteWinPercent * 100).toInt()
        val drawPercent = (move.drawPercent * 100).toInt()
        val blackPercent = (move.blackWinPercent * 100).toInt()
        val playedPercent = (move.playedPercent * 100).toInt()
        Text(
            text = "${move.san} $playedPercent%",
            modifier = Modifier.padding(end = 8.dp)
        )
        Row(modifier = Modifier.border(BorderStroke(2.dp, SolidColor(Color.Black)))) {

            Text(
                text = "$whitePercent%", textAlign = TextAlign.Center,
                color = Color.Black, modifier = Modifier
                    .weight(max(move.whiteWinPercent, 0.15f))
                    .background(Color.White)
            )

            Text(
                text = "$drawPercent%", textAlign = TextAlign.Center,
                color = Color.White, modifier = Modifier
                    .weight(max(move.drawPercent, 0.15f))
                    .background(Color.Gray)
            )

            Text(
                text = "$blackPercent%", textAlign = TextAlign.Center,
                color = Color.White, modifier = Modifier
                    .weight(max(move.blackWinPercent, 0.15f))
                    .background(Color.Black)
            )
        }
    }
}

@Composable
fun LastMoveStats(move: LichessDbMove) {
    //todo make this not look terrible
    val error = openingViewModel.humanMoveErrorMessage.collectAsState().value
    if (error == "") {
        Row {
            val whitePercent = (move.whiteWinPercent * 100).toInt()
            val drawPercent = (move.drawPercent * 100).toInt()
            val blackPercent = (move.blackWinPercent * 100).toInt()
            val playedPercent = (move.playedPercent * 100).toInt()
            Column {
                Row {
                    Text(
                        text = "You played: ${move.san}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Played: $playedPercent% of games",
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .border(
                            BorderStroke(3.dp, SolidColor(Color.Black)),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .height(40.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                ) {

                    Text(
                        text = "$whitePercent%", textAlign = TextAlign.Center,
                        color = Color.Black, fontSize = 24.sp,
                        modifier = Modifier
                            .weight(max(move.whiteWinPercent, 0.15f))
                            .background(Color.White)
                            .height(40.dp)
                    )

                    Text(
                        text = "$drawPercent%", textAlign = TextAlign.Center,
                        color = Color.White, fontSize = 24.sp,
                        modifier = Modifier
                            .weight(max(move.drawPercent, 0.15f))
                            .background(Color.Gray)
                            .height(40.dp)
                    )

                    Text(
                        text = "$blackPercent%", textAlign = TextAlign.Center,
                        color = Color.White, fontSize = 24.sp,
                        modifier = Modifier
                            .weight(max(move.blackWinPercent, 0.15f))
                            .background(Color.Black)
                            .height(40.dp)
                    )

                }
            }
        }
    } else {
        Text(text = error)
    }
}



