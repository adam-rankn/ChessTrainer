package com.pinguapps.chesstrainer.ui

import android.util.Log
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.LichessDbMove
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.data.remote.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OpeningViewModel: ChessboardViewModel() {
    private val lichessRepository = LichessApiRepository()

    val lichessLines: MutableStateFlow<List<LichessDbMove>> = MutableStateFlow(listOf())
    val lichessComputerMoves: MutableStateFlow<List<LichessDbMove>> = MutableStateFlow(listOf())

    val lichessErrorMessage: MutableStateFlow<String> = MutableStateFlow("Exception")

    val linesVisible = MutableStateFlow(true)
    val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    init {
        getMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }


    fun getMoves(fen: String, stateFlow: MutableStateFlow<List<LichessDbMove>> = lichessLines) {
        CoroutineScope(Dispatchers.Default).launch {
            val url = "master?fen=$fen"
            val allGamesUrl = "lichess?variant=standard&speeds=blitz,rapid,classical&ratings=2200,2900&fen=$fen"
            //todo user prefs for elo, speed
            when(val response = lichessRepository.getMovesFromFen(url)) {
                is LichessResponse.Loading -> {

                }
                is LichessResponse.Success -> {
                    val linesData = response.data?.let { getLinesData(it) }
                    if (linesData != null) {
                        stateFlow.value = linesData
                    }
                    else {
                        Log.e("viewmodel", "opening data was null")
                    }
                }
                is LichessResponse.Failure -> {
                    Log.e("Lichess API","Error getting moves: ${response.e}")
                    lichessErrorMessage.value = "failed to get lines"
                    //todo display to user
                }
            }
        }
    }


    fun onOpeningSelected(opening: Opening){
        loadPositionFromFenString(opening.fen)
        getMoves(opening.fen)
    }

    fun onHintClicked(){
        linesVisible.value = linesVisible.value.not()
    }

    val fenLoadError = MutableStateFlow("")
    fun loadPositionFromFenString(fen: String){
        chessgame.restartGame()
        chessgame.loadPositionFenString(fen)
    }

    fun onPlayerColorClicked(color: Color){
        chessgame.playerColor = color
        Log.d("openvm","player = ${chessgame.playerColor}")
    }

    suspend fun onHumanMoveMade(start:Square, end:Square) {
        //todo check human move and report
        makeCpuMove()
    }

    suspend fun makeCpuMove(){
        if (chessgame.toMove == chessgame.cpuColor) {
            Log.d("openvm", "to move (cpu): ${chessgame.cpuColor}")
            val pos = chessgame.generateFenStringFromPosition()
            CoroutineScope(Dispatchers.Default).launch {
                val url = "master?fen=$pos"
                val allGamesUrl = "lichess?variant=standard&speeds=blitz,rapid,classical&ratings=2200,2900&fen=$pos"
                //todo user prefs for elo, speed
                when(val response = lichessRepository.getMovesFromFen(url)) {
                    is LichessResponse.Loading -> {

                    }
                    is LichessResponse.Success -> {
                        val linesData = response.data?.let { getLinesData(it) }
                        if (linesData != null) {
                            chessgame.makeMove(linesData[0].uci)
                            Log.d("openvm","cpu moved: ${linesData[0].uci}")
                            Log.d("openvm", "to move (human): ${chessgame.toMove}")
                            getMoves(chessgame.generateFenStringFromPosition(),lichessLines)
                        }
                        else {
                            Log.e("viewmodel", "opening data was null")
                        }
                    }
                    is LichessResponse.Failure -> {
                        Log.e("Lichess API","Error getting moves: ${response.e}")
                        lichessErrorMessage.value = "failed to get lines"
                        //todo display to user
                    }
                }
            }







/*

            Log.d("openvm", "to move (cpu): ${chessgame.cpuColor}")
            //val pos = chessgame.generateFenStringFromPosition()
            //getMoves(pos,lichessComputerMoves)
            lichessComputerMoves.collect(collector = {
                getMoves(pos,lichessComputerMoves)
                if (it.isNotEmpty()) {
                    chessgame.makeMove(lichessComputerMoves.value[0].uci)
                    Log.d("openvm","cpu moved: ${lichessComputerMoves.value[0].uci}")
                    Log.d("openvm", "to move (human): ${chessgame.toMove}")
                    getMoves(chessgame.generateFenStringFromPosition(),lichessLines)
                }
            })*/
        }
    }





}