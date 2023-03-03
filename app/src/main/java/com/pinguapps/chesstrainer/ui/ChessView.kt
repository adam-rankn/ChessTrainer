package com.pinguapps.chesstrainer.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Chessboard
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.data.Square


class ChessView : View {
    private var board = Chessboard()


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    fun init() {
        WHITE_PAINT.color = Color.rgb(207, 151, 85)
        BLACK_PAINT.color = Color.rgb(66, 33, 21)
        //todo load actual colors thems etc

    }

    override fun onDraw(canvas: Canvas) {
        drawBoard(canvas)
        drawPieces(canvas)
    }

    fun drawBoard(canvas: Canvas) {
        val tileSize = width.coerceAtMost(height) / 8
        for (col in 0..7) for (row in 0..7) {
            val paint: Paint =
                if ((col + row) % 2 == 0) WHITE_PAINT else BLACK_PAINT
            canvas.drawRect(
                (col * tileSize).toFloat(),
                (row * tileSize).toFloat(),
                ((col + 1) * tileSize).toFloat(),
                ((row + 1) * tileSize).toFloat(),
                paint
            )
        }
    }

    private fun drawPieces(canvas: Canvas) {
        for (col in 0 .. 7) for (row in 0 .. 7) {
            val square = board.board[col][row]
            drawPiece(canvas, square)
        }
    }

    private fun drawPiece(canvas: Canvas, square: Square) {
        if (square.pieceColor == com.pinguapps.chesstrainer.data.Color.WHITE) {
            when (square.pieceType) {
                PieceType.PAWN -> drawPicture(canvas, square, R.drawable.w_pawn)
                PieceType.KNIGHT -> drawPicture(canvas, square, R.drawable.w_knight)
                PieceType.BISHOP -> drawPicture(canvas, square, R.drawable.w_bishop)
                PieceType.ROOK -> drawPicture(canvas, square, R.drawable.w_rook)
                PieceType.QUEEN -> drawPicture(canvas, square, R.drawable.w_queen)
                PieceType.KING -> drawPicture(canvas, square, R.drawable.w_king)
                else -> {}
            }
        }
        else if (square.pieceColor == com.pinguapps.chesstrainer.data.Color.BLACK) {
            when (square.pieceType) {
                PieceType.PAWN -> drawPicture(canvas, square, R.drawable.b_pawn)
                PieceType.KNIGHT -> drawPicture(canvas, square, R.drawable.b_knight)
                PieceType.BISHOP -> drawPicture(canvas, square, R.drawable.b_bishop)
                PieceType.ROOK -> drawPicture(canvas, square, R.drawable.b_rook)
                PieceType.QUEEN -> drawPicture(canvas, square, R.drawable.b_queen)
                PieceType.KING -> drawPicture(canvas, square, R.drawable.b_king)
                else -> {}
            }
        }
    }

    private fun drawPicture(canvas: Canvas, square: Square, picture: Int) {
        val tileSize = height.coerceAtMost(width) / 8
        val x: Int = square.col
        val y: Int = square.row
        val drawable: Drawable? = ResourcesCompat.getDrawable(resources, picture, null)
        val bitmapDrawable = (drawable as BitmapDrawable).bitmap
        canvas.drawBitmap(
            bitmapDrawable, null,
            Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize),
            null
        )
    }

    companion object {
        private val WHITE_PAINT: Paint = Paint()
        private val BLACK_PAINT: Paint = Paint()
    }

    fun onDrag(v: View?, event: DragEvent): Boolean {
        val startx = event.x
        val starty = event.y
        if (event.action == DragEvent.ACTION_DRAG_ENDED) {
        }
        return false
    }
}

