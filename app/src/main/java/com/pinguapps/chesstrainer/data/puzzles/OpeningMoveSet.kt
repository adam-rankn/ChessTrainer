package com.pinguapps.chesstrainer.data.puzzles

class OpeningMoveSet {

    val sicilianMainlineMovesBlack = hashMapOf<String,List<String>>(
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2" to
                listOf("d7d6","b8c6","e7e6"),
        "rnbqkbnr/pp2pppp/3p4/2p5/3PP3/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 3" to
                listOf("c5d4"),
        "rnbqkbnr/pp2pppp/3p4/8/3NP3/8/PPP2PPP/RNBQKB1R b KQkq - 0 4" to
                listOf("g8f6"),
        "rnbqkb1r/pp2pppp/3p1n2/8/3NP3/2N5/PPP2PPP/R1BQKB1R b KQkq - 2 5" to listOf("a7a6"),
        "rnbqkb1r/1p2pppp/p2p1n2/8/3NP3/2N1B3/PPP2PPP/R2QKB1R b KQkq - 1 6" to listOf("e7e5"),
        "rnbqkb1r/1p3ppp/p2p1n2/4p3/4P3/1NN1B3/PPP2PPP/R2QKB1R b KQkq - 1 7" to listOf("c8e6","f8e7"),
        "rnbqkb1r/1p2pppp/p2p1n2/8/3NP3/2N5/PPP1BPPP/R1BQK2R b KQkq - 1 6" to listOf("e7e5","e7e6"),
        "rnbqkb1r/1p3ppp/p2p1n2/4p3/4P3/1NN5/PPP1BPPP/R1BQK2R b KQkq - 1 7" to listOf("f8e7")


    )
}