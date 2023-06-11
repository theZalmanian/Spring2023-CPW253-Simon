package com.example.simon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import java.util.*
import kotlin.concurrent.schedule
import kotlin.collections.ArrayList
import com.example.simon.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    /**
     * An array containing all four "Simon" game buttons
     */
    private lateinit var simonButtons: Array<SimonGameButton>

    /**
     * An array-list containing all the buttons used in the current pattern
     */
    private var gamePattern: ArrayList<SimonGameButton> = arrayListOf()

    /**
     * A queue containing a copy of the current pattern
     */
    private var gamePatternCopy: LinkedList<SimonGameButton> = LinkedList<SimonGameButton>()

    /**
     * Keeps track of the current game's score, is 0 at start of game
     */
    private var currGameScore: Int = 0

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // setup view-binding for this fragment
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        // setup the "Scores" button's onclick to take you to the Scores Fragment
        binding.btnScoresGameFragment.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_gameFragment_to_scoresFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup the "Simon" game buttons array
        simonButtons = arrayOf(
            SimonGameButton(binding.btnGreen, requireContext().getColor(R.color.simon_green)),
            SimonGameButton(binding.btnRed, requireContext().getColor(R.color.simon_red)),
            SimonGameButton(binding.btnYellow, requireContext().getColor(R.color.simon_yellow)),
            SimonGameButton(binding.btnBlue, requireContext().getColor(R.color.simon_blue)),
        )

        // setup the onclick event for all four "Simon" game buttons
        for(currSimonButton in simonButtons) {
            setupSimonButtonOnClick(currSimonButton.getButton())
        }

        // setup the onclick event for the "Play Again" button
        binding.btnPlayAgain.setOnClickListener {
            startGame()
        }

        startGame()
    }

    /**
     * Sets up the on-click event for all four "Simon" buttons so the following occurs:
     * - If the button clicked matches the next color in the pattern, that color is removed from the gamePatternCopy
     *      - If that was the last color in the pattern, the next round begins
     * - Otherwise, the user input the wrong color and the game ends
     * @param currButton The "Simon" game button being setup
     */
    private fun setupSimonButtonOnClick(currButton:Button) {
        currButton.setOnClickListener {
            // get the next color in the pattern from the gamePatternCopy queue
            val nextColor = gamePatternCopy.peek()

            // if the current button's color matches the next color in the pattern
            if(currButton.id == nextColor!!.getButton().id) {
                // remove that color from the gamePatternCopy queue
                gamePatternCopy.remove()

                // if the gamePatternCopy queue is now empty
                if(gamePatternCopy.isEmpty()) {
                    // increment and display the new score, then start the next round
                    setAndDisplayScore(currGameScore + 1)
                    startNextRound()
                }
            }

            // otherwise, the user input the patten incorrectly
            else {
                endGame()
            }
        }
    }

    /**
     * Starts the "Simon" game when this fragment is opened,
     * or the "Play Again" button is clicked
     */
    private fun startGame() {
        // disable the "Play Again" button
        binding.btnPlayAgain.isEnabled = false

        // reset the game score to 0 and display it to the user
        setAndDisplayScore(0)

        // start the game by generating the first color
        startNextRound()
    }

    /**
     * Ends the "Simon" game when the player chooses an incorrect color
     * while attempting to recreate the game pattern
     */
    private fun endGame() {
        // reset the game pattern
        gamePattern.clear()

        // display the game over message in the gameText textView
        binding.txtGameText.text = getString(R.string.game_over_message, currGameScore)

        // enable the "Play Again" button
        binding.btnPlayAgain.isEnabled = true
    }

    /**
     * Starts the next round by adding a new color to the pattern,
     * and then displaying the new pattern to the user
     */
    private fun startNextRound() {
        // generate the next color
        val btnNextColor:SimonGameButton = generateNextColor()

        // add that button to the pattern array
        gamePattern.add(btnNextColor)

        // copy the current pattern over to the copy Queue
        copyGamePattern()

        // display the pattern to the user, now containing a new color
        displayGamePattern()
    }

    /**
     * Changes the game's score to the given value and displays to the user
     * @param newGameScore The new game score
     */
    private fun setAndDisplayScore(newGameScore:Int) {
        // set the game score to the given value
        currGameScore = newGameScore

        // get the "Game Text" textview, and display the current score
        binding.txtGameText.text = getString(R.string.current_score, currGameScore)
    }

    /**
     * Generates a random number between 0 and 3
     * @return SimonGameButton - the "Simon" game button located at that
     * index in the simonButtons array
     */
    private fun generateNextColor():SimonGameButton {
        // setup Random
        val rand = Random()

        // generate a random number between 0 and 3
        val index = rand.nextInt(4)

        // return the "Simon" game button at that index in the simonButtons array
        return simonButtons[index]
    }

    /**
     * Copies over all the contents of the gamePattern ArrayList to the gamePatternCopy Queue
     */
    private fun copyGamePattern() {
        // clear the copy queue
        gamePatternCopy.clear()

        // run through the pattern ArrayList
        for(currBtn:SimonGameButton in gamePattern) {
            // add the current button to the copy queue
            gamePatternCopy.add(currBtn)
        }
    }

    /**
     * Runs through the gamePattern ArrayList and displays the current round's pattern to the user
     */
    private fun displayGamePattern() {
        // setup Timer
        val timer = Timer()

        // run through the gamePattern ArrayList
        for ((index, currBtn) in gamePattern.withIndex()) {
            // display the current color in the pattern, with a 1.5s delay between each color
            timer.schedule((index + 1) * 1500L) {
                displayNextColor(currBtn)
            }
        }
    }

    /**
     * Displays the next color in the pattern to the user by changing the button's color
     * to black for 1 second and then setting it back to normal
     * @param nextColorInPattern The next colored button in the game pattern
     */
    private fun displayNextColor(nextColorInPattern:SimonGameButton) {
        // get the "Simon" game button on the view corresponding to the current button in the pattern
        val btnNextColorID:Int = nextColorInPattern.getButton().id
        val currBtnOnView:Button = binding.root.findViewById(btnNextColorID)

        // change that button's color to black to signify it's position in the pattern
        currBtnOnView.setBackgroundColor(Color.BLACK)

        // after a 1 second delay
        Timer().schedule(1000L) {
            // reset it's color back to the original
            currBtnOnView.setBackgroundColor(nextColorInPattern.getColor())
        }
    }
}