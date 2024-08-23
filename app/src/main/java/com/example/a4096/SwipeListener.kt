package com.example.a4096

import android.location.GnssAntennaInfo
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

class SwipeListener(private val listener: Listener): OnTouchListener {
    private var startX: Float = 0.0f
    private var startY: Float = 0.0f

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        when (p1?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 指が触れた位置を取得
                startX = p1.x
                startY = p1.y
            }
            MotionEvent.ACTION_UP -> {
                // 指が離れた位置を取得
                val endX = p1.x
                val endY = p1.y
                val distanceX = endX - startX
                val distanceY = endY - startY
                // 移動距離が100px以上だったらスワイプしたと判断
                if (distanceX > 100) {
                    // 右
                    listener.onSwipe("RIGHT")
                }
                else if (distanceX < -100) {
                    // 左
                    listener.onSwipe("LEFT")
                }
                else if (distanceY > 100) {
                    // 下
                    listener.onSwipe("DOWN")
                }
                else if (distanceY < -100) {
                    // 上
                    listener.onSwipe("UP")
                }
            }
        }
        return true
    }

    interface Listener {
        fun onSwipe(swipe: String)
    }
}