package com.example.minefield

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minefield.databinding.ActivityBoardBinding
import estudo.model.Board
import estudo.model.BoardEvent

class BoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBoardBinding
    private lateinit var board: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        board = Board(quantityRow = 11, quantityCol = 7, quantityMine = 15)
        board.onEvent(this::showResult)
        generateBoard(board)
    }


    @SuppressLint("ResourceType")
    fun generateBoard(board: Board) {
        for (j in 0 until board.quantityRow) {


            val trHead = TableRow(this)

            trHead.layoutParams = ViewGroup.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            for (i in 0 until board.quantityCol) {
                val field = FieldButton(this, board.fields[j][i])
                trHead.addView(field)
            }

            binding.main.addView(trHead)
        }

    }

    fun showMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    private fun showResult(event: BoardEvent) {

        val message = when (event) {
            BoardEvent.WIN -> "You win!!!"
            BoardEvent.LOSS -> "You loss!!! :P"
        }

        showMessage(this, message)
        board.restart()
    }
}