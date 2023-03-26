package com.pinguapps.chesstrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewTreeLifecycleOwner
import com.pinguapps.chesstrainer.data.puzzles.KnightPuzzleGame
import com.pinguapps.chesstrainer.databinding.FragmentBotChessBinding


class BotChessFragment: Fragment() {


    private var _binding: FragmentBotChessBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBotChessBinding.inflate(inflater, container, false)
        val view = binding.root
        val chessViewModel: ChessboardViewModel by activityViewModels()

        val boardView = binding.chessboard
        ViewTreeLifecycleOwner.set(boardView, this)

        boardView.game = KnightPuzzleGame()
        boardView.board = boardView.game.chessboard
        //boardView.game.loadPositionFenString("8/8/4k3/5r2/8/8/2BN4/2K5 w - - 0 1")


        binding.btnUndo.setOnClickListener {
            chessViewModel.undoMove()
            boardView.invalidate()
        }
        binding.btnRedo.setOnClickListener {
            chessViewModel.redoMove()
            boardView.invalidate()
        }

        binding.btnGoToStart.setOnClickListener {
            chessViewModel.undoAllMoves()
            boardView.invalidate()
        }

        binding.btnGoCurrent.setOnClickListener {
            chessViewModel.redoAllMoves()
            boardView.invalidate()
        }

/*        val sep = File.separator
        val workDir = Environment.getExternalStorageDirectory()
            .toString() + sep + "ChessTrainer/uci/logs"
        val report: Report = Report { bullshit("hi") }
        val stockFish = InternalStockFish(report,workDir,context)
        stockFish.initialize()
        stockFish.applyIniFile()
        stockFish.initOptions(EngineOptions())
        stockFish.writeLineToEngine("uci")
        val mesg = stockFish.readLineFromEngine(3000)
        Toast.makeText(context,"message is $mesg",Toast.LENGTH_LONG).show()
        binding.waster.text = "reply: $mesg"*/

/*        val uci: Uci = startUci()
        uci.processLine("uci")
        uci.processLine("isready")*/

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
