package com.example.battletanks.drawers

import android.widget.FrameLayout
import com.example.battletanks.CELL_SIZE
import com.example.battletanks.GameCore.isPlaying
import com.example.battletanks.enums.CELLS_TANK_SIZE
import com.example.battletanks.enums.Direction.DOWN
import com.example.battletanks.enums.Material.ENEMY_TANK
import com.example.battletanks.models.Coordinate
import com.example.battletanks.models.Element
import com.example.battletanks.models.Tank
import com.example.battletanks.utils.checkIfChanceBiggerThanRandom
import com.example.battletanks.utils.drawElement

private const val MAX_ENEMY_AMOUNT = 20

class EnemyDrawer(
    private val container: FrameLayout,
    private val elements: MutableList<Element>
) {

    private val respawnList: List<Coordinate>
    private var enemyAmount = 0
    private var currentCoordinate:Coordinate
    val tanks = mutableListOf<Tank>()
    lateinit var bulletDrawer: BulletDrawer
    private var gameStarted = false

    init {
        respawnList = getRespawnList()
        currentCoordinate = respawnList[0]
    }

    private  fun getRespawnList(): List<Coordinate> {
        val respawnList = mutableListOf<Coordinate>()
        respawnList.add(Coordinate(0,0))
        respawnList.add(
            Coordinate(
                0,
                ((container.width - container.width % CELL_SIZE) / CELL_SIZE -
                        (container.width - container.width % CELL_SIZE) / CELL_SIZE % 2) *
                        CELL_SIZE / 2 - CELL_SIZE * CELLS_TANK_SIZE
            )
        )
        respawnList.add(
            Coordinate(
                0,
                (container.width - container.width % CELL_SIZE) - CELL_SIZE * CELLS_TANK_SIZE
            )
        )
        return respawnList
    }

    private fun drawEnemy() {
        var index = respawnList.indexOf(currentCoordinate) + 1
        if (index == respawnList.size) {
            index = 0
        }
        currentCoordinate = respawnList[index]
        val enemyTank = Tank (
            Element(
                material = ENEMY_TANK,
                coordinate = currentCoordinate,
            ), DOWN,
            this
        )
        enemyTank.element.drawElement(container)
        elements.add(enemyTank.element)
        tanks.add(enemyTank)
    }

    private fun moveEnemyTanks() {
        Thread( {
            while (true) {
                if (!isPlaying()) {
                    continue
                }
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    private fun goThroughAllTanks() {
            tanks.toList().forEach {
                it.move(it.direction, container, elements)
                if (checkIfChanceBiggerThanRandom(10)) {
                    bulletDrawer.addNewBulletForTank(it)
                }
            }
    }

    fun startEnemyCreation() {
        if (gameStarted) {
            return
        }
        gameStarted = true
        Thread(Runnable{
            while(enemyAmount < MAX_ENEMY_AMOUNT) {
                if (!isPlaying()) {
                    continue
                }
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
        moveEnemyTanks()
    }

    fun removeTank(tankIndex: Int) {
        if (tankIndex < 0) return
        tanks.removeAt(tankIndex)
    }
}