package com.example.battletanks.drawers

import android.widget.FrameLayout
import com.example.battletanks.CELL_SIZE
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
    private var moveALlTanksThread: Thread? = null

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
            BulletDrawer(container, elements, this)
        )
        enemyTank.element.drawElement(container)
        elements.add(enemyTank.element)
        tanks.add(enemyTank)
    }

    fun moveEnemyTanks() {
        Thread( {
            while (true) {
                goThroughAllTanks()
                Thread.sleep(400)
            }
        }).start()
    }

    private fun goThroughAllTanks() {
        moveALlTanksThread = Thread( {
            tanks.forEach {
                it.move(it.direction, container, elements)
                if (checkIfChanceBiggerThanRandom(10)) {
                    it.bulletDrawer.makeBulletMove(it)
                }
            }
        })
        moveALlTanksThread?.start()
    }

    fun startEnemyCreation() {
        Thread(Runnable{
            while(enemyAmount < MAX_ENEMY_AMOUNT) {
                drawEnemy()
                enemyAmount++
                Thread.sleep(3000)
            }
        }).start()
    }

    fun removeTank(tankIndex: Int) {
        if (tankIndex < 0) return
        moveALlTanksThread?.join()
        tanks.removeAt(tankIndex)
    }
}