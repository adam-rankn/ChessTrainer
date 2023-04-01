package com.pinguapps.chesstrainer.data

import com.pinguapps.chesstrainer.ui.composables.AutoCompleteEntity
import java.util.*

class Opening(
    val name: String,
    val fen: String,
    val variations: List<Opening>? = null): AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return name.lowercase()
            .startsWith(query.lowercase())
    }
}

val kingsPawn =
    listOf(
        Opening("Alekhine defense",
        "rnbqkb1r/pppppppp/5n2/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1"),
        Opening("Alekhine's defense: Modern Variation",
        "rnbqkb1r/ppp1pppp/3p4/3nP3/3P4/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 1"),
        Opening("Bishop's Opening",
            "rnbqkbnr/pppp1ppp/8/4p3/2B1P3/8/PPPP1PPP/RNBQK1NR b KQkq - 0 1"),

        Opening("Caro-Kann",
            "rnbqkbnr/pp1ppppp/2p5/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
        listOf(
            Opening("Advance variation",
            "rnbqkbnr/pp2pppp/2p5/3pP3/3P4/8/PPP2PPP/RNBQKBNR b KQkq - 0 1"),
            Opening("Classical variation",
                "rn1qkbnr/pp2pppp/2p5/5b2/3PN3/8/PPP2PPP/R1BQKBNR w KQkq - 0 1"),
            Opening("Exchange variation",
                "rnbqkbnr/pp2pppp/2p5/3P4/3P4/8/PPP2PPP/RNBQKBNR b KQkq - 0 1"),
            Opening("Panov variation",
                "rnbqkb1r/pp2pppp/5n2/3p4/2PP4/2N5/PP3PPP/R1BQKBNR b KQkq - 0 1"),
            Opening("Karpov variation",
                "r1bqkbnr/pp1npppp/2p5/8/3PN3/8/PPP2PPP/R1BQKBNR w KQkq - 0 1"),
            )
        ),
        Opening("Danish Gambit",
            "rnbqkbnr/pppp1ppp/8/8/3pP3/2P5/PP3PPP/RNBQKBNR b KQkq - 0 1"),

        Opening("Four Knights",
            "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 ",
            listOf(
                Opening("Scotch variation",
                    "r1bqkb1r/pppp1ppp/2n2n2/4p3/3PP3/2N2N2/PPP2PPP/R1BQKB1R b KQkq - 0 1"),
                Opening("Spanish variation",
                    "r1bqkb1r/pppp1ppp/2n2n2/1B2p3/4P3/2N2N2/PPPP1PPP/R1BQK2R b KQkq - 0 1"),
            )
        ),

        Opening("French Defense",
            "r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/2N2N2/PPPP1PPP/R1BQKB1R w KQkq - 0 ",
            listOf(
                Opening("Advance variation",
                    "rnbqkbnr/ppp2ppp/4p3/3pP3/3P4/8/PPP2PPP/RNBQKBNR b KQkq - 0 1"),
                Opening("Burn variation",
                    "rnbqkb1r/ppp2ppp/4pn2/3p2B1/3PP3/2N5/PPP2PPP/R2QKBNR b KQkq - 0 1"),
                Opening("Classical variation",
                    "rnbqkb1r/ppp2ppp/4pn2/3p4/3PP3/2N5/PPP2PPP/R1BQKBNR w KQkq - 0 1"),
                Opening("Exchange variation",
                    "rnbqkbnr/ppp2ppp/4p3/3P4/3P4/8/PPP2PPP/RNBQKBNR b KQkq - 0 1"),
                Opening("Rubinstein variation",
                    "rnbqkbnr/ppp2ppp/4p3/8/3Pp3/2N5/PPP2PPP/R1BQKBNR w KQkq - 0 1"),
                Opening("Tarrasch variation",
                    "rnbqkbnr/ppp2ppp/4p3/3p4/3PP3/8/PPPN1PPP/R1BQKBNR b KQkq - 0 1"),
                Opening("Winawer variation",
                    "rnbqk1nr/ppp2ppp/4p3/3p4/1b1PP3/2N5/PPP2PPP/R1BQKBNR w KQkq - 0 1")
            )
        ),

        Opening("Italian Game",
            "r1bqkbnr/pppp1ppp/2n5/4p3/2B1P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 1",
            listOf(

            )
        ),

        Opening("King's Gambit",
            "rnbqkbnr/pppp1ppp/8/4p3/4PP2/8/PPPP2PP/RNBQKBNR b KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Modern Defense",
            "rnbqkbnr/pppppp1p/6p1/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        ),
        Opening("Philidor",
            "rnbqkbnr/ppp2ppp/3p4/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Pirc",
            "rnbqkb1r/ppp1pppp/3p1n2/8/3PP3/8/PPP2PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Russian Game",
            "rnbqkb1r/pppp1ppp/5n2/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Ruy Lopez",
            "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Scandinavian",
            "rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Scottish Game",
            "r1bqkbnr/pppp1ppp/2n5/4p3/3PP3/5N2/PPP2PPP/RNBQKB1R b KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Sicilian",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        ),

        Opening("Vienna Game",
            "rnbqkbnr/pppp1ppp/8/4p3/4P3/2N5/PPPP1PPP/R1BQKBNR b KQkq - 0 1",
            listOf(

            )
        ),
        Opening("Sicilian",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        ),
        Opening("Bongcloud",
            "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
            listOf(

            )
        )
    )

val queensPawn = listOf(
    Opening("Benko Gambit",
        "rnbqkb1r/p2ppppp/5n2/1ppP4/2P5/8/PP2PPPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Benoni",
        "rnbqkb1r/pp1p1ppp/4pn2/2pP4/2P5/8/PP2PPPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Blackmar-Diemar Gambit",
        "rnbqkbnr/ppp1pppp/8/3p4/3PP3/8/PPP2PPP/RNBQKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Bogo Indian",
        "rnbqk2r/pppp1ppp/4pn2/8/1bPP4/5N2/PP2PPPP/RNBQKB1R w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Catalan",
        "rnbqkb1r/pppp1ppp/4pn2/8/2PP4/6P1/PP2PP1P/RNBQKBNR b KQkq - 0 1",
        listOf(
            Opening("Open Catalan",
                "rnbqkb1r/pppp1ppp/4pn2/8/2PP4/6P1/PP2PP1P/RNBQKBNR b KQkq - 0 1",
                listOf()), //todo
            Opening("Closed Catalan",
                "rnbqk2r/ppp1bppp/4pn2/3p4/2PP4/5NP1/PP2PPBP/RNBQK2R b KQkq - 0 1",
                listOf()),

        )
    ),
    Opening("Dutch",
        "rnbqkbnr/ppppp1pp/8/5p2/3P4/8/PPP1PPPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Grünfeld",
        "rnbqkb1r/ppp1pp1p/5np1/3p4/2PP4/2N5/PP2PPPP/R1BQKBNR w KQkq - 0 1",
        listOf(

        )
    ),        Opening("King's Indian",
        "rnbqkb1r/pppppp1p/5np1/8/2PP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Nimzo-Indian",
        "rnbqk2r/pppp1ppp/4pn2/8/1bPP4/2N5/PP2PPPP/R1BQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Queen's Gambit",
        "rnbqkbnr/ppp1pppp/8/3p4/2PP4/8/PP2PPPP/RNBQKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Semi Slav",
        "rnbqkb1r/pp3ppp/2p1pn2/3p4/2PP4/2N2N2/PP2PPPP/R1BQKB1R w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Slav",
        "rnbqkbnr/pp2pppp/2p5/3p4/2PP4/8/PP2PPPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Queen's Indian",
        "rnbqkb1r/p1pp1ppp/1p2pn2/8/2PP4/5N2/PP2PPPP/RNBQKB1R w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("London",
        "rnbqkb1r/ppp1pppp/5n2/3p4/3P1B2/5N2/PPP1PPPP/RN1QKB1R b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Rapport-Jobava",
        "rnbqkb1r/ppp1pppp/5n2/3p4/3P1B2/2N5/PPP1PPPP/R2QKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Richter-Veresov",
        "rnbqkb1r/ppp1pppp/5n2/3p2B1/3P4/2N5/PPP1PPPP/R2QKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Trompowsky",
        "rnbqkb1r/pppppppp/5n2/6B1/3P4/8/PPP1PPPP/RN1QKBNR b KQkq - 0 1",
        listOf(

        )
    )
)

val other = listOf(
    Opening("Réti",
        "rnbqkbnr/ppp1pppp/8/3p4/2P5/5N2/PP1PPPPP/RNBQKB1R b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("English",
        "rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Minso-Larsen",
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Bird",
        "rnbqkbnr/pppppppp/8/8/5P2/8/PPPPP1PP/RNBQKBNR b KQkq - 0 1",
        listOf(

        )
    ),
    Opening("Hungarian",
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1",
        listOf(

        )
    ),
)

val allOpenings = listOf(kingsPawn, queensPawn, other).flatten()