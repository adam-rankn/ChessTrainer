package com.pinguapps.chesstrainer.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.ui.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChessAppBar(
    currentScreen: ChessScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

enum class ChessScreen {
    Menu,
    Chess,
    Knight,
    Pawn,
    Opening,
    OpeningSetup
}
@Composable
fun ChessApp(modifier: Modifier = Modifier,
             viewModel: ChessboardViewModel = viewModel(),
             navController: NavHostController = rememberNavController()) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ChessScreen.valueOf(
        backStackEntry?.destination?.route ?: ChessScreen.Menu.name
    )
    // TODO: Create NavController

    // TODO: Get current back stack entry

    // TODO: Get the name of the current screen
    Scaffold(
        topBar = {
            ChessAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier.padding(bottom = 0.dp)
            )
        }
    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = ChessScreen.Menu.name,
            modifier = modifier.padding(innerPadding)
        ){
            composable(route = ChessScreen.Menu.name) {
                MenuScreen(
                    navController = navController
                )
            }
            composable(route = ChessScreen.Opening.name) {
                OpeningScreen(
                    onCancelButtonClicked = {
                        navController.navigate("OpeningSetup")
                    },

                )
            }
            composable(route = ChessScreen.OpeningSetup.name) {
                OpeningSetupScreen(
                    onCancelButtonClicked = {
                    navigateToMenu(viewModel,navController)
                },
                    onStartClicked = {
                        navController.navigate("Opening")
                    }
                )
            }
            composable(route = ChessScreen.Pawn.name) {
                PawnScreen(onCancelButtonClicked = {
                    navigateToMenu(viewModel,navController)
                })
            }
            composable(route = ChessScreen.Chess.name) {
                BotChessScreen(onCancelButtonClicked = {
                    navigateToMenu(viewModel,navController)
                })
            }
            composable(route = ChessScreen.Knight.name) {
                KnightScreen(onCancelButtonClicked = {
                    navigateToMenu(viewModel,navController)
                })
            }
        }
    }
}


