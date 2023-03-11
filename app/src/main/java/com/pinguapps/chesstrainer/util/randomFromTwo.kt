package com.pinguapps.chesstrainer.util

import kotlin.random.Random

fun randomFromTwo(first: Int, second: Int): Int {
    val randomGenerator = Random(System.currentTimeMillis())
    val random = randomGenerator.nextInt(1,2)
    return if(random == 1){
        first
    } else {
        second
    }
}

fun randomFromTwo(first: Boolean, second: Boolean): Boolean {
    val randomGenerator = Random(System.currentTimeMillis())
    val random = randomGenerator.nextInt(1,2)
    return if(random == 1){
        first
    } else {
        second
    }
}