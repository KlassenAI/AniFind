package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.anifind.Constants.DEBOUNCE_TIMEOUT
import com.android.anifind.databinding.FragmentSearchBinding
import com.android.anifind.extensions.init
import com.android.anifind.extensions.navigateUp
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.RequestAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: OverviewViewModel by activityViewModels()
    private val animeAdapter = AnimePagingAdapter(DEFAULT)
    private val requestAdapter = RequestAdapter()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {
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
        viewModel.requestedAnimes.observe(viewLifecycleOwner, {
            it?.subscribe { data -> animeAdapter.submitData(lifecycle, data) }
        })
        viewModel.recentRequests.observe(viewLifecycleOwner, {
            requestAdapter.setData(it)
        })
    }

    private fun initRecyclers() {
        binding.recyclerAnimes.init(animeAdapter)
        binding.recyclerRequests.init(requestAdapter)
    }

    private fun initEditText() {
        binding.editText.textChanges()
            .map { it.trim().toString() }
            .doOnNext {
                binding.linearLayoutRequests.isVisible = it.isEmpty()
                binding.recyclerAnimes.isVisible = it.isNotEmpty()
                binding.btnReset.isVisible = it.isNotEmpty()
            }
            .debounce(DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.requestAnimes(it) }

        binding.editText.setOnEditorActionListener { textView, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) viewModel.saveRequest(textView.text.toString())
            false
        }

        requestAdapter.onRequestClick = { binding.editText.setText(it) }
    }

    private fun initButtons() {
        binding.btnBack.setOnClickListener { navigateUp() }
        binding.btnClear.setOnClickListener { viewModel.clearRecentRequests() }
        binding.btnReset.setOnClickListener {
            binding.editText.setText("")
            binding.linearLayoutRequests.isVisible = true
            binding.recyclerAnimes.isVisible = false
            binding.btnReset.isVisible = false
        }
    }
}
