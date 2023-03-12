package com.pinguapps.chesstrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pinguapps.chesstrainer.databinding.FragmentChessboardBinding
import com.pinguapps.chesstrainer.logic.PassedPawnPuzzle
import androidx.lifecycle.ViewTreeLifecycleOwner

class ChessboardFragment: Fragment() {


    private var _binding: FragmentChessboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChessboardBinding.inflate(inflater, container, false)
        val view = binding.root
        val chessViewModel: ChessboardViewModel by activityViewModels()

        val boardView = binding.chessboard
        ViewTreeLifecycleOwner.set(boardView, this)


        val puzzleGen = PassedPawnPuzzle()
        puzzleGen.generateBasicPuzzle()
        boardView.game.chessboard = puzzleGen.chessboard
        boardView.board = puzzleGen.chessboard
        boardView.invalidate()
        //boardView.game = chessViewModel.chessgame


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
