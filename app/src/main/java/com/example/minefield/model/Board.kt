package estudo.model

import java.util.Random

enum class BoardEvent { WIN, LOSS }

class Board(val quantityRow: Int, val quantityCol: Int, val quantityMine: Int) {
    val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvent) -> Unit>()

    init {
        generateFields()
        putNeighbors()
        sortMines()
    }

    private fun generateFields() {
        for (row in 0 until quantityRow) {
            fields.add(ArrayList())
            for (col in 0 until quantityCol) {
                val newField = Field(row = row, col = col)
                newField.onEvent(this::verifyLossOrWin)
                fields[row].add(newField)
            }
        }
    }

    private fun putNeighbors() {
        forEachFields { putNeighbors(it) }
    }

    private fun putNeighbors(field: Field) {
        val (row, col) = field
        val rows = arrayOf(row - 1, row, row + 1)
        val cols = arrayOf(col - 1, col, col + 1)

        rows.forEach { r ->
            cols.forEach { c ->
                val current = fields.getOrNull(r)?.getOrNull(c)
                current?.takeIf { field != it }?.let { field.addNeighbor(it) }
            }
        }

    }

    private fun sortMines() {
        val generate = Random()

        var rowSorted: Int
        var colSorted: Int
        var currentQuantityMines = 0

        while (currentQuantityMines < quantityMine) {
            rowSorted = generate.nextInt(quantityRow)
            colSorted = generate.nextInt(quantityCol)

            val fieldSorted = fields[rowSorted][colSorted]
            if (fieldSorted.safe) {
                fieldSorted.addMine()
                currentQuantityMines++
            }
        }
    }

    private fun goalAttain(): Boolean {
        var playerWin = true
        forEachFields { if (!it.goalAttain) playerWin = false }
        return playerWin
    }

    private fun verifyLossOrWin(field: Field, event: FieldEvent) {
        if (event == FieldEvent.EXPLOSION) {
            callbacks.forEach { it(BoardEvent.LOSS) }
        } else if (goalAttain()) {
            callbacks.forEach { it(BoardEvent.WIN) }
        }
    }


    fun forEachFields(callback: (Field) -> Unit) {
        fields.forEach {
            it.forEach(callback)
        }
    }

    fun onEvent(callback: (BoardEvent) -> Unit) {
        callbacks.add(callback)
    }

    fun restart() {
        forEachFields { it.restart() }
        sortMines()
    }

}