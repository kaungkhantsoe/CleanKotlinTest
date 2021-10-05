package com.kks.cleankotlintest.presentation.view.main

import android.content.Intent
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.databinding.ActivityMainBinding
import com.kks.cleankotlintest.presentation.adapters.MainAdapter
import com.kks.cleankotlintest.callback.MainListener
import com.kks.cleankotlintest.common.*
import com.kks.cleankotlintest.extensions.SnackBarOnRetryListener
import com.kks.cleankotlintest.extensions.showRetrySnackBar
import com.kks.cleankotlintest.extensions.showSnackBar
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.view.detail.MovieDetailActivity
import com.kks.cleankotlintest.presentation.viewmodel.main.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ViewBindingActivity<ActivityMainBinding>(),
    SwipeRefreshLayout.OnRefreshListener,
    SmartScrollListener.OnSmartScrollListener,
    MainListener, SnackBarOnRetryListener {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var layoutManager: LinearLayoutManager

    private val adapter by lazy {
        MainAdapter(this)
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
            is ScreenState.Loading -> {
                binding.progress.visible(true)
            }
            is ScreenState.Render -> processRenderState(screenState.renderState)
        }
    }

    private fun processRenderState(renderState: DataState<Nothing>) {
        binding.progress.gone(true)

        when (renderState) {
            is DataState.Success -> {
                if (renderState.data is List<*>) {
                    addMovies(renderState.data as List<MovieVO>)
                }
            }
            is DataState.Error -> {
                showRetrySnackBar(binding.contextView, renderState.error.title, this@MainActivity)
            }
            is DataState.EndReach -> {
                showSnackBar(binding.contextView, getString(R.string.you_have_reached_the_end))
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

    private var detailMovieId = -1
    private var detailMoviePosition = -1

    private var detailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (detailMovieId != -1)
                viewModel.getDetail(detailMovieId)?.let {
                    adapter.update(it, detailMoviePosition)
                }
        }

    override fun onClickMovie(id: Int, position: Int) {
        detailMovieId = id
        detailMoviePosition = position
        detailLauncher.launch(
            Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
                putExtra(MovieDetailActivity.ID_EXTRA, id)
            }
        )
    }

    override fun onClickLike(id: Int, position: Int) {
        val item = adapter.getItemsList()[position] as MovieVO
        item.isLiked = if (item.isLiked == 0) 1 else 0
        viewModel.changeLike(item)
        adapter.update(item, position)
    }

    override fun onRetry() {
        adapter.clear()
        viewModel.pageNumber = 1
    }

}