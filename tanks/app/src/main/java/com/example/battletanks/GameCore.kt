package com.example.battletanks

import android.app.Activity
import android.view.View
import com.google.android.material.animation.AnimationUtils

class GameCore(private val activity: Activity) {
    @Volatile
    private var isPlay = false
    private var isPlayedOrBaseDestroyed = false

    fun startOrPauseGame() {
        isPlay = !isPlay
    }

    fun isPlaying() = isPlay && !isPlayedOrBaseDestroyed

    fun pauseTheGame() {
        isPlay = false
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