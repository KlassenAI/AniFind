package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.LoadStateFooterBinding
import com.android.anifind.presentation.adapter.LoadStateAdapter.ViewHolder

class LoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) = holder.bind(loadState)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = ViewHolder(
        LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(
        private val binding: LoadStateFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) = with(binding) {
            progressBar.isVisible = state is LoadState.Loading
            textView.isVisible = state !is LoadState.Loading
            btnRetry.isVisible = state !is LoadState.Loading
            btnRetry.setOnClickListener { retry.invoke() }
        }
    }
}