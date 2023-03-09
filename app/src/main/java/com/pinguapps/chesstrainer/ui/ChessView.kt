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
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.res.ResourcesCompat
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.data.Square
import kotlin.math.abs
import kotlin.math.floor


class ChessView : View {
    var game = Chessgame()
    private var board = game.chessboard
    private var playerColor = game.playerColor

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



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec )
    }

    override fun onDraw(canvas: Canvas) {
        if (playerColor == com.pinguapps.chesstrainer.data.Color.BLACK){
            //canvas.rotate(180f)
        }
        drawBoard(canvas)
        drawSelectedPiece(canvas)
        drawCheckHighlight(canvas)
        drawPieces(canvas)
        drawValidMoves(canvas)


    }

    private fun drawBoard(canvas: Canvas) {
        if (playerColor != com.pinguapps.chesstrainer.data.Color.BLACK) {
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
        else {
            val tileSize = width.coerceAtMost(height) / 8
            for (col in 0..7) for (row in 0..7) {
                val paint: Paint =
                    if ((col + row) % 2 == 0) WHITE_PAINT else BLACK_PAINT
                canvas.drawRect(
                    (abs(col-7) * tileSize).toFloat(),
                    (abs(row-7) * tileSize).toFloat(),
                    ((abs(col-7) + 1) * tileSize).toFloat(),
                    ((abs(row-7) + 1) * tileSize).toFloat(),
                    paint
                )
            }
        }
    }

    private fun drawPieces(canvas: Canvas) {
        if (playerColor != com.pinguapps.chesstrainer.data.Color.BLACK) {
            for (col in 0..7) for (row in 0..7) {
                val square = board.board[col][row]
                drawPiece(canvas, square)
            }
        }
        else {
            for (col in 0..7) for (row in 0..7) {
                val square = board.board[col][row]
                //val square = board.board[abs(col-7)][abs(row-7)]
                drawPiece(canvas, square)
            }
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

    private fun drawCheckHighlight(canvas: Canvas){

        if (board.whiteInCheck){
            val square = board.whiteKingSquare
            drawPicture(canvas, square, R.drawable.ring)
        }
        else if (board.blackInCheck){
            val square = board.blackKingSquare
            drawPicture(canvas, square, R.drawable.ring)

        }
    }

    private fun drawSelectedPiece(canvas: Canvas){
        if (board.selectedSquare != null){
            val tileSize = height.coerceAtMost(width) / 8

            val col: Int
            val row: Int

            if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                col = board.selectedSquare!!.col
                row = board.selectedSquare!!.row
            }
            else {
                col = abs(board.selectedSquare!!.col - 7)
                row = abs(board.selectedSquare!!.row - 7)
            }


            //todo load from themes
            HIGHLIGHT_PAINT.color = Color.rgb(253, 33, 21)
            val paint: Paint = HIGHLIGHT_PAINT
            canvas.drawRect(
                (col * tileSize).toFloat(),
                (row * tileSize).toFloat(),
                ((col + 1) * tileSize).toFloat(),
                ((row + 1) * tileSize).toFloat(),
                paint)
        }

    }

    private fun drawPicture(canvas: Canvas, square: Square, picture: Int) {
        if (playerColor != com.pinguapps.chesstrainer.data.Color.BLACK) {
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
        else {
            val tileSize = height.coerceAtMost(width) / 8
            val x: Int = abs(7 -square.col)
            val y: Int = abs(7 - square.row)
            val drawable: Drawable? = ResourcesCompat.getDrawable(resources, picture, null)
            val bitmapDrawable = (drawable as BitmapDrawable).bitmap
            canvas.drawBitmap(
                bitmapDrawable, null,
                Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize),
                null
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val tileSize = height.coerceAtMost(width) / 8
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                val x: Float = event.x
                val y: Float = event.y
                val col: Int
                val row: Int

                if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                    col = floor(x.div(tileSize)).toInt()
                    row = floor(y.div(tileSize)).toInt()
                }
                else {
                    col = abs(floor(x.div(tileSize)).toInt()-7)
                    row = abs(floor(y.div(tileSize)).toInt()-7)
                }

                if (row in 0..7 && col in 0..7) {

                    val clickedSquare = board.board[col][row]
                    val highlightedSquare = board.selectedSquare
                    if (highlightedSquare != null && board.isMoveValid(clickedSquare)) {
                        board.makeMove(clickedSquare)
                        invalidate()
                    } else {
                        if (clickedSquare.pieceType != PieceType.NONE && board.turn == clickedSquare.pieceColor) {
                            //todo add check for player color
                            board.validMoves = board.generatePieceMoves(clickedSquare)
                            board.selectedSquare = clickedSquare
                            invalidate()
                        } else {
                            board.selectedSquare = null
                            board.validMoves = mutableListOf()
                        }
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {

                val x: Float = event.x
                val y: Float = event.y

                val col: Int
                val row: Int

                if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                    col = floor(x.div(tileSize)).toInt()
                    row = floor(y.div(tileSize)).toInt()
                }
                else {
                    col = abs(floor(x.div(tileSize)).toInt()-7)
                    row = abs(floor(y.div(tileSize)).toInt()-7)
                }


                if (row in 0..7 && col in 0..7) {
                    val highlightedSquare = board.selectedSquare
                    val clickedSquare = board.board[col][row]

                    if (highlightedSquare != null && board.isMoveValid(clickedSquare)) {
                        board.makeMove(clickedSquare)
                        invalidate()
                    } else {
                        if (clickedSquare.pieceType != PieceType.NONE && board.turn == clickedSquare.pieceColor) {
                            //todo add check for player color
                            board.validMoves = board.generatePieceMoves(clickedSquare)
                            board.selectedSquare = clickedSquare
                            invalidate()
                        } else {
                            board.selectedSquare = null
                            board.validMoves = mutableListOf()
                        }
                    }

                }
            }
            }
        return true
    }

    fun drawValidMoves(canvas: Canvas) {
        if (board.validMoves.size > 0) {
            val square = board.selectedSquare
            if (square != null &&square.pieceType != PieceType.NONE) {
                val moves = board.generatePieceMoves(square)
                for (move in moves) {
                    val circleSquare = move.endSquare
                    val tileSize = height.coerceAtMost(width) / 8
                    val x: Int
                    val y: Int

                    if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                        x = circleSquare.col
                        y = circleSquare.row
                    }
                    else {
                        x = abs(circleSquare.col -7)
                        y = abs (circleSquare.row - 7)
                    }

                    val drawable: Drawable? =
                        ResourcesCompat.getDrawable(resources, R.drawable.circle, null)
                    val bitmapDrawable = (drawable as BitmapDrawable).bitmap
                    canvas.drawBitmap(
                        bitmapDrawable, null,
                        Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize),
                        null
                    )
                }
                invalidate()
            }

        }
    }

    fun onDrag(view: View, dragEvent: DragEvent): Boolean {
        val dragAction = dragEvent.action;
        val dragView = dragEvent.localState;
        val containsDragable: Boolean = when (dragAction) {
            DragEvent.ACTION_DRAG_EXITED -> {
                false
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                true
            }
            else -> {true}
        }
        if (dragAction == DragEvent.ACTION_DROP && containsDragable){
            //your function to move and check valid moves
        }
        return true;
    }

    companion object {
        private val WHITE_PAINT: Paint = Paint()
        private val BLACK_PAINT: Paint = Paint()
        private val HIGHLIGHT_PAINT: Paint = Paint()
    }

}

