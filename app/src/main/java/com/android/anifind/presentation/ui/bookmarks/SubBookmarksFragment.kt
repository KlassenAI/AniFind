package com.android.anifind.presentation.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSubBookmarksBinding
import com.android.anifind.domain.model.Anime
import com.android.anifind.domain.model.AnimeEntity
import com.android.anifind.extensions.conceal
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateToBookmarksAnime
import com.android.anifind.extensions.showSnackbar
import com.android.anifind.presentation.adapter.AnimeEntityAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.SharedViewModel

abstract class SubBookmarksFragment: Fragment(R.layout.fragment_sub_bookmarks), AnimeEntityAdapter.OnItemClickListener {

    val viewModel: BookmarksViewModel by activityViewModels()
    private val binding: FragmentSubBookmarksBinding by viewBinding()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var entityAdapter: AnimeEntityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entityAdapter = AnimeEntityAdapter(viewModel, this)
        binding.recycler.init(entityAdapter)
        initData()
    }

    abstract fun initData()

    fun LiveData<List<AnimeEntity>>.observe() = with(binding) {
        observe(viewLifecycleOwner) {
            entityAdapter.submitList(it)
            progressBar.conceal()
            emptyMessage.isVisible = it.isEmpty()
            recycler.isVisible = it.isNotEmpty()
        }
    }

    override fun notifyItemClicked(animeEntity: AnimeEntity) {
        sharedViewModel.initBookmarksAnime(animeEntity)
        navigateToBookmarksAnime()
    }

    override fun notifyShowSnackbar(text: String, action: () -> Unit) = showSnackbar(text, action)
}