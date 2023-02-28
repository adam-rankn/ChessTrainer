package com.pinguapps.chesstrainer.data

class Piece(
    val color: Color,
    val type: PieceType,
    var pinned: PinnedState = PinnedState.NONE)
