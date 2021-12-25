package com.android.anifind.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.anifind.Constants.DEBOUNCE_TIMEOUT
import com.android.anifind.databinding.FragmentSearchBinding
import com.android.anifind.domain.AnimeAdapter
import com.android.anifind.presentation.viewmodel.SearchViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()
    private val animeAdapter: AnimeAdapter = AnimeAdapter(listOf())
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.animes.observe(viewLifecycleOwner, {
            animeAdapter.setListAnime(it)
        })

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), VERTICAL))
            adapter = animeAdapter
        }

        compositeDisposable.add(binding.editText.textChanges()
            .map { it.trim() }
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { searchViewModel.searchAnimes(it.toString()) })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
