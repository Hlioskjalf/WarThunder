package com.example.battletanks

import android.app.Activity
import android.view.View
import com.example.battletanks.activities.SCORE_REQUEST_CODE
import com.example.battletanks.activities.ScoreActivity
import com.example.battletanks.activities.binding
import com.google.android.material.animation.AnimationUtils

class GameCore(private val activity: Activity) {
    @Volatile
    private var isPlay = false
    private var isPlayedOrBaseDestroyed = false
    private var isPlayerWin = false

    fun startOrPauseGame() {
        isPlay = !isPlay
    }

    fun isPlaying() = isPlay && !isPlayedOrBaseDestroyed && !isPlayerWin

    fun pauseTheGame() {
        isPlay = false
    }

    fun playerWon(score: Int) {
        isPlayerWin = true
        activity.startActivityForResult(
            ScoreActivity.createIntent(activity, score),
            SCORE_REQUEST_CODE
        )
    }

    fun destroyPlayerOrBase() {
        isPlayedOrBaseDestroyed = true
        pauseTheGame()
        animateEndGame()
    }

    private fun animateEndGame() {
        activity.runOnUiThread {
            binding.gameOverText.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
            binding.gameOverText.startAnimation(slideUp)
        }
    }
}