package com.kks.cleankotlintest.presentation.viewmodel.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.cleankotlintest.core.interactor.Interactors
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.model.toDomainModel
import com.kks.cleankotlintest.presentation.model.toPresentationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by kaungkhantsoe on 20/05/2021.
 **/
class MovieDetailViewModel(
    private val interactors: Interactors
) : ViewModel()
{
    fun getDetail(id: Int): MovieVO? =
        interactors.getMovie(id)?.toPresentationModel()

    fun changeLike(movieVO: MovieVO): Int {
        var position = -1
        viewModelScope.launch(Dispatchers.IO) {
            position = interactors.changeLikeMovie(movieVO.toDomainModel())
        }

        return position
    }

}