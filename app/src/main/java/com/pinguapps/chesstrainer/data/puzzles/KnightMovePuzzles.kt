package com.pinguapps.chesstrainer.data.puzzles

class KnightMovePuzzles {

    val knightMoveList3Ply = listOf(
        Triple(
            "r2qk2r/1b1p2p1/p3p2p/1p2P3/3Q1P2/7N/PP4PP/3R1R1K w - - 0 1",
            listOf("f2", "e4", "d6"),
            3
        ),
        Triple(
            "r2qk2r/1b1p2p1/p3p2p/1p2P3/3Q1P2/3N4/PP4PP/3R1R1K w - - 0 1",
            listOf("c3", "e4", "d6"),
            3
        ),
        Triple(
            "2kr1r2/p1p2pp1/1p2p1p1/1P2B1P1/3P1N1P/P7/4P3/3RK3 w - - 0 1",
            listOf("d3", "b4", "c6"),
            3
        ),
        Triple("k1r4r/pp4pp/2b1p3/4P3/5PN1/8/6PP/2R3RK w - - 0 1", listOf("e3", "c4", "d6"), 3),
        Triple("4r1rk/ppp4p/2b1p3/4P3/2N2P2/8/6PP/2R3RK w - - 0 1", listOf("e3", "g4", "f6"), 3),
        Triple(
            "r1bq1r1k/pp4pp/2p5/8/5P2/1B2Q1PN/PPP5/K1R3R1 w - - 0 1",
            listOf("f2", "d3", "e5"),
            3
        ),
        Triple(
            "r1bq1r1k/6pp/p1p5/1p6/3P3Q/NBP3P1/PP6/K1R2R2 w - - 0 1",
            listOf("c2", "e3", "c5"),
            3
        ),
        Triple(
            "kq3r2/p4ppp/1p1p4/1PpPp3/2P1P3/4NP2/6PP/R4R1K w - - 0 1",
            listOf("f5", "e7", "c6"),
            3
        ),
        Triple("kr6/p1p3b1/3p4/2pP4/4P3/P3KR2/1P1N4/8 w - - 0 1", listOf("c4", "a5", "c6"), 3),
        Triple(
            "r2q1rk1/1p2bppp/p1np4/4p3/4P3/N2PBB2/P1P2PPP/R3QRK1 w - - 0 1",
            listOf("b1", "c3", "d5"),
            3
        ),
        Triple(
            "2rq1rk1/pp2bppp/2np4/4p3/4P3/2BP3P/P1P1BPPN/1R2QRK1 w - - 0 1",
            listOf("g4", "e3", "d5"),
            3
        ),
        Triple(
            "2rq1rk1/p4ppp/1pb1p3/3p4/3P4/1N2P2P/P1P1BPP1/1R2QRK1 w - - 0 1",
            listOf("d2", "f3", "e5"),
            3
        ),
        Triple(
            "2rq1rk1/pp2bppp/3p4/2p5/2P5/1P1P3P/P3BPP1/1R1QNRK1 w - - 0 1",
            listOf("c2", "e3", "d5"),
            3
        ),


        Triple(
            "kq3r2/p4ppp/1p1p4/1PpPp3/2P1P3/4NP2/6PP/R4R1K w - - 0 1",
            listOf("f5", "e7", "c6"),
            3
        ),

        )

    val knightMoveList4Ply = listOf(
        Triple(
            "k3qr2/pp3ppp/8/2Pp4/3Pp3/2N1PP2/6PP/RR5K w - - 0 1",
            listOf("e2", "g3", "f5", "d6"),
            4
        ),
        Triple(
            "k3qr2/pp3ppp/8/2Pp4/3Pp3/4PP2/6PP/RRN4K w - - 0 1",
            listOf("e2", "g3", "f5", "d6"),
            4
        ),
    )

    val knightMoveList5Ply = listOf(
        Triple(
            "k4q2/5ppp/1b6/1p1P4/4P3/8/PP1N4/KRR4b w - - 0 1",
            listOf("f1", "h2", "g4", "e5", "c6"),
            5
        ),
    )

    val knightMoveList6Ply = listOf(
        Triple(
            "k7/2p5/3p4/3P4/8/1Nb1b3/8/1K6 w - - 0 1",
            listOf("c1", "e2", "g3", "f5", "e7", "c6"),
            6
        ),
        Triple(
            "8/8/2b5/1bP1k3/3P1p2/3N4/PP6/K7 w - - 0 1",
            listOf("f2", "g4", "h6", "g8", "e7", "c8", "b6"),
            7
        ),
        Triple(
            "k7/2b5/5b2/1b3P2/1N6/8/8/1K6 w - - 0 1",
            listOf("c2", "e1", "f3", "d2", "e4", "c5", "e6"),
            7
        ),

        Triple(
            "k7/8/8/3q4/8/2N5/2b5/K7 w - - 0 1",
            listOf("e2", "g1", "h3", "f2", "f4", "g4", "f6", "e8", "c7", "a6"),
            9
        ),

        )


}