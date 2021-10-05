package com.kks.cleankotlintest.presentation.view.detail

import android.os.Build
import android.transition.Transition
import android.view.LayoutInflater
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.bumptech.glide.RequestManager
import com.kks.cleankotlintest.databinding.ActivityMovieDetailBinding
import com.kks.cleankotlintest.common.ViewBindingActivity
import com.kks.cleankotlintest.presentation.model.MovieVO
import com.kks.cleankotlintest.presentation.viewmodel.detail.MovieDetailViewModel
import org.koin.android.ext.android.inject

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
class MovieDetailActivity: ViewBindingActivity<ActivityMovieDetailBinding>() {

    private val viewModel : MovieDetailViewModel by viewModel()

    companion object {
        val ID_EXTRA = "detail:_id"
        val VIEW_NAME_MOVIE_POSTER = "detail:image"
    }

    val requestManager: RequestManager by inject()

    var movieVO: MovieVO? = null

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

        movieVO = viewModel.getDetail(intent.getIntExtra(ID_EXTRA,0))

        loadItem()
    }

    private fun loadItem() {
        binding.textOverview.text = movieVO?.overview

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            requestManager.load("https://image.tmdb.org/t/p/w500${movieVO?.posterPath}")
                .into(binding.imageMovie)
//            loadThumbnail()
        } else {
            // If all other cases we should just load the full-size image now
//            loadFullSizeImage()
        }
    }

    @RequiresApi(21)
    private fun addTransitionListener() : Boolean {
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