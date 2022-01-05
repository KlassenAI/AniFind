package com.android.anifind.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.anifind.presentation.adapter.AnimePagingAdapter

fun RecyclerView.init(animeAdapter: AnimePagingAdapter) {
    layoutManager = LinearLayoutManager(context)
    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    adapter = animeAdapter
}