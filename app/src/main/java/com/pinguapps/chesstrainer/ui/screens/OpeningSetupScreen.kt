package com.pinguapps.chesstrainer.ui.screens


import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.data.allOpenings
import com.pinguapps.chesstrainer.ui.OpeningViewModel
import com.pinguapps.chesstrainer.ui.composables.AutoCompleteBox
import com.pinguapps.chesstrainer.ui.composables.ChessControlsBar
import com.pinguapps.chesstrainer.ui.composables.Chessboard
import com.pinguapps.chesstrainer.ui.composables.TextSearchBar
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

val openingViewModel = OpeningViewModel()
@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun OpeningSetupScreen(
    onCancelButtonClicked: () -> Unit,
    onStartClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        AutoCompleteOpeningBox(openings = allOpenings)
        Chessboard(
            openingViewModel.chessgame
        )
        var text by remember { mutableStateOf(TextFieldValue("")) }

        val clipboardManager: ClipboardManager = LocalClipboardManager.current

/*        Row() {
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                },
                label = { Text(text = "Load from FEN")},
                modifier = Modifier.fillMaxWidth(0.8f)

            )
            Button(onClick = {
                try {
                    openingViewModel.loadPositionFromFenString(text.text)
                }
                catch (e: Exception){
                    //todo
                    Toast.makeText(context,"failed to load position", LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.weight(0.15f)) {
                Text(text = "Load")
            }
        }*/

        Row() {
            Image(
                painter = painterResource(id = R.drawable.w_king),
                contentDescription = "play as white",
                modifier = Modifier.size(80.dp).clickable {
                    openingViewModel.onPlayerColorClicked(Color.WHITE)
                }
            )

            Image(
                painter = painterResource(id = R.drawable.b_king),
                contentDescription = "play as black",
                modifier = Modifier.size(80.dp).clickable {
                    openingViewModel.onPlayerColorClicked(Color.BLACK)
                }
            )
            Button(onClick = { openingViewModel.resetGame() }) {
                Text(text = "Reset Board")
            }
        }



        Button(onClick = {
            onStartClicked()
            coroutineScope.launch {
            //make sure human is first to move
                openingViewModel.makeCpuMove()
            }
        }, modifier = Modifier.fillMaxWidth().height(80.dp)
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
        keySelector = {opening ->
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
