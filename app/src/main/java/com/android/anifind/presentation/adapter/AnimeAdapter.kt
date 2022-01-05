package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.Constants.IMAGE_URL
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.bumptech.glide.Glide

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
            binding.apply {
                nameOriginal.text = anime.name
                nameRussian.text = anime.russian
                date.text = anime.airedOn?.substringBefore("-")
                rate.text = anime.score.toString()
                Glide.with(this.anime)
                    .load(getImageUrl(anime.image.original))
                    .centerCrop()
                    .error(R.drawable.error_load)
                    .fallback(R.drawable.error_load)
                    .into(poster)
            }
        }

        private fun getImageUrl(url: String) = IMAGE_URL + url
    }
}