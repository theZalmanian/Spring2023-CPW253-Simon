package com.example.simon

import android.widget.Button

/**
 * An object consisting of a button and a color for use in the "Simon" game
 */
class SimonGameButton(private var button: Button, private var color: Int) {
    /**
     * Gets and returns the button contained in the SimonGameButton
     */
    fun getButton():Button {
        return button
    }

    /**
     * Gets and returns the color belonging to that SimonGameButton
     */
    fun getColor():Int {
        return color
    }
}