package com.example.simon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class SimonGameViewModel(val dao: SimonGameDao) : ViewModel() {

    /**
     * Adds the given "Simon" game data to the db
     * @param gameDate The date the game took place on
     * @param gameScore The user's score at the end of the game
     */
    fun addSimonGame(gameDate:Date, gameScore:Int) {
        viewModelScope.launch {
            // setup simon game w/ given data
            val simonGameData = SimonGame(gameDate = gameDate, gameScore = gameScore)

            // insert the gama data into the DB
            dao.addGame(simonGameData)
        }
    }
}