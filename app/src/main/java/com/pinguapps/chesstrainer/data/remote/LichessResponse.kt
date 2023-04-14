package com.pinguapps.chesstrainer.data.remote

sealed class LichessResponse<out T> {
    object Loading : LichessResponse<Nothing>()

    data class Success<out T>(
        var data: @UnsafeVariance T?
    ) : LichessResponse<T>()

    data class Failure(
        val e: Exception
    ) : LichessResponse<Nothing>()
}