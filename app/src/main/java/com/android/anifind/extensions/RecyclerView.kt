package com.android.anifind.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.adapter.RequestAdapter

fun <VH : RecyclerView.ViewHolder> RecyclerView.init(animeAdapter: RecyclerView.Adapter<VH>) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = animeAdapter
}
