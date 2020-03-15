package com.maxk.marvy.model.marvel

import androidx.paging.PagedList

data class DataContainer<T> (
    val offset: Int?,
    val total: Int?,
    val results: List<T>
)