package com.pinguapps.chesstrainer.ui.screens


import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.allOpenings
import com.pinguapps.chesstrainer.ui.OpeningViewModel
import com.pinguapps.chesstrainer.ui.composables.AutoCompleteBox
import com.pinguapps.chesstrainer.ui.composables.Chessboard
import com.pinguapps.chesstrainer.ui.composables.TextSearchBar
import com.pinguapps.chesstrainer.ui.util.withSound
import kotlinx.coroutines.launch

val openingViewModel = OpeningViewModel()

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OpeningSetupScreen(
    onCancelButtonClicked: () -> Unit,
    onStartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row{
                Chessboard(openingViewModel.chessgame)
                Column() {
                    AutoCompleteOpeningBox(openings = allOpenings)
                    SetupUi(onStartClicked = onStartClicked, context = context)
                }

            }
        }
        else -> {
            Column(
                modifier = modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AutoCompleteOpeningBox(openings = allOpenings)
                Chessboard(openingViewModel.chessgame)
                SetupUi(onStartClicked = onStartClicked, context = context)

            }
        }
    }
}

@Composable
fun SetupUi(
    onStartClicked: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
){
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GameLoader(context)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.w_king),
                contentDescription = "play as white",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        openingViewModel.onPlayerColorClicked(Color.WHITE)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.b_king),
                contentDescription = "play as black",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        openingViewModel.onPlayerColorClicked(Color.BLACK)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
            )
            Button(
                onClick = remember{{
                    openingViewModel.resetGame()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }}
                    .withSound(context),
                modifier = Modifier.padding(16.dp),
            ) {
                Text(text = "Reset Board")
            }
        }
        Button(
            onClick = remember{
                {
                    onStartClicked()
                    coroutineScope.launch {
                        openingViewModel.startTraining()
                    }
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }}.withSound(context),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text(text = "Start Training With Current Position")
        }
    }
}

@Composable
fun OpeningAutoCompleteItem(opening: Opening) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = opening.name) //todo style style = MaterialTheme.typography.subtitle2
    }
}

const val AutoCompleteSearchBarTag = "AutoCompleteSearchBarTag"

@ExperimentalAnimationApi
@Composable
fun AutoCompleteOpeningBox(openings: List<Opening>) {
    AutoCompleteBox(
        items = openings,
        itemContent = { opening ->
            OpeningAutoCompleteItem(opening)
        },
        keySelector = { opening ->
            opening.fen
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { opening ->
            value = opening.name
            filter(value)
            view.clearFocus()
            openingViewModel.onOpeningSelected(opening)
        }

        TextSearchBar(
            modifier = Modifier
                .testTag(AutoCompleteSearchBarTag)
                .padding(top = 0.dp),
            value = value,
            label = "Search Openings",
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                isSearching = focusState.isFocused
                value = ""
                filter(value)
            },
            onValueChanged = { query ->
                value = query
                filter(value)
            }
        )
    }
}

@Composable
fun GameLoader(
    context: Context,

){
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            label = { Text(text = "Load from FEN") },
            modifier = Modifier.weight(1f)

        )
        Button(
            onClick = remember{{
                try {
                    openingViewModel.loadPositionFromFenString(text.text)
                } catch (e: Exception) {
                    //todo
                    Toast.makeText(context, "failed to load position", LENGTH_SHORT).show()
                }
            }}.withSound(context),
            modifier = Modifier.padding(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.back_button)
            )
        }
    }
}


