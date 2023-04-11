package com.pinguapps.chesstrainer.ui.composables

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.ui.util.withSound

@Composable
fun ChessControlsBar(
    context: Context,
    onUndoPressed: () -> Unit,
    onRedoPressed: () -> Unit,
    onUndoAllPressed: () -> Unit,
    onRedoAllPressed: () -> Unit,
    onHintPressed: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = {
                onUndoAllPressed()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        {
            Icon(
                painter = painterResource(id = R.drawable.step_backward_2),
                contentDescription = "Undo all moves",
            )
        }
        Button(
            onClick = {
                onUndoPressed()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.step_backward),
                contentDescription = "Undo last move",
            )
        }
        Button(
            onClick = {
                onHintPressed()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.lightbulb_on_outline),
                contentDescription = "Get Hint",
            )
        }
        Button(
            onClick = {
                onRedoPressed()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.step_forward),
                contentDescription = "Redo last move",
            )
        }
        Button(
            onClick = {
                onRedoAllPressed()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.step_forward_2),
                contentDescription = "Redo all moves",
            )
        }
    }
}