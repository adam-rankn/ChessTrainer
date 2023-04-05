package com.pinguapps.chesstrainer.data.remote

import com.google.gson.annotations.SerializedName


data class LichessOpeningData (

    @SerializedName("white"    ) var white    : Int?                = null,
    @SerializedName("draws"    ) var draws    : Int?                = null,
    @SerializedName("black"    ) var black    : Int?                = null,
    @SerializedName("moves"    ) var moves    : ArrayList<Moves>    = arrayListOf(),
    //@SerializedName("topGames" ) var topGames : ArrayList<TopGames> = arrayListOf(),
    //@SerializedName("opening"  ) var opening  : String?             = null
)

data class Moves (

    @SerializedName("uci"           ) var uci           : String? = null,
    @SerializedName("san"           ) var san           : String? = null,
    @SerializedName("averageRating" ) var averageRating : Int?    = null,
    @SerializedName("white"         ) var white         : Int?    = null,
    @SerializedName("draws"         ) var draws         : Int?    = null,
    @SerializedName("black"         ) var black         : Int?    = null,
    //@SerializedName("game"          ) var game          : String? = null
)

data class Black (

    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("rating" ) var rating : Int?    = null
)

data class White (

    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("rating" ) var rating : Int?    = null
)

data class TopGames (

    @SerializedName("uci"    ) var uci    : String? = null,
    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("winner" ) var winner : String? = null,
    @SerializedName("black"  ) var black  : Black?  = Black(),
    @SerializedName("white"  ) var white  : White?  = White(),
    @SerializedName("year"   ) var year   : Int?    = null,
    @SerializedName("month"  ) var month  : String? = null
)
