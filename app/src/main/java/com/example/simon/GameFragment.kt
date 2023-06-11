package com.example.simon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import java.util.*
import kotlin.concurrent.schedule
import kotlin.collections.ArrayList

class GameFragment : Fragment() {
    /**
     * An array containing all the "game" buttons
     */
    private lateinit var gameButtons: Array<SimonGameButton>

    /**
     * An array containing all the buttons used in the current pattern
     */
    private lateinit var gamePattern: ArrayList<SimonGameButton>

    /**
     * A queue containing a copy of the current pattern
     */
    private lateinit var gamePatternCopy: LinkedList<SimonGameButton>

    /**
     * Keeps track of the current view
     */
    private lateinit var currView:View

    /**
     * Keeps track of the current game's score, is 0 at start of game
     */
    private var gameScore: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // grab the "Scores" button on the Game Fragment
        val btnScores = view.findViewById<Button>(R.id.btnScoresGameFragment)

        // setup it's onclick to take you to the Scores Fragment
        btnScores.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_gameFragment_to_scoresFragment)
        }

        // setup the current view
        currView = view

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup the "game" buttons array
        gameButtons = arrayOf(
            SimonGameButton(view.findViewById(R.id.btnGreen),
                            getColor(requireContext(), R.color.simon_green)),
            SimonGameButton(view.findViewById(R.id.btnRed),
                            getColor(requireContext(), R.color.simon_red)),
            SimonGameButton(view.findViewById(R.id.btnYellow),
                            getColor(requireContext(), R.color.simon_yellow)),
            SimonGameButton(view.findViewById(R.id.btnBlue),
                            getColor(requireContext(), R.color.simon_blue)),
        )

        // setup the game pattern ArrayList
        gamePattern = arrayListOf()

        // setup the game pattern copy Queue
        gamePatternCopy = LinkedList<SimonGameButton>()

        // begin the game after a 1 second delay
        Timer().schedule(1000) {
            startGame()
        }
    }

    /**
     * Starts the game when the fragment is opened,
     * or the "Play Again" button is clicked
     */
    private fun startGame() {
        startNextTurn()
        startNextTurn()
    }

    /**
     * Begins the next turn by adding a new color to the pattern,
     * and then displaying the new pattern to the user
     */
    private fun startNextTurn() {
        // generate the next color and add it to the pattern
        generateNextColor()

        // copy the current pattern over to the copy Queue
        copyPatternToQueue()

        // display the pattern to the user, now containing another color
        displayPattern()
    }

    /**
     * When called generates a random num between 0 and 3,
     * and then sets the button at that index in the "game buttons" array list
     * as the next button/color in the pattern
     */
    private fun generateNextColor() {
        // setup Random
        val rand = Random()

        // generate a random num between 0 and 3
        val index = rand.nextInt(4)

        // get the "game" button at that index in the game buttons array
        val btnNextColor:SimonGameButton = gameButtons[index]

        // add that button to the pattern array
        gamePattern.add(btnNextColor)
    }

    /**
     * Copies over all the contents of the "Game Pattern" ArrayList to the copy Queue
     */
    private fun copyPatternToQueue() {
        // run through the pattern ArrayList
        for(currBtn:SimonGameButton in gamePattern) {
            // add the current button to the copy queue
            gamePatternCopy.add(currBtn)
        }
    }

    /**
     * When called runs through the "Game Pattern" ArrayList,
     * and displays the current color pattern to the user
     */
    private fun displayPattern() {
        // run through the pattern array
        for ((index, currBtn) in gamePattern.withIndex()) {
            // Display the current color to the user with a 2s delay
            Timer().schedule((index + 1) * 2000L) {
                displayCurrColor(currBtn)
            }
        }
    }

    /**
     * Displays the current color to the user by changing that button's color to black,
     * and then resetting it back to norma;
     */
    private fun displayCurrColor(currBtnInPattern:SimonGameButton) {
        // get the "Game" button on the view corresponding to the current button in the pattern
        val btnOnView: Button = currView.findViewById(currBtnInPattern.getButton().id)

        // set the button's color to black to signify it's position in the pattern
        btnOnView.setBackgroundColor(Color.BLACK)

        // after a 1 second delay
        Timer().schedule(1000L) {
            // reset it's color back to the original
            btnOnView.setBackgroundColor(currBtnInPattern.getColor())
        }
    }
}