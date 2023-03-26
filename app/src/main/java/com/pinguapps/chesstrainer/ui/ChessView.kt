package com.pinguapps.chesstrainer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.google.android.material.color.MaterialColors
import com.pinguapps.chesstrainer.R
import com.pinguapps.chesstrainer.data.Chessgame
import com.pinguapps.chesstrainer.data.GameResult
import com.pinguapps.chesstrainer.data.PieceType
import com.pinguapps.chesstrainer.data.Square
import com.pinguapps.chesstrainer.databinding.PopupGameOverBinding
import com.pinguapps.chesstrainer.databinding.PopupPawnPromotionBinding
import kotlin.math.abs
import kotlin.math.floor


class ChessView : View {
    var game = Chessgame()
    var board = game.chessboard
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

    private fun init() {
        WHITE_PAINT.color = MaterialColors.getColor(this, R.attr.boardLightSquare)
        BLACK_PAINT.color = MaterialColors.getColor(this, R.attr.boardDarkSquare)
        HIGHLIGHT_PAINT.color = MaterialColors.getColor(this, R.attr.colorHighlightPiece)
        TARGET_PAINT.color = MaterialColors.getColor(this, R.attr.boardTargetSquare)
    }

    /**
     *  starts observing values
     *  @see observeGameResult
     *  @see observePawnPromotion
     *  ViewTreeLifecycleOwner() has been set at this point which is required
     *  for observables in custom view
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        observePawnPromotion()
        observeGameResult()
    }

    /**
     *  forces the view to be a square
     *  on landscape will fill height
     *  on portrait will fill width
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthPixels = MeasureSpec.getSize(widthMeasureSpec)
        val heightPixels = MeasureSpec.getSize(heightMeasureSpec)
        if (widthPixels > heightPixels){
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        }
        else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawBoard(canvas)
        drawSelectedPiece(canvas)
        drawCheckHighlight(canvas)
        drawTargetSquare(canvas)
        drawPieces(canvas)
        drawValidMoves(canvas)
    }

    /**
     * Draws the board board. If player is black, perspective is inverted
     * @see drawSquare
     */
    private fun drawBoard(canvas: Canvas) {
            for (col in 0..7) for (row in 0..7) {
                val paint: Paint = if ((col + row) % 2 == 0) WHITE_PAINT else BLACK_PAINT
                drawSquare(col,row,paint,canvas)
            }
    }

    /**
     * Draws the square at the given column and row. if player is black, board is flipped
     * so perspective will be inverted
     * @see drawBoard
     * @see drawTargetSquare
     * @see drawSelectedPiece
     */
    fun drawSquare(col: Int, row: Int, paint: Paint, canvas: Canvas){
        val tileSize = width.coerceAtMost(height) / 8
        if (playerColor != com.pinguapps.chesstrainer.data.Color.BLACK) {
            canvas.drawRect(
                (col * tileSize).toFloat(),
                (row * tileSize).toFloat(),
                ((col + 1) * tileSize).toFloat(),
                ((row + 1) * tileSize).toFloat(),
                paint
            )
        }
        else {
            canvas.drawRect(
                (abs(col-7) * tileSize).toFloat(),
                (abs(row-7) * tileSize).toFloat(),
                ((abs(col-7) + 1) * tileSize).toFloat(),
                ((abs(row-7) + 1) * tileSize).toFloat(),
                paint
            )
        }
    }

    /**
     * Draws all pieces on board.
     */
    private fun drawPieces(canvas: Canvas) {
        for (col in 0..7) for (row in 0..7) {
            val square = board.board[col][row]
            drawPiece(canvas, square)
        }
    }

    /**
     * checks square and chooses the correct drawable to draw for current piece
     *
     * @param square the square on the board to check for piece type
     */
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

    /**
     * Highlights a king which is in check
     */
    private fun drawCheckHighlight(canvas: Canvas){

        if (board.whiteInCheck){
            val square = board.whiteKingSquare
            drawPicture(canvas, square, R.drawable.ring)
        }
        else if (board.blackInCheck){
            val square = board.blackKingSquare
            drawPicture(canvas, square, R.drawable.ring)
//todo make this look not bad
        }
    }


    /**
     * Highlights the square which contains currently selected piece
     * @see drawSquare
     */
    private fun drawSelectedPiece(canvas: Canvas){
        if (board.selectedSquare != null) {
            val (col,row) = getCoordinatesFromSquare(board.selectedSquare!!)
            val paint: Paint = HIGHLIGHT_PAINT
            drawSquare(col, row, paint, canvas)
        }

    }

    /**
     * Highlights the square which is the current puzzle target, if exists
     * @see drawSquare
     */
    private fun drawTargetSquare(canvas: Canvas){
        if (game.targetSquare!= null){
            val (col, row ) = getCoordinatesFromSquare(game.targetSquare!!)
            val paint: Paint = TARGET_PAINT
            drawSquare(col,row,paint,canvas)
        }
    }

    /**
     * draws an image on a chosen square
     */
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

    //accessibility will be handled through speech to text, therefore we do not need performclick here
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                //todo animate pieces
            }
            MotionEvent.ACTION_UP -> {
                val x: Float = event.x
                val y: Float = event.y
                val (col, row) = getSquareFromCoordinates(x,y)

                if (row in 0..7 && col in 0..7) {
                    handleBoardClick(col,row)
                }
            }
            MotionEvent.ACTION_DOWN -> {
                val x: Float = event.x
                val y: Float = event.y
                val (col, row) = getSquareFromCoordinates(x,y)

                if (row in 0..7 && col in 0..7) {
                    handleBoardClick(col,row)
                }
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        performClick()
        return super.performClick()
    }


    /** Takes coordinates and returns corresponding square in board
     *
     * @param x x coordinate in view
     * @param y y coordinate in view
     *
     * @return column and row on chessboard that was clicked
     */
    private fun getSquareFromCoordinates(x: Float, y: Float): Pair<Int,Int>{
        val tileSize = height.coerceAtMost(width) / 8
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
        return Pair(col,row)
    }

    /**
     * Helper function that gives the pixel coordinates of the given board square
     * @param square the square to find the coords of
     * @see getSquareFromCoordinates
     */

    private fun getCoordinatesFromSquare(square: Square): Pair<Int, Int> {
        val col: Int
        val row: Int
        if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
            col = square.col
            row = square.row
        }
        else {
            col = abs(square.col - 7)
            row = abs(square.row - 7)
        }
        return Pair(col,row)
    }

    /** Takes the appropriate action depending on whether a piece is selected
     * if piece is selected and clicked square is valid move, make move. If clicked on
     * own piece, select piece. If piece selected and clicked square is not valid, deselect piece
     *
     * @param col column of clicked square
     * @param row row of clicked square
     *
     */
    private fun handleBoardClick(col: Int, row: Int) {
        if (game.gameResult.value != GameResult.GAME_IN_PROGRESS){
            return
        }
        val highlightedSquare = board.selectedSquare
        val clickedSquare = board.board[col][row]
        if (highlightedSquare != null && board.isMoveValid(clickedSquare)) {
            game.makeHumanMove(clickedSquare)
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


    /** Draws a circle on every square that the currently selected piece can move to
     *
     * @param canvas the view's canvas
     *
     */
    private fun drawValidMoves(canvas: Canvas) {
        if (board.validMoves.size > 0) {
            val square = board.selectedSquare
            if (square != null &&square.pieceType != PieceType.NONE) {
                val moves = board.generatePieceMoves(square)
                for (move in moves) {
                    val circleSquare = move.endSquare
                    val tileSize = height.coerceAtMost(width) / 8
                    val (x,y) = getCoordinatesFromSquare(circleSquare)
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




    /**
     *  handles the promotion of pawns
     *  observes the promotion square value of current board and lets user choose piece
     *  to promote to
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun observePawnPromotion(){
        findViewTreeLifecycleOwner()?.let {lifeCycleOwner ->
            board.promotionSquare.observe(lifeCycleOwner) {square ->

                val popupInflater = context.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                val popupBind = PopupPawnPromotionBinding.inflate(popupInflater as LayoutInflater)


                if (square.pieceColor == com.pinguapps.chesstrainer.data.Color.WHITE) {
                    val popupWhite = PopupWindow(popupBind.root, WRAP_CONTENT, WRAP_CONTENT, false)

                    popupBind.root.setOnTouchListener { _, _ ->
                        true
                    }
                    popupWhite.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)


                    popupBind.promotionKnight.setOnClickListener {
                        board.promotePawn(square,PieceType.KNIGHT,com.pinguapps.chesstrainer.data.Color.WHITE)
                        game.makeComputerMove()
                        invalidate()
                        popupWhite.dismiss()
                    }
                    popupBind.promotionBishop.setOnClickListener {
                        board.promotePawn(square,PieceType.BISHOP,com.pinguapps.chesstrainer.data.Color.WHITE)
                        game.makeComputerMove()
                        invalidate()
                        popupWhite.dismiss()
                    }
                    popupBind.promotionRook.setOnClickListener {
                        board.promotePawn(square,PieceType.ROOK,com.pinguapps.chesstrainer.data.Color.WHITE)
                        game.makeComputerMove()
                        invalidate()
                        popupWhite.dismiss()
                    }
                    popupBind.promotionQueen.setOnClickListener {
                        board.promotePawn(square,PieceType.QUEEN,com.pinguapps.chesstrainer.data.Color.WHITE)
                        game.makeComputerMove()
                        invalidate()
                        popupWhite.dismiss()
                    }
                }
                else {
                    popupBind.promotionKnight.setImageResource(R.drawable.b_knight)
                    popupBind.promotionBishop.setImageResource(R.drawable.b_bishop)
                    popupBind.promotionRook.setImageResource(R.drawable.b_rook)
                    popupBind.promotionQueen.setImageResource(R.drawable.b_queen)

                    val popupBlack = PopupWindow(popupBind.root, WRAP_CONTENT, WRAP_CONTENT, false)
                    popupBlack.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)

                    popupBind.promotionKnight.setOnClickListener {
                        Log.d("test","before pawn promotion to horsey")
                        game.promotePawn(square,PieceType.KNIGHT,com.pinguapps.chesstrainer.data.Color.BLACK)
                        Log.d("test","pawn promotion to horsey")
                        game.makeComputerMove()
                        invalidate()
                        popupBlack.dismiss()
                    }
                    popupBind.promotionBishop.setOnClickListener {
                        game.promotePawn(square,PieceType.BISHOP,com.pinguapps.chesstrainer.data.Color.BLACK)
                        Log.d("test","pawn promotion to b-shop")
                        invalidate()
                        game.makeComputerMove()
                        popupBlack.dismiss()
                    }
                    popupBind.promotionRook.setOnClickListener {
                        game.promotePawn(square,PieceType.ROOK,com.pinguapps.chesstrainer.data.Color.BLACK)
                        Log.d("test","pawn promotion to rook")
                        game.makeComputerMove()
                        invalidate()
                        popupBlack.dismiss()
                    }
                    popupBind.promotionQueen.setOnClickListener {
                        game.promotePawn(square,PieceType.QUEEN,com.pinguapps.chesstrainer.data.Color.BLACK)
                        Log.d("test","pawn promotion to queen")
                        game.makeComputerMove()
                        invalidate()
                        popupBlack.dismiss()
                    }
                }

            }
        }
    }

    /**
     * observes game result and makes game result popup depending on result
     */
    private fun observeGameResult() {
        findViewTreeLifecycleOwner()?.let { lifeCycleOwner ->
            game.gameResult.observe(lifeCycleOwner) { gameResult ->
                val popupInflater = context.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                val popupBind = PopupGameOverBinding.inflate(popupInflater as LayoutInflater)
                val popupGG = PopupWindow(popupBind.root, WRAP_CONTENT, WRAP_CONTENT, false)
                when (gameResult) {

                    GameResult.GAME_IN_PROGRESS -> {}
                    GameResult.BLACK_WIN_CHECKMATE -> {
                        popupBind.resultTypeText.setText(R.string.result_checkmate)
                        if (playerColor == com.pinguapps.chesstrainer.data.Color.BLACK){
                            popupBind.resultText.setText(R.string.result_win)
                        }
                        else {
                            popupBind.resultText.setText(R.string.result_lose)
                        }
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.WHITE_WIN_CHECKMATE -> {
                        popupBind.resultTypeText.setText(R.string.result_checkmate)
                        if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                            popupBind.resultText.setText(R.string.result_win)
                        }
                        else {
                            popupBind.resultText.setText(R.string.result_lose)
                        }
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.BLACK_WIN_RESIGNATION -> {
                        popupBind.resultTypeText.setText(R.string.result_resignation)
                        if (playerColor == com.pinguapps.chesstrainer.data.Color.BLACK){
                            popupBind.resultText.setText(R.string.result_win)
                        }
                        else {
                            popupBind.resultText.setText(R.string.result_lose)
                        }
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.WHITE_WIN_RESIGNATION -> {
                        popupBind.resultTypeText.setText(R.string.result_resignation)
                        if (playerColor == com.pinguapps.chesstrainer.data.Color.WHITE){
                            popupBind.resultText.setText(R.string.result_win)
                        }
                        else {
                            popupBind.resultText.setText(R.string.result_lose)
                        }
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.DRAW_BY_AGREEMENT -> {
                        popupBind.resultTypeText.setText(R.string.result_agreement)
                        popupBind.resultText.setText(R.string.result_draw)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.DRAW_BY_REPETITION ->{
                        popupBind.resultTypeText.setText(R.string.result_repetition)
                        popupBind.resultText.setText(R.string.result_draw)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.DRAW_BY_FIFTY -> {
                        popupBind.resultTypeText.setText(R.string.result_fifty)
                        popupBind.resultText.setText(R.string.result_draw)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.DRAW_BY_INSUFFICIENT -> {
                        popupBind.resultTypeText.setText(R.string.result_material)
                        popupBind.resultText.setText(R.string.result_draw)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.DRAW_BY_STALEMATE -> {
                        popupBind.resultTypeText.setText(R.string.result_stalemate)
                        popupBind.resultText.setText(R.string.result_draw)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.PUZZLE_WON -> {
                        popupBind.resultText.setText(R.string.puzzle_passed)
                        popupBind.btnRematch.setText(R.string.new_puzzle)
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    GameResult.PUZZLE_FAILED -> {
                        popupBind.resultTypeText.setText(R.string.wrong_move)
                        popupBind.resultText.setText(R.string.puzzle_failed)
                        popupGG.isFocusable = true
                        popupBind.btnRematch.setText(R.string.new_puzzle)
                        popupBind.btnRetry.visibility = VISIBLE
                        popupGG.showAtLocation(this.rootView, Gravity.CENTER, 0, 0)
                    }
                    else -> {}
                }
                popupBind.btnMenu.setOnClickListener {
                    //todo go to menu
                }
                popupBind.btnRematch.setOnClickListener {
                    board.clearBoard()
                    game.newGame()
                    invalidate()
                    popupGG.dismiss()
                }

                popupBind.btnRetry.setOnClickListener {
                    game.restartGame()
                    invalidate()
                    popupGG.dismiss()
                }
            }

        }
    }

    companion object {
        private val WHITE_PAINT: Paint = Paint()
        private val BLACK_PAINT: Paint = Paint()
        private val HIGHLIGHT_PAINT: Paint = Paint()
        private val TARGET_PAINT: Paint = Paint()
    }

}

