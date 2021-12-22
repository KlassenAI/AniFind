package com.android.anifind.domain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime

class AnimeAdapter(
    private var animes: List<Anime>
) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anime: Anime = animes[position]
        holder.bind(anime)
    }

    override fun getItemCount(): Int = animes.size

    fun setListAnime(list: List<Anime>) {
        animes = list
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime) {
            binding.nameOriginal.text = anime.name
            binding.nameRussian.text = anime.russian
        }
    }
}