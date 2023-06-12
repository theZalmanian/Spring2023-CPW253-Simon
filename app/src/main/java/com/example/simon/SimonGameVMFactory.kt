package com.example.simon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimonGameVMFactory(private val dao:SimonGameDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SimonGameVM::class.java)) {
            return SimonGameVM(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model")
    }
}