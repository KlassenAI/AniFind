package com.android.anifind.extensions

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

fun <VH : RecyclerView.ViewHolder> RecyclerView.init(paramAdapter: RecyclerView.Adapter<VH>) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = paramAdapter
}
