package com.example.simon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SimonGameDao {
    @Insert
    suspend fun addGame(currGame:SimonGame)

    @Query("SELECT * FROM SimonGame ORDER BY gameID DESC")
    fun getAllGames(): LiveData<List<SimonGame>>

    @Query("SELECT MAX(gameScore) FROM SimonGame")
    fun getHighestScore(): LiveData<Int>
}