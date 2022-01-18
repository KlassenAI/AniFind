package com.android.anifind.presentation.ui.anime

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.R
import com.android.anifind.databinding.FragmentAnimeBinding
import com.android.anifind.presentation.viewmodel.BaseViewModel

class AnimeFragment : Fragment(R.layout.fragment_anime) {

    private val viewModel: BaseViewModel by activityViewModels()
    private val binding: FragmentAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.anime.observe(viewLifecycleOwner) { Log.d("anime", it.toString()) }
    }
}