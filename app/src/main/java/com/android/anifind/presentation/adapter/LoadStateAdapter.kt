package com.android.anifind.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.databinding.LoadStateFooterBinding
import com.android.anifind.presentation.adapter.LoadStateAdapter.LoadStateViewHolder

class LoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(
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