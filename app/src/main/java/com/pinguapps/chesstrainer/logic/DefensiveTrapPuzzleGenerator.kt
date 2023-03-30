package com.pinguapps.chesstrainer.logic

import kotlin.random.Random

class DefensiveTrapPuzzleGenerator {

    private val randomGenerator = Random(System.currentTimeMillis())
    val puzzle = pickDefensivePuzzle()

    fun pickDefensivePuzzle(): Triple<String, List<String>, Int> {

        val allPuzzles = mutableListOf<Triple<String,List<String>,Int>>()

        val puzzle = allPuzzles[randomGenerator.nextInt(0,allPuzzles.size-1)]
        return puzzle
    }
}