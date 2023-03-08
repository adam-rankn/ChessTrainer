package com.pinguapps.chesstrainer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pinguapps.chesstrainer.databinding.FragmentChessboardBinding

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

        val board = chessViewModel.chessboard

        val boardView = binding.chessboard
        //boardView.layoutParams = LinearLayout.LayoutParams(boardView.width,boardView.width)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
