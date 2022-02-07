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
import com.android.anifind.presentation.adapter.SearchAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchFragment : SubOverviewFragment(R.layout.fragment_search) {

    private val searchAdapter = SearchAdapter()
    private val viewModel: OverviewViewModel by activityViewModels()
    private val binding: FragmentSearchBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        initObservers()
        initEditText()
        initButtons()
    }

    private fun initObservers() {
        viewModel.recentRequests.observe(viewLifecycleOwner) { searchAdapter.submitList(it) }
        viewModel.searchAnimes.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun initRecyclers() = with(binding) {
        recyclerAnimes.init(adapter, progressBar, errorMessage, emptyMessage)
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
            if (i != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false
            viewModel.saveRequest(textView.text.toString())
            recyclerAnimes.run { post { smoothScrollToPosition(0) } }
            false
        }
    }

    private fun initButtons() = with(binding) {
        btnBack.setOnClickListener { navigateUp() }
        btnClear.setOnClickListener { viewModel.clearRecentRequests() }
        btnRetry.setOnClickListener { adapter.retry() }
        btnReset.setOnClickListener {
            searchEditText.setText("")
            layoutSearches.isVisible = true
            layoutAnimes.isVisible = false
            btnReset.isVisible = false
        }
    }
}