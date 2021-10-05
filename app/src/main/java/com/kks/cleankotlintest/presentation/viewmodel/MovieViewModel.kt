package com.kks.cleankotlintest.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kks.cleankotlintest.core.interactor.Interactors
import com.kks.cleankotlintest.common.DataState
import com.kks.cleankotlintest.common.ScreenState
import com.kks.cleankotlintest.constants.MovieType
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
import timber.log.Timber
import com.kks.cleankotlintest.core.domain.MovieRequest as DomainMovie

class MovieViewModel(
    private val interactors: Interactors,
    private val networkUtil: NetworkListener
) : ViewModel() {

    private val tag = "MovieViewModel"

    private lateinit var _screenState: MutableLiveData<ScreenState<DataState<Nothing>>>
    private var _pageNumber: Int = 1
    private var _isRefreshed = false
    private var _loadedMovies = mutableListOf<MovieVO>()
    var type = MovieType.UPCOMING

    var pageNumber: Int = _pageNumber
        set(value) {
            _screenState.postValue(
                ScreenState.Loading
            )

            field = value

            _isRefreshed = value == 1 && networkUtil.isNetworkAvailable()

            if (!_isRefreshed) {
                Timber.d("$tag Query From db")
                queryMoviesFromDb(value)
            }
            else {
                Timber.d("$tag Load from remote")
                loadMoviesFromRemote(value, type)
            }
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

    private fun queryMoviesFromDb(page: Int = 1) =
        viewModelScope.launch(Dispatchers.IO) {

            if (page == 1) _loadedMovies.clear()

            val movies = interactors.getMovieFromLocal(page, type.value)
                .map(DomainMovie::toPresentationModel)

            Timber.d("$tag loaded from db ${movies.size}")

            if (movies.isNullOrEmpty()) {
                when {
                    networkUtil.isNetworkAvailable() -> loadMoviesFromRemote(page, type)
                    page == 1 -> _screenState.postValue(
                        ScreenState.Render(DataState.Error(NetworkConstant.NO_NETWORK_CONNECT))
                    )
                    else -> _screenState.postValue(
                        ScreenState.Render(DataState.EndReach)
                    )
                }
            } else {
                Timber.d("$tag Loaded from db !!! $movies")
                _screenState.postValue(
                    ScreenState.Render(DataState.Success(movies))
                )
                _loadedMovies.addAll(movies)
            }
        }

    private fun loadMoviesFromRemote(page: Int = 1, type: MovieType) =
        viewModelScope.launch(Dispatchers.IO) {

            if (page == 1) _loadedMovies.clear()

            try {
                Flowable.just(safeApiCall {
                    if (type == MovieType.UPCOMING)
                        interactors.getUpcomingMoviesFromRemote(page)
                    else interactors.getPopularMoviesFromRemote(page)
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
                                        results.map { movieRequest ->
                                            if (type == MovieType.POPULAR)
                                                movieRequest.ptype = MovieType.POPULAR.value
                                            else movieRequest.utype = MovieType.UPCOMING.value
                                        }
                                        interactors.insertMovie(results)
                                        _loadedMovies.addAll(results.map { movie ->
                                            movie.toPresentationModel()
                                        })

                                        _screenState.postValue(
                                            ScreenState.Render(
                                                DataState.Success(
                                                    results.map { movie ->
                                                        movie.ptype = type.value
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