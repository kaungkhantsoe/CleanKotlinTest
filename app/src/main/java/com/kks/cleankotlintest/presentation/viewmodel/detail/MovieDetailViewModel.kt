package com.kks.cleankotlintest.presentation.viewmodel.detail

import androidx.lifecycle.ViewModel
import com.kks.cleankotlintest.core.interactor.Interactors
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.model.toPresentationModel

/**
 * Created by kaungkhantsoe on 20/05/2021.
 **/
class MovieDetailViewModel(
    private val interactors: Interactors
) : ViewModel()
{
    fun getDetail(id: Int): MovieVO? =
        interactors.getMovie(id)?.toPresentationModel()

}