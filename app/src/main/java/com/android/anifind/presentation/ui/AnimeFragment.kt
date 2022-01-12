package com.android.anifind.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.databinding.FragmentAnimeBinding
import com.android.anifind.presentation.viewmodel.AnimeViewModel

class AnimeFragment : Fragment() {

    private val viewModel: AnimeViewModel by activityViewModels()
    private lateinit var binding: FragmentAnimeBinding

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        binding = FragmentAnimeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.anime.observe(viewLifecycleOwner) {
            Log.d("anime", it.toString())
        }
    }
}
