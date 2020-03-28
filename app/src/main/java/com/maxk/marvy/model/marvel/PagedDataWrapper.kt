package com.maxk.marvy.model.marvel

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.maxk.marvy.api.NetworkRequestStatus

class PagedDataWrapper<T>(val pagedList: LiveData<PagedList<T>>,
                          val pagingRequestStatus: LiveData<NetworkRequestStatus<Unit>>)