package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.logic.Chessgame
import com.pinguapps.chesstrainer.ui.OpeningViewModel

@Composable
fun OpeningScreen(
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Chessboard(Chessgame())
    }

    val viewModel2 = OpeningViewModel()
    viewModel2.getMoves("master?fen=rnbqkbnr%2Fpppppppp%2F8%2F8%2F8%2F8%2FPPPPPPPP%2FRNBQKBNR%20w%20KQkq")

}