package com.example.minefield

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.minefield.databinding.FieldBinding
import estudo.model.Field
import estudo.model.FieldEvent

class FieldButton @JvmOverloads constructor(
    context: Context, private val field: Field, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private lateinit var binding: FieldBinding;

    init {
        initialize()
    }

    @SuppressLint("ResourceType")
    private fun initialize() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(super.getContext()),
            R.layout.field, this, true
        )
        field.onEvent(this::putStyle)
        this.default()
        onClick()
        onLongClick()

        val level = resources.getDimensionPixelSize(R.dimen.level_hard)
        val layoutParam = LayoutParams(level, level)
        binding.box.layoutParams = layoutParam
    }

    private fun setColor(color: Int) {
        binding.colorField = color
    }

    private fun onClick() {
        binding.box.setOnClickListener {
            field.open()
        }
    }


    private fun onLongClick() {
        binding.box.setOnLongClickListener {
            field.changeFlag()
            true
        }
    }


    private fun putStyle(field: Field, fieldEvent: FieldEvent) {
        when (fieldEvent) {
            FieldEvent.EXPLOSION -> explosion()
            FieldEvent.OPEN -> open()
            FieldEvent.FLAG -> flag()
            else -> default()
        }
    }

    private fun explosion() {
        setColor(Color.RED)
        binding.box.text = "X"
    }

    private fun open() {
        setColor(Color.rgb(0, 75, 51))
        val textColor = when (field.quantityNeighborWithMine) {
            1 -> Color.GREEN
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5, 6 -> Color.RED
            else -> Color.MAGENTA
        }

        binding.box.setTextColor(textColor)

        binding.box.text = (field.quantityNeighborWithMine.takeIf { it > 0 } ?: "").toString()
    }

    private fun flag() {
        binding.box.text = "F"
        binding.box.textSize = 20f
    }

    private fun default() {
        val position: Int = field.row - field.col
        if (position % 2 == 0) {
            setColor(Color.rgb(0, 153, 51))
        } else {
            setColor(Color.rgb(0, 204, 68))
        }
    }
}