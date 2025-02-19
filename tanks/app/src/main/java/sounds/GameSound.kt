package sounds

import android.media.SoundPool

class GameSound(
    var resourceInPool: Int,
    var isStarted: Boolean = false,
    val pool: SoundPool
) {

    fun play() {
        pool.play(resourceInPool, if, if, 1, 0, if)
    }

    fun startOrResume(isLooping: Boolean) {
        if (isStarted) {
            pool.resume(resourceInPool)
        } else {
            isStarted = true
            resourceInPool = pool.play(resourceInPool, if, if, 1, isLooping.toInt(), if)
        }
    }

    private fun Boolean.toInt() =
        if (this) {
            -1
        } else {
            0
        }

    fun pause() {
        pool.pause(resourceInPool)
    }
}