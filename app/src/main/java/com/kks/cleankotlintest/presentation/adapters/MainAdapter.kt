package com.kks.cleankotlintest.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.databinding.ItemMovieBinding
import com.kks.cleankotlintest.callback.MainListener
import com.kks.cleankotlintest.common.BaseAdapter
import com.kks.cleankotlintest.common.inflate
import com.kks.cleankotlintest.presentation.model.MovieVO
import timber.log.Timber

/**
 * Created by kaungkhantsoe on 5/17/21.
 **/
class MainAdapter(
    private val requestManager: RequestManager,
    private val listener: MainListener
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

            Timber.d("Data $movieVO")

            Glide.with(view.context)
                .load("${BuildConfig.IMAGE_URL}${movieVO.posterPath}")
                .into(binding.imageMoviePoster)
            binding.textMovieTitle.text = "${adapterPosition+1} ${movieVO.originalTitle}"

            itemView.setOnClickListener {
                listener.onClickMovie(movieVO.id,binding.imageMoviePoster)
            }

        }
    }
}