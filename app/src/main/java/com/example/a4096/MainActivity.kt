package com.example.a4096

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity(), SwipeListener.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<View>(0)
    }

    private fun Initialize() {
        ;
    }

    override fun onSwipe(swipe: String) {
        when (swipe) {
            "RIGHT" -> {
                Log.d("TAG", "Right")
            }
            "LEFT" -> {
                Log.d("TAG", "Left")
            }
            "DOWN" -> {
                Log.d("TAG", "Down")
            }
            "UP" -> {
                Log.d("TAG", "Up")
            }
        }
    }
}