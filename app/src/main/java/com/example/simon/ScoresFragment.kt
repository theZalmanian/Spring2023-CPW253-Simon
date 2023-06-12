package com.example.simon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.lifecycleScope
import com.example.simon.databinding.FragmentScoresBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScoresFragment : Fragment() {
    private lateinit var binding: FragmentScoresBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout using view binding
        binding = FragmentScoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get access to the SimonGameDao from the database instance
        val simonGameDao = SimonGameDatabase.getInstance(requireContext()).simonGameDao

        // get the highest score, and observe the DB for changes
        simonGameDao.getHighestScore().observe(viewLifecycleOwner) { highestScore ->
            highestScore.let {
                // if the user has not played any games yet
                if (highestScore == null) {
                    // tell them to come back after they do
                    binding.txtHighScoreDisplay.text = getString(R.string.come_back_later)

                    // disable the "Clear Game History" button, as there is no history to clear
                    binding.btnClearGameHistory.isEnabled = false
                }
                else {
                    // display the highest recorded score on the view
                    binding.txtHighScoreDisplay.text = getString(R.string.highest_score, highestScore)
                }
            }
        }

        // get the ListView used to display the data for each game
        val lstScoresDisplay: ListView = binding.lstScoresDisplay

        // get all the games stored in the DB
        simonGameDao.getAllGames().observe(viewLifecycleOwner) { gameDataList ->
            // display them all on the "Scores Display" ListView
            lstScoresDisplay.adapter = ScoresAdapter(requireContext(), gameDataList)
        }

        // setup the onclick event for the "Clear Game History" button
        binding.btnClearGameHistory.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    // when clicked, clear the SimonGame table of all entries,
                    // and reset the gameID sequence back to 1
                    simonGameDao.clearGamesTable()
                    simonGameDao.resetSequence()
                }
            }
        }
    }
}
