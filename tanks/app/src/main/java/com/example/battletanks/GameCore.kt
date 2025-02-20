package com.example.battletanks

import android.app.Activity
import android.view.View
import android.view.animation.Animation
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

    fun resumeTheGame() {
        isPlay = true
    }

    fun playerWon(score: Int) {
        isPlayerWin = true
        activity.startActivityForResult(
            ScoreActivity.createIntent(activity, score),
            SCORE_REQUEST_CODE
        )
    }

    fun destroyPlayerOrBase(score: Int) {
        isPlayedOrBaseDestroyed = true
        pauseTheGame()
        animateEndGame(score)
    }

    private fun animateEndGame(score: Int) {
        activity.runOnUiThread {
            binding.gameOverText.visibility = View.VISIBLE
            val slideUp = AnimationUtils.loadAnimation(activity, R.anim.slide_up)
            binding.gameOverText.startAnimation(slideUp)
            slideUp.setAnimationListner(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) { }
                override fun onAnimationRepeat(animation: Animation?) { }
                override fun onAnimationEnd(animation: Animation?) {
                    activity.startActivityForResult(
                        ScoreActivity.createIntent(activity, score),
                        SCORE_REQUEST_CODE
                    )
                }
            })
        }
    }
}