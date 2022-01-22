package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.presentation.viewmodel.BaseViewModel

class AnimeAdapter(
    private val viewModel: BaseViewModel
) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    var animes: List<AnimeEntity> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = animes.size

    override fun onBindViewHolder(holder: ViewHolder, p: Int) = holder.bind(animes[p])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: AnimeEntity) = with(binding) {
            poster.setImage(entity.imageUrl)
            name.text = entity.name
            date.text = entity.date
            episodes.hide()
            score.text = entity.score
        }
    }
}
