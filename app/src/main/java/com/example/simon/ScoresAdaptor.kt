package com.example.simon

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ScoresAdapter(context: Context, gameDataList: List<SimonGame>)
    : ArrayAdapter<SimonGame>(context, 0, gameDataList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(
                R.layout.item_score, parent, false
            )
        }

        // Get the current game item from the gameDataList
        val gameItem = getItem(position)

        // Get the TextViews for the game data in the item_score layout
        val txtGameId = itemView?.findViewById<TextView>(R.id.txtGameId)
        val txtGameDate = itemView?.findViewById<TextView>(R.id.txtGameDate)
        val txtGameScore = itemView?.findViewById<TextView>(R.id.txtGameScore)

        // Set the text values for the TextViews
        txtGameId?.text = context.getString(R.string.game_count_display, gameItem?.gameID)
        txtGameDate?.text = gameItem?.gameDate
        txtGameScore?.text = context.getString(R.string.game_end_score_display, gameItem?.gameScore)

        // return the modified view, now containing game data
        return itemView!!
    }
}

