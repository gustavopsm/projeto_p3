package com.example.p3_project.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p3_project.R
import com.example.p3_project.data.entities.Torneio

class TorneioAdapter(
    private var torneios: List<Torneio>
) : RecyclerView.Adapter<TorneioAdapter.TorneioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorneioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_torneio, parent, false)
        return TorneioViewHolder(view)
    }

    override fun onBindViewHolder(holder: TorneioViewHolder, position: Int) {
        holder.bind(torneios[position])
    }

    override fun getItemCount(): Int = torneios.size

    fun updateList(newList: List<Torneio>) {
        torneios = newList
        notifyDataSetChanged()
    }

    inner class TorneioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeTorneio: TextView = itemView.findViewById(R.id.textViewNomeTorneio)

        fun bind(torneio: Torneio) {
            nomeTorneio.text = torneio.nome
        }
    }
}
