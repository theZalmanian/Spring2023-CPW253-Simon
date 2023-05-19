package com.example.simon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

class GameFragment : Fragment() {
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

        return view
    }
}