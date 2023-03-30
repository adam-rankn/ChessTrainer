package com.pinguapps.chesstrainer.data.remote

object Api {

    private external fun baseUrlFromJNI(boolean: Boolean): String

    const val BASE_URL = "https://explorer.lichess.ovh/master?fen="

    private const val V1 = "v1/"

    const val DATA_LIST = V1 + "my_data.php"
}