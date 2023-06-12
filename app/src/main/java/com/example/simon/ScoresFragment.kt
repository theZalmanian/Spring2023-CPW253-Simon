package com.example.simon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.simon.databinding.FragmentScoresBinding

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

        // get the ListView used to display the data for each game
        val lstScoresDisplay: ListView = binding.lstScoresDisplay

        // get all the games stored in the DB
        simonGameDao.getAllGames().observe(viewLifecycleOwner) { gameDataList ->
            // display them all on the "Scores Display" ListView
            lstScoresDisplay.adapter = ScoresAdapter(requireContext(), gameDataList)
        }
    }
}
