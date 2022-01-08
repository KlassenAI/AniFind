package com.android.anifind.presentation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import com.android.anifind.databinding.FragmentLatestBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AdapterType
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.HomeViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class LatestFragment : Fragment() {

    private val adapter = AnimePagingAdapter(DEFAULT)
    private val homeViewModel by activityViewModels<HomeViewModel>()
    private val bookmarksViewModel by activityViewModels<BookmarksViewModel>()
    private lateinit var binding: FragmentLatestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLatestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onItemClick = {
            bookmarksViewModel.setAnime(it)
            findNavController().navigate(R.id.action_fragmentHome_to_animeFragment)
        }
        binding.recycler.init(adapter)
        homeViewModel.latest.observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
    }
}
