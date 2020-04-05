package com.maxk.marvy.model.marvel

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.maxk.marvy.api.NetworkRequestStatus

/**
 * Metadata about a paging request result.
 */
data class PagedMetadata(val itemsFetched: Int)

/**
 * Encapsulates paging request result.
 */
class PagedDataWrapper<T>(val pagedList: LiveData<PagedList<T>>,
                          val pagingRequestStatus: LiveData<NetworkRequestStatus<PagedMetadata>>)