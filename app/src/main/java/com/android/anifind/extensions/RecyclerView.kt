package com.android.anifind.extensions

import android.view.View
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.domain.model.Anime
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.LoadStateAdapter
import com.android.anifind.presentation.adapter.RequestAdapter

fun RecyclerView.init(
    paramAdapter: AnimePagingAdapter,
    loadingView: View,
    errorView: View,
    emptyView: View? = null,
    onItemClick: ((Anime) -> Unit)
) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter.withLoadStateHeaderAndFooter(
        LoadStateAdapter { paramAdapter.retry() }, LoadStateAdapter { paramAdapter.retry() }
    )
    paramAdapter.onItemClick = onItemClick
    paramAdapter.addLoadStateListener { loadState ->
        loadingView.isVisible =  loadState.source.refresh is LoadState.Loading
        this.isVisible =  loadState.source.refresh is LoadState.NotLoading
        errorView.isVisible = loadState.source.refresh is LoadState.Error

        emptyView?.let {
            if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached && paramAdapter.itemCount < 1) {
                this.isVisible = false
                emptyView.isVisible = true
            } else {
                emptyView.isVisible = false
            }
        }
    }
}

fun RecyclerView.init(paramAdapter: RequestAdapter, onItemClick: ((String) -> Unit)) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter
    paramAdapter.onItemClick = onItemClick
}
