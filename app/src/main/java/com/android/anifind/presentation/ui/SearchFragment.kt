package com.android.anifind.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.Constants.DEBOUNCE_TIMEOUT
import com.android.anifind.databinding.FragmentSearchBinding
import com.android.anifind.extensions.init
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: OverviewViewModel by activityViewModels()
    private val adapter = AnimePagingAdapter()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.init(adapter)

        searchViewModel.animes.observe(viewLifecycleOwner, {
            it?.subscribe { pagingData -> adapter.submitData(lifecycle, pagingData) }
        })

        binding.editText.textChanges()
            .map { it.trim() }
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() && it.length > 2 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query -> searchViewModel.searchAnimes(query.toString()) }
    }
}
