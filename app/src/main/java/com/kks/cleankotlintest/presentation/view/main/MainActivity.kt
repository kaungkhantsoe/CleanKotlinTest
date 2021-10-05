package com.kks.cleankotlintest.presentation.view.main

import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.kks.cleankotlintest.databinding.ActivityMainBinding
import com.kks.cleankotlintest.presentation.adapters.MainAdapter
import com.kks.cleankotlintest.callback.MainListener
import com.kks.cleankotlintest.common.*
import com.kks.cleankotlintest.presentation.model.MovieVO
import org.koin.android.ext.android.inject
import com.kks.cleankotlintest.presentation.view.detail.MovieDetailActivity
import com.kks.cleankotlintest.presentation.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ViewBindingActivity<ActivityMainBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SmartScrollListener.OnSmartScrollListener,
    MainListener {

    val requestManager: RequestManager by inject()

    private val viewModel: MainViewModel by viewModel()

    private lateinit var layoutManager: LinearLayoutManager

    private val adapter by lazy {
        MainAdapter(requestManager, this)
    }

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding
        get() = ActivityMainBinding::inflate

    override fun setup() {

        binding.swipeRefresh.setOnRefreshListener(this)

        layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerMovieList.adapter = adapter
        binding.recyclerMovieList.layoutManager = layoutManager
        binding.recyclerMovieList.setHasFixedSize(true)
        binding.recyclerMovieList.addOnScrollListener(SmartScrollListener(this))

        viewModel.screenState.observe(::getLifecycle, ::updateUI)
    }

    private fun updateUI(screenState: ScreenState<DataState<Nothing>>) {
        when (screenState) {
            ScreenState.Loading -> {
                binding.progress.visible(true)
            }
            is ScreenState.Render -> processRenderState(screenState.renderState)
        }
    }

    private fun processRenderState(renderState: DataState<Nothing>) {

        //Just to make progress visible a while in case of fast data fetch
        Handler(mainLooper).postDelayed({
            binding.progress.gone(true)
        }, 1000)

        when (renderState) {
            is DataState.Success -> {
                if (renderState.data is List<*>) {
                    addMovies(renderState.data as List<MovieVO>)
//                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//                    },10000)
                }
            }
            is DataState.Error -> {
                showToast(renderState.message.toString())
            }
            is DataState.EndReach -> {
                Snackbar.make(
                    binding.contextView,
                    "You have reached the end",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addMovies(movieVOS: List<MovieVO>) {
        adapter.clearFooter()
        adapter.add(movieVOS)
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        adapter.clear()
        viewModel.pageNumber = 1
    }

    override fun onListEndReach() {
        viewModel.pageNumber++
    }

    override fun onClickMovie(id: Int, view: View) {

        val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ID_EXTRA, id)

        val activityOptions: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                Pair(
                    view,
                    MovieDetailActivity.VIEW_NAME_MOVIE_POSTER
                )

            )

        ActivityCompat.startActivity(this@MainActivity, intent, activityOptions.toBundle())
    }

}