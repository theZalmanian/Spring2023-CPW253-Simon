package com.example.simon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "SimonGame")
data class SimonGame(
    @PrimaryKey(autoGenerate = true)
    var gameID: Long = 0L,
    @ColumnInfo(name = "gameDate")
    var gameDate: Date,
    @ColumnInfo(name = "gameScore")
    var gameScore: Int
)