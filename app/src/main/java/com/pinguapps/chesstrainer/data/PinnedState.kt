package com.pinguapps.chesstrainer.data

/**
 * whether a piece is pinned, and if so, in which direction.
 * vertical pin means the piece can only move vertically
 */
enum class PinnedState {

    VERTICAL,
    HORIZONTAL,
    DIAGONALA1H8,
    DIAGONALA8H1,
    NONE
}