package com.android.anifind.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.R
import com.android.anifind.databinding.ItemAnimeBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.WatchStatus
import com.android.anifind.domain.model.WatchStatus.*
import com.android.anifind.extensions.hide
import com.android.anifind.extensions.setImage
import com.android.anifind.presentation.viewmodel.BaseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AnimeAdapter(
    private val viewModel: BaseViewModel
) : RecyclerView.Adapter<AnimeAdapter.ViewHolder>() {

    var animes: List<Anime> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = animes.size

    override fun onBindViewHolder(holder: ViewHolder, p: Int) = holder.bind(animes[p], p)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(anime: Anime, position: Int) {
           with(binding) {
                if (anime.isFavorite == true) {
                    binding.btnFavorite.setDraw(R.drawable.ic_bookmark_filled)
                } else {
                    binding.btnFavorite.setDraw(R.drawable.ic_bookmark_border)
                }

                when (anime.watchStatus) {
                    WATCHING -> btnStatus.setDraw(R.drawable.ic_time)
                    PLANNED -> btnStatus.setDraw(R.drawable.ic_calendar_item)
                    COMPLETED -> btnStatus.setDraw(R.drawable.ic_done)
                    DROPPED -> btnStatus.setDraw(R.drawable.ic_delete)
                    HOLD -> btnStatus.setDraw(R.drawable.ic_pause)
                    null -> btnStatus.setDraw(R.drawable.ic_add)
                }

                poster.setImage(anime.image.original)
                name.setText(anime.russian, anime.name)
                date.setYear(anime.airedOn?.getYear())
                episodes.hide()
                rate.setDouble(anime.score)

                itemView.setOnClickListener {
                    viewModel.saveAnime(anime)
                    //itemView.findNavController().navigate(R.id.animeFragment)
                }

                btnFavorite.setOnClickListener {
                    if (anime.watchStatus != null) {
                        animes[position].isFavorite = anime.isFavorite
                        viewModel.update(anime)
                    } else {
                        if (anime.isFavorite == true) {
                            animes[position].isFavorite = false
                            viewModel.delete(anime)
                        } else {
                            animes[position].isFavorite = true
                            viewModel.insert(anime)
                        }
                    }
                    notifyItemChanged(position)
                }

                btnStatus.setOnClickListener {
                    val checkedItem = anime.watchStatus?.ordinal?.plus(1) ?: 0
                    MaterialAlertDialogBuilder(itemView.context)
                        .setTitle("Статус просмотра")
                        .setSingleChoiceItems(WatchStatus.titles(), checkedItem) { dialog, _ ->
                            Log.d("action", "msg")
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}
