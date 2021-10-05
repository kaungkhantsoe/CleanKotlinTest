package com.kks.cleankotlintest.presentation.view.detail

import android.transition.Transition
import android.view.LayoutInflater
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.databinding.ActivityMovieDetailBinding
import com.kks.cleankotlintest.common.ViewBindingActivity
import com.kks.cleankotlintest.constants.MovieType
import com.kks.cleankotlintest.extensions.showImage
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.viewmodel.MovieViewModel

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
class MovieDetailActivity : ViewBindingActivity<ActivityMovieDetailBinding>() {

    private val viewModel: MovieViewModel by viewModel()

    companion object {
        val ID_EXTRA = "detail:_id"
        val TYPE_EXTRA = "detail:_type"
        val VIEW_NAME_MOVIE_POSTER = "detail:image"
    }

    var movieVO: MovieVO? = null
    var movieType = -1

    override val bindingInflater: (LayoutInflater) -> ActivityMovieDetailBinding
        get() = ActivityMovieDetailBinding::inflate

    override fun setup() {
        setupToolbar()

        // BEGIN_INCLUDE(detail_set_view_name)
        /*
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(binding.imageMovie, VIEW_NAME_MOVIE_POSTER);
        // END_INCLUDE(detail_set_view_name)

        movieVO = viewModel.getDetail(intent.getIntExtra(ID_EXTRA, 0))
        movieType = intent.getIntExtra(TYPE_EXTRA, -1)


        binding.ivLike.setOnClickListener {
            movieVO?.let {
                if (movieType == MovieType.POPULAR.value)
                    it.isPLiked = if (it.isPLiked == 0) 1 else 0
                else it.isULiked = if (it.isULiked == 0) 1 else 0
                viewModel.changeLike(it)
                showImage(
                    binding.ivLike,
                    handleLikeImage()
                )
            }
        }
        loadItem()
    }

    private fun loadItem() {
        binding.textOverview.text = movieVO?.overview
        showImage(binding.imageMovie, "${BuildConfig.IMAGE_URL}${movieVO?.posterPath}")
        showImage(
            binding.ivLike,
            handleLikeImage()
        )
    }

    private fun handleLikeImage() =
        if (movieType == MovieType.POPULAR.value)
            if (movieVO?.isPLiked == 0) R.drawable.ic_like_disable else R.drawable.ic_like
        else
            if (movieVO?.isULiked == 0) R.drawable.ic_like_disable else R.drawable.ic_like

    @RequiresApi(21)
    private fun addTransitionListener(): Boolean {
        val transition = window.sharedElementEnterTransition

        if (transition != null) {
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition) {

                }

                override fun onTransitionEnd(transition: Transition) {
                    transition.removeListener(this)
                }

                override fun onTransitionCancel(transition: Transition) {
                    transition.removeListener(this)
                }

                override fun onTransitionPause(transition: Transition) {

                }

                override fun onTransitionResume(transition: Transition) {

                }

            })
            return true
        }

        return false
    }
}