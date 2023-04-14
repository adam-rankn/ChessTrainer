package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pinguapps.chesstrainer.ui.util.withSound
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.ui.theme.MenuBgDark

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier
        .background(MenuBgDark)
        .fillMaxHeight(),
    navController: NavController
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Box(modifier = Modifier
        .background(MenuBgDark)
        .fillMaxSize()) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
/*       Button(onClick = {
            navController.navigate("Pawn")
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }.withSound(context) ,modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Pawn Puzzle",
            fontSize = 24.sp)
        }*/

/*        Button(onClick = {
            navController.navigate("Knight")
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }.withSound(context) ,modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Knight Puzzle",
                fontSize = 24.sp)
        }*/

/*        Button(onClick = {
            navController.navigate("Chess")
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }.withSound(context) ,modifier = Modifier.fillMaxWidth().height(80.dp),
        ) {
            Text(text = "Play Chess",
                fontSize = 24.sp)
        }*/
            Image(
                painter = painterResource(
                    id = R.drawable.crow_clever
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    navController.navigate("OpeningSetup")
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }.withSound(context),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
            ) {
                Text(
                    text = "Study Openings",
                    fontSize = 24.sp
                )
            }
        }
    }
}