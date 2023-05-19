package com.example.simon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class WelcomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        // grab the "Play Game" button
        val btnPlayGame = view.findViewById<Button>(R.id.btnPlayGame)

        // setup it's onclick to take you to the Game Fragment
        btnPlayGame.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_gameFragment)
        }

        // grab the "Scores" button on the Welcome Fragment
        val btnScores = view.findViewById<Button>(R.id.btnScoresWelcomeFragment)

        // setup it's onclick to take you to the Scores Fragment
        btnScores.setOnClickListener {
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_scoresFragment)
        }

        return view
    }
}