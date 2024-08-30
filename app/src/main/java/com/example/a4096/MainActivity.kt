package com.example.a4096

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), SwipeListener.Listener {
    private val size: Int = 4
    private val panel = Array(size) { arrayOfNulls<TextView>(size) }
    private val board = Array(size) { IntArray(size) }

    private var score: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 画面サイズ取得
        val wm = this.windowManager.currentWindowMetrics
        val insets = wm.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        val width = wm.bounds.width() - insets.left - insets.right
        val height = wm.bounds.height() - insets.top - insets.bottom

        val view = findViewById<View>(R.id.mainLayout)
        view.setOnTouchListener(SwipeListener(this))

        val main = findViewById<LinearLayout>(R.id.contentsPanel)

        val dp = resources.displayMetrics.density
        val contentWidth = Math.round((width - (32 * dp * 2)) / 4)
        for (row in 0 until size) {
            var rowLinearLayout = LinearLayout(this)
            rowLinearLayout.orientation = LinearLayout.HORIZONTAL
            main.addView(rowLinearLayout)

            for (col in 0 until size) {
                var textView = TextView(this)
                textView.textSize = 60f
                textView.gravity = Gravity.CENTER_HORIZONTAL
                textView.width = contentWidth
                textView.height = contentWidth
                textView.background = getDrawable(R.drawable.border)
                panel[row][col] = textView
                rowLinearLayout.addView(panel[row][col])
            }
        }

        initialize()
    }

    private fun initialize() {
        // 起動時はランダムに２つ配置する
        addNewTile()
        addNewTile()
        updateDisplay()
    }

    private fun updateDisplay() {
        for (row in 0 until size) {
            for (col in 0 until size) {
                val view = panel[row][col]
                if (board[row][col] > 0) {
                    view?.text = board[row][col].toString()
                }
                else {
                    view?.text = ""
                }
            }
        }
    }

    override fun onSwipe(swipe: String) {
        mergeRow(swipe)
    }

    private fun addNewTile() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (board[row][col] == 0) {
                    emptyCells.add(row to col)
                }
            }
        }
        if (emptyCells.isNotEmpty()) {
            val (row, col) = emptyCells.random()
            board[row][col] = 2
            Log.d("TAG", "addNewTile " + row.toString() + ":" + col.toString())
        }
    }

    private fun mergeRow(swipe: String) {
        // 盤面をスワイプ方向に寄せる
        // 計算はこの後で行う
        for (i in 0 until size) {
            for (col in 0 until size) {
                for (row in 0 until size) {
                    when (swipe) {
                        "RIGHT" -> {
                            move(col, row, col + 1, row)
                        }
                        "LEFT" -> {
                            move(col, row, col - 1, row)
                        }
                        "DOWN" -> {
                            move(col, row, col, row + 1)
                        }
                        "UP" -> {
                            move(col, row, col, row - 1)
                        }
                    }
                }
            }
        }

        // マージできるかチェック
        when (swipe) {
            "LEFT" -> {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        calculation(x, y, x - 1, y)
                    }
                }
            }
            "RIGHT" -> {
                for (x in size - 1 downTo 0) {
                    for (y in size - 1 downTo 0) {
                        calculation(x, y, x + 1, y)
                    }
                }
            }
            "UP" -> {
                for (x in 0 until size) {
                    for (y in 0 until size) {
                        calculation(x, y, x, y - 1)
                    }
                }
            }
            "DOWN" -> {
                for (x in size - 1 downTo 0) {
                    for (y in size - 1 downTo 0) {
                        calculation(x, y, x, y + 1)
                    }
                }
            }
        }

        // マージした後の盤面をスワイプ方向に寄せる
        for (col in 0 until size) {
            for (row in 0 until size) {
                when (swipe) {
                    "RIGHT" -> {
                        move(col, row, col + 1, row)
                    }
                    "LEFT" -> {
                        move(col, row, col - 1, row)
                    }
                    "DOWN" -> {
                        move(col, row, col, row + 1)
                    }
                    "UP" -> {
                        move(col, row, col, row - 1)
                    }
                }
            }
        }

        addNewTile()
        updateDisplay()
    }

    private fun move(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (x2 < 0 || size <= x2 || y2 < 0 || size <= y2) {
            return
        }

        if (board[y2][x2] == 0) {
            board[y2][x2] = board[y1][x1]
            board[y1][x1] = 0
        }
    }

    private fun calculation(x1: Int, y1: Int, x2: Int, y2: Int) {
        if (x2 < 0 || size <= x2 || y2 < 0 || size <= y2) {
            return
        }

        if (board[y2][x2] == board[y1][x1]) {
            board[y1][x1] = 0
            board[y2][x2] = board[y2][x2] * 2
            updateScore(board[x2][y2])
        }
    }

    private fun updateScore(value: Int) {
        score += value
        var scoreLabel = findViewById<TextView>(R.id.textView)
        scoreLabel.text = score.toString()
    }
}