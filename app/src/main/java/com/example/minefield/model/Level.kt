package com.example.minefield.model

enum class Level(private val row: Int, private val col: Int){
    EASY(11, 6),
    MEDIUM(18, 10),
    HARD(25, 14)
}