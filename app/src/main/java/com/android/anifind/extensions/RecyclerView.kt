package com.android.anifind.extensions

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.presentation.adapter.AnimeAdapter
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.LoadStateAdapter
import com.android.anifind.presentation.adapter.SearchAdapter

fun RecyclerView.init(
    paramAdapter: AnimePagingAdapter,
    loadingView: View,
    errorView: View,
    emptyView: View? = null,
) {
    layoutManager = LinearLayoutManager(context)
    itemAnimator = null
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter.withLoadStateHeaderAndFooter(
        LoadStateAdapter { paramAdapter.retry() }, LoadStateAdapter { paramAdapter.retry() }
    )
    paramAdapter.addLoadStateListener { loadState ->
        loadingView.isVisible = loadState.source.refresh is LoadState.Loading
        this.isVisible = loadState.source.refresh is LoadState.NotLoading
        errorView.isVisible = loadState.source.refresh is LoadState.Error

        emptyView?.let {
            if (isListEmpty(loadState, paramAdapter)) {
                this.isVisible = false
                emptyView.isVisible = true
            } else {
                emptyView.isVisible = false
            }
        }
    }
}

private fun isListEmpty(loadState: CombinedLoadStates, paramAdapter: AnimePagingAdapter): Boolean {
    return loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && paramAdapter.itemCount < 1
}

fun RecyclerView.init(paramAdapter: SearchAdapter, onItemClick: ((String) -> Unit)) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter
    paramAdapter.onItemClick = onItemClick
}

fun RecyclerView.init(paramAdapter: AnimeAdapter) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter
}