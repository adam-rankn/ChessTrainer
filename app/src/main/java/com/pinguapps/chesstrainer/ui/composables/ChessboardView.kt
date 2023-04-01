package com.pinguapps.chesstrainer.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.pinguapps.chesstrainer.logic.Chessgame
import com.pinguapps.chesstrainer.ui.ChessView

@Composable
fun Chessboard(game: Chessgame) {

    val state = remember { mutableStateOf(0) }

    AndroidView(factory = { context ->
        ChessView(context).apply {
            this.game = game
            this.setViewTreeLifecycleOwner(findViewTreeLifecycleOwner())
        }
    })

}