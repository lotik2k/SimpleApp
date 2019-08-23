package com.movista.app.domain.searchparams

import com.movista.app.domain.model.SearchModel
import io.reactivex.subjects.PublishSubject


interface ISearchParamsUseCase {

    fun initSearchParams(): SearchModel
    fun observeSearchParamsUpdate(): PublishSubject<SearchModel>
}