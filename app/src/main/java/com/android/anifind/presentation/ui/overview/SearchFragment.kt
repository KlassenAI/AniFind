package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.Constants.DEBOUNCE_TIMEOUT
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSearchBinding
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateUp
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.SearchAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : BaseOverviewFragment(R.layout.fragment_search) {

    private val searchAdapter = SearchAdapter()
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: OverviewViewModel by activityViewModels()
    private lateinit var animeAdapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animeAdapter = AnimePagingAdapter(viewModel, this)
        initRecyclers()
        initObservers()
        initEditText()
        initButtons()
    }

    private fun initObservers() {
        viewModel.searchAnimes.observe(viewLifecycleOwner,
            { animeAdapter.submitData(lifecycle, it) }
        )
        viewModel.recentRequests.observe(viewLifecycleOwner,
            { searchAdapter.searches = it }
        )
    }

    private fun initRecyclers() = with(binding) {
        recyclerAnimes.init(animeAdapter, progressBar, errorMessage, emptyMessage)
        recyclerRequests.init(searchAdapter) { searchEditText.setText(it) }
    }

    private fun initEditText() = with(binding) {
        searchEditText.textChanges()
            .map { it.trim().toString() }
            .doOnNext {
                layoutSearches.isVisible = it.isEmpty()
                layoutAnimes.isVisible = it.isNotEmpty()
                btnReset.isVisible = it.isNotEmpty()
            }
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.requestAnimes(it) }
        searchEditText.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.saveRequest(textView.text.toString())
                recyclerAnimes.run { post { smoothScrollToPosition(0) } }
            }
            false
        }
    }

    private fun initButtons() = with(binding) {
        btnBack.setOnClickListener { navigateUp() }
        btnClear.setOnClickListener { viewModel.clearRecentRequests() }
        btnRetry.setOnClickListener { animeAdapter.retry() }
        btnReset.setOnClickListener {
            searchEditText.setText("")
            layoutSearches.isVisible = true
            layoutAnimes.isVisible = false
            btnReset.isVisible = false
        }
    }
}