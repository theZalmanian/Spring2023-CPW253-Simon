package com.example.simon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimonGameViewModelFactory(private val dao:SimonGameDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SimonGameViewModel::class.java)) {
            return SimonGameViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}