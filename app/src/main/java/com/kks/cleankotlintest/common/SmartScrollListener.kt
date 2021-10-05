package com.kks.cleankotlintest.common

import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kaungkhantsoe on 20/05/2021.
 **/

class SmartScrollListener(smartScrollListener: OnSmartScrollListener) :
    RecyclerView.OnScrollListener() {
    interface OnSmartScrollListener {
        fun onListEndReach()
    }

    private var isListEndReached = false
    private var canScrollMore = true
    private val mSmartScrollListener: OnSmartScrollListener = smartScrollListener
    fun setCanScrollMore(canScrollMore: Boolean) {
        this.canScrollMore = canScrollMore
    }

    fun isCanScrollMore(): Boolean {
        return canScrollMore
    }

    override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(rv, dx, dy)
        val visibleItemCount = rv.layoutManager!!.childCount
        val totalItemCount = rv.layoutManager!!.itemCount
        val pastVisibleItems =
            (rv.layoutManager as LinearLayoutManager?)!!.findFirstVisibleItemPosition()
        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
            isListEndReached = false
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {
        super.onScrollStateChanged(recyclerView, scrollState)
        if (scrollState == RecyclerView.SCROLL_STATE_IDLE && (recyclerView.layoutManager as LinearLayoutManager?)
                ?.findLastVisibleItemPosition() == recyclerView.adapter!!.itemCount - 1 && !isListEndReached && canScrollMore
        ) {
            isListEndReached = true
            mSmartScrollListener.onListEndReach()
        }
    }

}