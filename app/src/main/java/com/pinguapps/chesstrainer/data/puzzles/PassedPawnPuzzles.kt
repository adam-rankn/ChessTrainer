package com.pinguapps.chesstrainer.data.puzzles

class PassedPawnPuzzles {

    val whiteSquarePositionsList = listOf(
        "k7/ppp1p3/5pp1/6P1/5P2/8/PPP5/K7 w - - 0 1",
        "k7/p1p1p3/1p3pp1/6P1/P4P2/1P6/2P5/K7 w - - 0 1",
        "7k/2p2ppp/pp6/P7/1P6/8/6PP/7K w - - 0 1",
        "6k1/2p3pp/pp6/P7/1P6/8/5PPP/6K1 w - - 0 1",
        "5k2/2p3pp/pp6/P7/1P6/8/8/7K w - - 0 1",
        "7k/3p2pp/1pp5/1P6/2P5/8/8/7K w - - 0 1",
        "7k/3p1p1p/1pp3p1/1P6/2P5/8/8/7K w - - 0 1",
        "8/3p1ppk/1pp4p/1P6/2P5/8/8/7K w - - 0 1")

    val whiteHatPositionsList = listOf(
        "k7/ppp4p/6p1/6P1/5P1P/8/PPP5/K7 w - - 0 1",
        "k7/pp5p/6p1/6P1/K4P1P/1P6/2P5/8 w - - 0 1",
        "7k/p6p/1p4p1/1P6/P1P5/8/6PP/7K w - - 0 1",
        "7k/p4ppp/1p6/1P6/P1P5/8/7P/7K w - - 0 1",
        "6k1/p4ppp/1p6/1P6/P1P5/8/6KP/8 w - - 0 1",
        "7k/1p3ppp/2p5/2P5/1P1P4/8/6KP/8 w - - 0 1",
        "k7/ppp3p1/5p2/5P2/4P1P1/8/8/K7 w - - 0 1"
    )

    val whiteSquigglyPositionsList = listOf(
        "1k6/5pp1/6p1/4P1P1/5P1P/8/8/K7 w - - 0 1",
        "k7/ppp2pp1/6p1/4P1P1/5P1P/8/P7/K7 w - - 0 1",
        "k7/2p2pp1/1p4p1/p3P1P1/P4P1P/1P6/2P5/K7 w - - 0 1",
        "k7/1pp2pp1/p5p1/4P1P1/5P1P/1P6/P1P5/K7 w - - 0 1",
        "k7/2p2pp1/pp4p1/4P1P1/5P1P/P1P5/1P6/K7 w - - 0 1",
        "7k/1pp2ppp/1p6/1P1P4/P1P5/7P/6P1/7K w - - 0 1",
        "8/1pp2p1k/1p4p1/1P1P3p/P1P5/6P1/7P/7K w - - 0 1",
        "8/1pp2ppk/1p5p/1P1P4/P1P5/8/7P/7K w - - 0 1",
        "8/1pp2p1k/1p4p1/1P1P3p/P1P4P/8/8/7K w - - 0 1"
    )
    val whiteUndoublerList = listOf(
        "8/5kp1/7p/4KP1P/5P2/8/8/8 w - - 0 1",
        "8/3kp3/5p2/2KP1P2/3P4/8/8/8 w - - 0 1",
        "8/5pk1/4p3/4P1PK/6P1/8/8/8 w - - 0 1"
    )

    val spacerPuzzles = listOf(
        "8/4pp2/8/3P1P1k/8/7K/8/8 w - - 0 1",
        "8/1pp5/8/1P1P3k/8/7K/8/8 w - - 0 1",
        "7k/pp4pp/8/P1P5/8/8/6PP/7K w - - 0 1"
    )


    val otherPuzzles = listOf(
        "k7/5pp1/6p1/4P1P1/7P/8/8/K7 w - - 0 1"
    )

    val allPuzzles: List<String> get() = listOf(
        whiteUndoublerList,
        whiteHatPositionsList,
        whiteSquigglyPositionsList,
        whiteSquarePositionsList,
        otherPuzzles).flatten()

}