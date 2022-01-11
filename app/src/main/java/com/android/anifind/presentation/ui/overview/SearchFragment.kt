package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.anifind.Constants.DEBOUNCE_TIMEOUT
import com.android.anifind.R
import com.android.anifind.databinding.FragmentSearchBinding
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateUp
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.RequestAdapter
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val bookmarksViewModel: BookmarksViewModel by activityViewModels()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private val animeAdapter = AnimePagingAdapter(DEFAULT)
    private val requestAdapter = RequestAdapter()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclers()
        initObservers()
        initEditText()
        initButtons()
    }

    private fun initObservers() {
        overviewViewModel.searchAnimes.observe(viewLifecycleOwner,
            { animeAdapter.submitData(lifecycle, it) }
        )
        overviewViewModel.recentRequests.observe(viewLifecycleOwner, { requestAdapter.setData(it) })
    }

    private fun initRecyclers() {
        binding.apply {
            recyclerAnimes.init(animeAdapter, progressBar, errorMessage, emptyMessage) {
                bookmarksViewModel.setAnime(it)
                findNavController().navigate(R.id.animeFragment)
            }
            recyclerRequests.init(requestAdapter) { searchEditText.setText(it) }
        }
    }

    private fun initEditText() {
        binding.apply {
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
                .subscribe { overviewViewModel.requestAnimes(it) }
            searchEditText.setOnEditorActionListener { textView, i, _ ->
                if (i == EditorInfo.IME_ACTION_DONE) {
                    overviewViewModel.saveRequest(textView.text.toString())
                    binding.recyclerAnimes.run { post { smoothScrollToPosition(0) } }
                }
                false
            }
        }
    }

    private fun initButtons() {
        binding.apply {
            btnBack.setOnClickListener { navigateUp() }
            btnClear.setOnClickListener { overviewViewModel.clearRecentRequests() }
            btnReset.setOnClickListener {
                searchEditText.setText("")
                layoutSearches.isVisible = true
                layoutAnimes.isVisible = false
                btnReset.isVisible = false
            }
            btnRetry.setOnClickListener { animeAdapter.retry() }
        }
    }
}
