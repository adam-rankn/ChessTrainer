package com.pinguapps.chesstrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pinguapps.chesstrainer.ui.BotChessFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, BotChessFragment())
            .commit()
    }
}