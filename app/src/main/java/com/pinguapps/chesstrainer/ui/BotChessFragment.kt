package com.pinguapps.chesstrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pinguapps.chesstrainer.databinding.FragmentBotChessBinding
import androidx.lifecycle.ViewTreeLifecycleOwner

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

        boardView.game = chessViewModel.chessgame
        boardView.board = chessViewModel.chessgame.chessboard


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




        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
