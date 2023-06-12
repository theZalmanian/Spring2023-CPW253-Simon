package com.example.simon

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SimonGameDao {
    /**
     * Inserts the given game to the DB
     */
    @Insert
    suspend fun addGame(currGame:SimonGame)

    /**
     * Gets and returns all games stored in the DB in a list
     */
    @Query("SELECT * FROM SimonGame ORDER BY gameID DESC")
    fun getAllGames(): LiveData<List<SimonGame>>

    /**
     * Gets and returns the highest recorded score in the DB.
     * Returns null if the DB is empty
     */
    @Query("SELECT MAX(gameScore) FROM SimonGame")
    fun getHighestScore(): LiveData<Int>

    /**
     * Removes all game entries from the SimonGame table,
     * effectively clearing that user's game history
     *
     * Call resetSequence() immediately after for best results
     */
    @Query("DELETE FROM SimonGame")
    suspend fun clearGamesTable()

    /**
     * Rests the gameID counter in the SimonGame table to start back up from 1
     */
    @Query("DELETE FROM sqlite_sequence WHERE name='SimonGame'")
    fun resetSequence()
}