package com.pinguapps.chesstrainer.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.pinguapps.chesstrainer.data.Color
import com.pinguapps.chesstrainer.data.LichessDbMove
import com.pinguapps.chesstrainer.data.Opening
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.data.remote.*
import com.pinguapps.chesstrainer.logic.Chessgame
import com.pinguapps.chesstrainer.util.generateUciNotation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class OpeningViewModel : ViewModel() {
    private val lichessRepository = LichessApiRepository()
    val chessgame = Chessgame()

    val lichessLines: MutableStateFlow<List<LichessDbMove>> = MutableStateFlow(listOf())
    private val pastlines = Stack<List<LichessDbMove>>()
    private val futureLines = Stack<List<LichessDbMove>>()

    val humanMoveLichessStats: MutableStateFlow<LichessDbMove> = MutableStateFlow(LichessDbMove())
    private val pastStats = Stack<LichessDbMove>()
    private val futureStats = Stack<LichessDbMove>()


    val lichessLinesErrorMessage: MutableStateFlow<String> = MutableStateFlow("")
    val humanMoveErrorMessage: MutableStateFlow<String> = MutableStateFlow("")

    val linesVisible = MutableStateFlow(true)
    val position = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"

    init {
        getMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
    }


    private fun getMoves(fen: String) {
        CoroutineScope(Dispatchers.Default).launch {
            lichessLinesErrorMessage.value = ""
            val url = "master?fen=$fen"
            val allGamesUrl =
                "lichess?variant=standard&speeds=blitz,rapid,classical&ratings=2200,2900&fen=$fen"
            //todo user prefs for elo, speed
            when (val response = lichessRepository.getMovesFromFen(url)) {
                is LichessResponse.Loading -> {

                }
                is LichessResponse.Success -> {
                    val linesData = response.data?.let { getLinesData(it) }
                    if (linesData != null && linesData.isNotEmpty()) {
                        Log.d("vm", "updating lines data")
                        lichessLines.value = linesData
                        Log.d("vm", "${lichessLines.value.map { it.uci }}")
                    } else {
                        Log.e("viewmodel", "opening data was null")
                        lichessLinesErrorMessage.value = "No lines in DB for this position"
                    }
                }
                is LichessResponse.Failure -> {
                    Log.e("Lichess API", "Error getting moves: ${response.e}")
                    lichessLinesErrorMessage.value = "Failed to get lines"
                }
            }
        }
    }

    /**
     * makes cpu move if it is cpu turn, gets line data if player turn
     * called once on training start to ensure state is correct
     */
    fun startTraining() {
        if (chessgame.isPlayerTurn()) {
            getMoves(chessgame.generateFenStringFromPosition())
        } else if (chessgame.isCpuTurn()) {
            makeCpuMove()
        }
    }


    fun onOpeningSelected(opening: Opening) {
        loadPositionFromFenString(opening.fen)
        getMoves(opening.fen)
    }

    fun onHintClicked() {
        linesVisible.value = linesVisible.value.not()
    }

    val fenLoadError = MutableStateFlow("")
    fun loadPositionFromFenString(fen: String) {
        chessgame.restartGame()
        chessgame.loadPositionFenString(fen)
    }

    fun onPlayerColorClicked(color: Color) {
        chessgame.playerColor = color
        Log.d("openvm", "player = ${chessgame.playerColor}")
    }

    fun onHumanMoveMade(start: Square, end: Square) {
        pastlines.push(lichessLines.value)
        pastStats.push(humanMoveLichessStats.value)
        Log.d("vm", "onHumanMoveMade called")
        //chessgame.makeHumanMove(end)
        humanMoveErrorMessage.value = ""
        for (move in lichessLines.value) {
            Log.d("vm", "checking move")
            Log.d("checking,", "${move.uci} vs (played) ${generateUciNotation(start, end)}")
            if (move.uci == generateUciNotation(start, end)) {
                humanMoveErrorMessage.value = ""
                humanMoveLichessStats.value = move
                Log.d("vm", "move found, breaking")
                break
            }
            humanMoveErrorMessage.value = "Your move was not found in the database"
        }
        Log.d("vm", "calling makeCpuMove()")
        makeCpuMove()
        futureLines.clear()
        futureStats.clear()
    }

    private fun makeCpuMove() {
        if (chessgame.toMove == chessgame.cpuColor) {
            Log.d("openvm", "to move (cpu): ${chessgame.cpuColor}")
            val pos = chessgame.generateFenStringFromPosition()
            CoroutineScope(Dispatchers.Default).launch {
                val url = "master?fen=$pos"
                val allGamesUrl =
                    "lichess?variant=standard&speeds=blitz,rapid,classical&ratings=2200,2900&fen=$pos"
                //todo user prefs for elo, speed
                when (val response = lichessRepository.getMovesFromFen(url)) {
                    is LichessResponse.Loading -> {

                    }
                    is LichessResponse.Success -> {
                        val linesData = response.data?.let { getLinesData(it) }
                        if (linesData != null && linesData.isNotEmpty()) {
                            val move = pickRandomMoveWeighted(linesData)
                            chessgame.makeMove(move)
                            Log.d("openvm", "cpu moved: ${linesData[0].uci}")
                            Log.d("openvm", "to move (human): ${chessgame.toMove}")
                            getMoves(chessgame.generateFenStringFromPosition())
                        } else {
                            Log.e("viewmodel", "opening data was null")
                            lichessLinesErrorMessage.value = "no games for this position"
                        }
                    }
                    is LichessResponse.Failure -> {
                        Log.e("Lichess API", "Error getting moves: ${response.e}")
                        lichessLinesErrorMessage.value = "failed to get lines"
                    }
                }
            }
        }
    }

    /**
     * picks a random move weighted based on the % of games the move was played in the database
     */
    private fun pickRandomMoveWeighted(moves: List<LichessDbMove>): String {
        val random = (0..100).random()
        var cumulativePercentage = 0
        for (move in moves) {
            cumulativePercentage += (move.playedPercent * 100).toInt()
            if (cumulativePercentage >= random) {
                return move.uci
            }
        }
        return moves[0].uci
    }


    /**
     * puts current board position on the future move stack and loads previous position
     * Also loads the previous set of DB lines from history
     * @see redoMove
     *
     */
    fun undoMove() {
        chessgame.undoMove()

        if (pastlines.isEmpty()) {
            return
        }
        if (chessgame.isPlayerTurn()) {
            futureLines.push(lichessLines.value)
            lichessLines.value = pastlines.pop()
        }
        humanMoveLichessStats.value = LichessDbMove()
/*        if (chessgame.isPlayerTurn() && pastStats.isNotEmpty() && chessgame.moveCounter > 0){
            futureStats.push(humanMoveLichessStats.value)
            humanMoveLichessStats.value = pastStats.pop()
        }*/
    }

    /**
     * puts current board position on the past move stack and loads position from top of future move stack
     * Also loads the next set of DB lines from future
     * @see undoMove
     *
     */
    fun redoMove() {
        chessgame.redoMove()
        if (futureLines.isEmpty()) {
            return
        }
        if (chessgame.isPlayerTurn()) {
            pastlines.push(lichessLines.value)
            lichessLines.value = futureLines.pop()
        }
        humanMoveLichessStats.value = LichessDbMove()
/*        if (chessgame.isPlayerTurn() && futureStats.isNotEmpty()){
            pastStats.push(humanMoveLichessStats.value)
            humanMoveLichessStats.value = futureStats.pop()
        }*/
    }

    /**
     * undoes all moves until start position is reached, adding each to the future move stack
     * also handles all past sets of DB lines
     * @see undoMove
     *
     */
    fun undoAllMoves() {
        chessgame.undoAllMoves()
        if (pastlines.isEmpty()) {
            return
        }
        while (pastlines.isNotEmpty()) {
            futureLines.push(lichessLines.value)
            lichessLines.value = pastlines.pop()
        }
/*        while (pastStats.isNotEmpty()) {
            futureStats.push(humanMoveLichessStats.value)
            humanMoveLichessStats.value = pastStats.pop()
        }*/
    }

    /**
     * redoes all moves in the future stack until position after most recent move is reached,
     * adding each to move history stack. Also also handles all future sets of DB lines
     * @see redoMove
     *
     */
    fun redoAllMoves() {
        chessgame.redoAllMoves()
        if (futureLines.isEmpty()) {
            return
        }
        while (futureLines.isNotEmpty()) {
            pastlines.push(lichessLines.value)
            lichessLines.value = futureLines.pop()
        }
/*        while (pastStats.isNotEmpty()) {
            pastStats.push(humanMoveLichessStats.value)
            humanMoveLichessStats.value = futureStats.pop()
        }*/
    }

    fun resetGame() {
        chessgame.newGame()
    }


}