package com.pinguapps.chesstrainer.ui

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Color
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
    ): View? {
        _binding = FragmentChessboardBinding.inflate(inflater, container, false)
        val view = binding.root
        val chessViewModel: ChessboardViewModel by activityViewModels()

        val board = chessViewModel.chessboard

/*        val  layoutParams = LinearLayout.LayoutParams(0,0,
        1.0f)

        for (col in 0..7) {
            val columnLayout = LinearLayout(context)
            for (row in 0..7){
                val layout = LinearLayout(context)
                if (row + col % 2 == 0) {
                    layout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                else {
                    layout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                layout.layoutParams = layoutParams
                columnLayout.addView(layout)
            }
            columnLayout.layoutParams = layoutParams
            chessboardLayout.addView(columnLayout)
        }*/


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
