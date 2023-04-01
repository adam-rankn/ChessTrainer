package com.pinguapps.chesstrainer.ui.screens


import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.allOpenings
import com.pinguapps.chesstrainer.logic.Chessgame
import com.pinguapps.chesstrainer.ui.OpeningViewModel
import com.pinguapps.chesstrainer.ui.composables.AutoCompleteBox
import com.pinguapps.chesstrainer.ui.composables.Chessboard
import com.pinguapps.chesstrainer.ui.composables.TextSearchBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp


val openingViewModel = OpeningViewModel()
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OpeningScreen(
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AutoCompleteOpeningBox(openings = allOpenings)
        Chessboard(openingViewModel.chessGame)
    }

    val openingViewmodel = OpeningViewModel()
    //openingViewmodel.getMoves("master?fen=rnbqkbnr%2Fpppppppp%2F8%2F8%2F8%2F8%2FPPPPPPPP%2FRNBQKBNR%20w%20KQkq")

}

@Composable
fun OpeningAutoCompleteItem(opening: Opening) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
            modifier = Modifier.testTag(AutoCompleteSearchBarTag),
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



