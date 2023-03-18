package com.pinguapps.chesstrainer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinguapps.chesstrainer.databinding.RowLayoutBotBinding
import com.pinguapps.chesstrainer.logic.bots.Bot

class BotRecyclerAdapter(
    private val onItemClicked: (Bot) -> Unit,
): RecyclerView.Adapter<BotRecyclerAdapter.ViewHolder>(

) {
    var bots = arrayListOf<Bot>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowLayoutBotBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bot = bots[position]

        holder.name.text = bot.name
        holder.description.text = bot.description


    }
    inner class ViewHolder(binding: RowLayoutBotBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        private var view: View = binding.root
        val name = binding.botName
        val description = binding.botDescription


        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
        }
    }

    override fun getItemCount(): Int {
        return bots.size
    }

}