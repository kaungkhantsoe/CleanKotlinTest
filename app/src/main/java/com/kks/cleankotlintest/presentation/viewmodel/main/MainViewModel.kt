package com.kks.cleankotlintest.presentation.viewmodel.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.cleankotlintest.core.interactor.Interactors
import com.kks.cleankotlintest.common.DataState
import com.kks.cleankotlintest.common.ScreenState
import com.kks.cleankotlintest.constants.NetworkConstant
import com.kks.cleankotlintest.core.domain.MovieListRequest
import com.kks.cleankotlintest.extensions.safeApiCall
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.model.toDomainModel
import com.kks.cleankotlintest.presentation.model.toPresentationModel
import com.kks.cleankotlintest.util.NetworkListener
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.kks.cleankotlintest.core.domain.MovieRequest as DomainMovie

class MainViewModel(
    private val interactors: Interactors,
    private val networkUtil: NetworkListener
) : ViewModel() {

    private lateinit var _screenState: MutableLiveData<ScreenState<DataState<Nothing>>>
    private var _pageNumber: Int = 1
    private var _isRefreshed = false
    private var _loadedMovies = mutableListOf<MovieVO>()

    var pageNumber: Int = _pageNumber
        set(value) {
            _screenState.postValue(
                ScreenState.Loading
            )

            field = value

            _isRefreshed = value == 1 && networkUtil.isNetworkAvailable()

            if (!_isRefreshed) queryMoviesFromDb(value)
            else loadMoviesFromRemote(value)
        }

    val screenState: LiveData<ScreenState<DataState<Nothing>>>
        get() {
            if (!::_screenState.isInitialized) {
                _screenState = MutableLiveData()

                viewModelScope.launch(Dispatchers.IO) {
                    _screenState.postValue(
                        ScreenState.Loading
                    )
                }

                queryMoviesFromDb()
            }

            return _screenState
        }

    private fun queryMoviesFromDb(page: Int = 1) = viewModelScope.launch(Dispatchers.IO) {

        if (page == 1) _loadedMovies.clear()

        val movies = interactors.getMovieFromLocal(page).map(DomainMovie::toPresentationModel)

        if (movies.isNullOrEmpty()) {
            when {
                networkUtil.isNetworkAvailable() -> loadMoviesFromRemote(page)
                page ==1 -> _screenState.postValue(
                    ScreenState.Render(DataState.Error(NetworkConstant.NO_NETWORK_CONNECT))
                )
                else -> _screenState.postValue(
                    ScreenState.Render(DataState.EndReach)
                )
            }
        } else {
            _screenState.postValue(
                ScreenState.Render(DataState.Success(movies))
            )
            _loadedMovies.addAll(movies)
        }
    }

    private fun loadMoviesFromRemote(page: Int = 1) = viewModelScope.launch(Dispatchers.IO) {

        if (page == 1) _loadedMovies.clear()

        try {
            Flowable.just(safeApiCall {
                interactors.getMoviesFromRemote(page)
            })
                .observeOn(Schedulers.io())
                .subscribe {
                    when (it) {
                        is DataState.Error -> {
                            _screenState.postValue(
                                ScreenState.Render(
                                    DataState.Error(it.error)
                                )
                            )
                        }
                        is DataState.Success -> {
                            val data: MovieListRequest = it.data as MovieListRequest
                            data.results?.let { results ->
                                if (results.isEmpty())
                                    _screenState.postValue(
                                        ScreenState.Render(DataState.EndReach)
                                    )
                                else {
                                    interactors.insertMovie(results)
                                    _loadedMovies.addAll(results.map { movie ->
                                        movie.toPresentationModel()
                                    })

                                    _screenState.postValue(
                                        ScreenState.Render(
                                            DataState.Success(
                                                results.map { movie ->
                                                    movie.toPresentationModel()
                                                }
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        is DataState.EndReach -> {
                            _screenState.postValue(
                                ScreenState.Render(DataState.EndReach)
                            )
                        }
                    }
                }

        } catch (e: Exception) {
            _screenState.postValue(
                ScreenState.Render(DataState.Error(NetworkConstant.UNKNOWN_ERROR))
            )
        }
    }

    fun changeLike(movieVO: MovieVO): Int {
        var position = -1
        viewModelScope.launch(Dispatchers.IO) {
            position = interactors.changeLikeMovie(movieVO.toDomainModel())
        }

        return position
    }

    fun getDetail(id: Int): MovieVO? =
        interactors.getMovie(id)?.toPresentationModel()

}