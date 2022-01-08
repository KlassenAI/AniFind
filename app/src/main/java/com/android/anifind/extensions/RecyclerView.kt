package com.android.anifind.extensions

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.domain.model.Anime
import com.android.anifind.presentation.adapter.LoadStateAdapter

fun <VH : RecyclerView.ViewHolder> RecyclerView.init(paramAdapter: PagingDataAdapter<Anime, VH>) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter.withLoadStateHeaderAndFooter(
        LoadStateAdapter { paramAdapter.retry() }, LoadStateAdapter { paramAdapter.retry() }
    )
}

fun <VH : RecyclerView.ViewHolder> RecyclerView.init(
    paramAdapter: PagingDataAdapter<Anime, VH>,
    loadingView: View,
    notLoadingView: View,
    errorView: View
) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter.withLoadStateHeaderAndFooter(
        LoadStateAdapter { paramAdapter.retry() }, LoadStateAdapter { paramAdapter.retry() }
    )
    paramAdapter.addLoadStateListener {
        loadingView.isVisible =  it.source.refresh is LoadState.Loading
        notLoadingView.isVisible =  it.source.refresh is LoadState.NotLoading
        errorView.isVisible = it.source.refresh is LoadState.Error
    }
}

fun <VH : RecyclerView.ViewHolder> RecyclerView.init(paramAdapter: RecyclerView.Adapter<VH>) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter
}
