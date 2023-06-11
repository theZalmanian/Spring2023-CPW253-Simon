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
import com.example.simon.databinding.FragmentGameBinding

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
     * Keeps track of the current game's score, is 0 at start of game
     */
    private var gameScore: Int = 0

    // setup view-binding for this fragment
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // setup view-binding for this fragment
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val view = binding.root

        // setup it's onclick to take you to the Scores Fragment
        binding.btnScoresGameFragment.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_gameFragment_to_scoresFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup the "game" buttons array
        gameButtons = arrayOf(
            SimonGameButton(binding.btnGreen, getColor(requireContext(), R.color.simon_green)),
            SimonGameButton(binding.btnRed, getColor(requireContext(), R.color.simon_red)),
            SimonGameButton(binding.btnYellow, getColor(requireContext(), R.color.simon_yellow)),
            SimonGameButton(binding.btnBlue, getColor(requireContext(), R.color.simon_blue)),
        )

        // setup the onclick event for all four "game" buttons
        for(currSimonButton:SimonGameButton in gameButtons) {
            setupGameButtonOnClick(currSimonButton.getButton());
        }

        // setup the game pattern ArrayList
        gamePattern = arrayListOf()

        // setup the game pattern copy Queue
        gamePatternCopy = LinkedList<SimonGameButton>()

        // begin the game after a .5 second delay
        Timer().schedule(500L) {
            startGame()
        }
    }

    private fun setupGameButtonOnClick(currButton:Button) {
        currButton.setOnClickListener() {
            // if the copy queue is not empty
            if(!gamePatternCopy.isEmpty()) {
                // get the next button in the pattern from the copy queue
                val nextColor = gamePatternCopy.peek()

                // if the current button is the same as the next button in the pattern
                if(currButton.id == nextColor.getButton().id) {
                    // remove that button from the pattern
                    gamePatternCopy.remove()

                    // if the game copy queue is now empty, start the next round
                    if(gamePatternCopy.isEmpty()) {
                        startNextTurn()
                    }
                }

                // if the buttons id's do not match, the user input the patten incorrectly
                else {
                    endGame()
                }
            }

            // if the copy queue is empty, the user input the pattern correctly
            else {
                startNextTurn()
            }

        }
    }

    /**
     * Starts the game when the fragment is opened,
     * or the "Play Again" button is clicked
     */
    private fun startGame() {
        // reset the game score to 0
        gameScore = 0

        // start the game by generating the first color
        startNextTurn()
    }

    private fun endGame() {
        // display the game over message
        binding.txtGameText.text = "You lost the game after " + gameScore + " rounds";

        // enable the "play again" button
        binding.btnPlayAgain.isEnabled = true
    }

    /**
     * Begins the next turn by adding a new color to the pattern,
     * and then displaying the new pattern to the user
     */
    private fun startNextTurn() {
        // a new round has begun
        gameScore++

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
     * as the next button (color) in the pattern
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
        // clear the copy queue
        gamePatternCopy.clear()

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
     * and then resetting it back to normal
     */
    private fun displayCurrColor(currBtnInPattern:SimonGameButton) {
        // get the "Game" button on the view corresponding to the current button in the pattern
        val btnOnView: Button = binding.root.findViewById(currBtnInPattern.getButton().id)

        // set the button's color to black to signify it's position in the pattern
        btnOnView.setBackgroundColor(Color.BLACK)

        // after a 1 second delay
        Timer().schedule(1000L) {
            // reset it's color back to the original
            btnOnView.setBackgroundColor(currBtnInPattern.getColor())
        }
    }
}