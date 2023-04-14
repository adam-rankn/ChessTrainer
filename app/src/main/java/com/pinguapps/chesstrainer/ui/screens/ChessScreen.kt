package com.pinguapps.chesstrainer.ui.screens


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.ui.*
import com.pinguapps.chesstrainer.ui.theme.ChessPurple
import com.pinguapps.chesstrainer.ui.theme.MenuBgDark

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
    OpeningSetup,
    Preferences,
    Splash
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
    val topBarState = rememberSaveable { (mutableStateOf(false)) }

    when (backStackEntry ?.destination?.route) {
        "Splash" -> {
            topBarState.value = false
        }
        "Menu" -> {
            topBarState.value = false
        }
        "OpeningSetup" -> {
            topBarState.value = true
        }
        "Opening" -> {
            topBarState.value = true
        }
        "Preferences" -> {
            topBarState.value = true
        }
        "Chess" -> {
            topBarState.value = true
        }
        "Knight" -> {
            topBarState.value = true
        }
        "Pawn" -> {
            topBarState.value = true
        }
    }
    Scaffold(
        topBar = {
            if (topBarState.value){
                    ChessAppBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() },
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
            }
            else {
                if (currentScreen == ChessScreen.Menu){
                    Spacer(modifier = Modifier.fillMaxWidth().height(64.dp).background(MenuBgDark))
                }
                else if (currentScreen == ChessScreen.Splash){
                    Spacer(modifier = Modifier.fillMaxWidth().height(64.dp).background(ChessPurple))
                }
            }

        }
/*        topBar = {
            ChessAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                modifier = Modifier.padding(bottom = 0.dp)
            )
        }*/

    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = ChessScreen.Splash.name,
            modifier = modifier.padding(innerPadding),
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
            composable(route = ChessScreen.Splash.name) {
                SplashScreen(onTimeElapsed = {
                    navController.navigate("Menu") {
                            popUpTo(0) {
                                inclusive = true

                        }
                    }
                })
            }
            composable(route = ChessScreen.Preferences.name) {
                PrefsScreen(onCancelButtonClicked = {
                    navigateToMenu(viewModel,navController)

                })


            }
        }
    }
}

fun navigateToMenu(
    viewModel: ChessboardViewModel,
    navController: NavHostController
) {
    viewModel.resetGame()
    navController.popBackStack(ChessScreen.Menu.name, inclusive = false)
}


