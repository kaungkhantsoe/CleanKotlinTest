package com.kks.cleankotlintest.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.databinding.ItemMovieBinding
import com.kks.cleankotlintest.callback.MainListener
import com.kks.cleankotlintest.common.BaseAdapter
import com.kks.cleankotlintest.common.inflate
import com.kks.cleankotlintest.constants.MovieType
import com.kks.cleankotlintest.extensions.showImage
import com.kks.cleankotlintest.presentation.model.MovieVO

class MainAdapter(
    private val listener: MainListener,
    private val movieType: MovieType
) : BaseAdapter() {

    override fun onCreateCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = ViewHolder(
        parent.inflate(
            R.layout.item_movie
        )
    )

    override fun onBindCustomViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as ViewHolder).bind(getItemsList()[position] as MovieVO)
    }

    override fun onCreateCustomHeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder? = null


    override fun onBindCustomHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMovieBinding.bind(view)

        fun bind(movieVO: MovieVO) {

            view.context.showImage(
                binding.ivLike,
                if (movieType == MovieType.POPULAR)
                    if (movieVO.isPLiked == 1) R.drawable.ic_like else R.drawable.ic_like_disable
                else if (movieVO.isULiked == 1) R.drawable.ic_like else R.drawable.ic_like_disable
            )

            view.context.showImage(
                binding.imageMoviePoster,
                "${BuildConfig.IMAGE_URL}${movieVO.posterPath}"
            )

            binding.textMovieTitle.text = view.context.getString(
                R.string.movie_title,
                (adapterPosition + 1).toString(),
                movieVO.originalTitle
            )

            itemView.setOnClickListener {
                listener.onClickMovie(movieVO.id, adapterPosition)
            }

            binding.ivLike.setOnClickListener {
                listener.onClickLike(movieVO.id, adapterPosition)
            }

        }
    }
}