package com.pinguapps.chesstrainer.ui.util

import android.content.Context
import android.media.AudioManager

fun (() -> Unit).withSound(context: Context): () -> Unit = {
    (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        .playSoundEffect(AudioManager.FX_KEY_CLICK)
    this()
}