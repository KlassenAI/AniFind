package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.android.anifind.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel()
