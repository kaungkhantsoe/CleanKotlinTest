package com.kks.cleankotlintest.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by kaungkhantsoe on 5/17/21.
 **/

abstract class BaseAdapter(items: List<Pageable>? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isLoading = false
    private val itemsList: MutableList<Pageable> = items?.toMutableList() ?: ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_LOADING -> {
            ProgressHolder(prepareLoadingView(parent.context))
        }
        VIEW_RETRY -> {
            val retryFooter = itemsList[itemsList.size - 1] as RetryFooter
            val view: View = LayoutInflater.from(parent.context)
                    .inflate(retryFooter.retryLayoutId, parent, false)
            RetryHolder(view, retryFooter.retryActionTriggerViewId)
        }
        VIEW_HEADER -> {
            onCreateCustomHeaderViewHolder(parent, viewType)!!
        }
        VIEW_EMPTY -> {
            val customView = itemsList[itemsList.size - 1] as EmptyView
            val view: View = LayoutInflater.from(parent.context).inflate(customView.customLayoutId, parent, false)
            EmptyViewHolder(view)
        }
        else -> {
            onCreateCustomViewHolder(parent, viewType)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_LOADING) {
            (holder as ProgressHolder).progressBar.isIndeterminate = true
        } else if (viewType == VIEW_RETRY) {
            val footer = itemsList[position] as RetryFooter
            (holder as RetryHolder).setOnRetryListener(footer.onRetryListener)
        } else if (viewType == VIEW_HEADER) {
            onBindCustomHeaderViewHolder(holder, position)
        } else if (viewType == VIEW_EMPTY) {

        } else {
            onBindCustomViewHolder(holder, position)
        }
    }

    override fun getItemCount(): Int = itemsList.size


    override fun getItemViewType(position: Int): Int = when (itemsList[position]) {
        is ProgressFooter -> {
            VIEW_LOADING
        }
        is RetryFooter -> {
            VIEW_RETRY
        }
        is RecyclerHeader<*> -> {
            VIEW_HEADER
        }
        is EmptyView -> {
            VIEW_EMPTY
        }
        else -> {
            VIEW_ITEM
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    return if (type == VIEW_ITEM) 1 else manager.spanCount
                }
            }
        }
    }

    /**
     * Get List of Items in the adapter
     *
     * @return List of Items in the adapter
     */
    fun getItemsList(): List<Pageable> {
        return itemsList
    }

    /**
     * Removes the last item if it is either ProgressFooter or RetryFooter
     */
    fun clearFooter() {
        isLoading = false
        if (itemsList.isNotEmpty()) {
            val pageable = itemsList[itemsList.size - 1]
            if (pageable is RetryFooter || pageable is ProgressFooter || pageable is EmptyView) {
                itemsList.removeAt(itemsList.size - 1)
                notifyItemRemoved(itemsList.size)
            }
        }
    }

    /**
     * Add one pageable data to a specific position in adapter
     *
     * @param data     Item to be inserted
     * @param position Position where to insert
     */
    fun add(data: Pageable, position: Int) {
        if (0 <= position && position > itemsList.size) {
            itemsList.add(position, data)
            notifyItemInserted(position)
        } else {
            throw ArrayIndexOutOfBoundsException("Inserted position most greater than 0 and less than data size")
        }
    }

    /**
     * Add one pageable data to last position in adapter
     *
     * @param data Item to be inserted
     */
    fun add(data: Pageable) {
        itemsList.add(data)
        notifyItemInserted(itemsList.size)
    }

    /**
     * Add list of pageable items in adapter
     *
     * @param newItems List of items to be inserted
     */
    fun add(newItems: List<Pageable>) {
        itemsList.addAll(newItems)
        notifyItemRangeInserted(itemsList.size - newItems.size, newItems.size)
    }

    fun update(newData: Pageable, position: Int) {
        if (0 <= position && position < itemsList.size) {
            itemsList[position] = newData
            notifyItemChanged(position);
        } else {
            throw ArrayIndexOutOfBoundsException("Inserted position most greater than 0 and less than data size")
        }
    }

    /**
     * Remove one pagable data from a specific position in adapter
     *
     * @param position Position to be removed
     */
    fun remove(position: Int) {
        if (0 <= position && position < itemsList.size) {
            itemsList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            throw ArrayIndexOutOfBoundsException("Inserted position most greater than 0 and less than data size")
        }
    }

    /**
     * Remove a range of pagable data from a specific position to a specific position
     *
     * @param startPosition Start position to remove
     * @param endPosition   End position to remove
     */
    fun remove(startPosition: Int, endPosition: Int) {
        val isValidStart = 0 <= startPosition && startPosition < itemsList.size
        val isValidEnd = 0 <= endPosition && endPosition < itemsList.size
        if (isValidStart && isValidEnd) {
            itemsList.subList(startPosition, endPosition).clear()
            notifyItemRangeRemoved(startPosition, endPosition)
        }
    }

    /**
     * Add HeaderView to the Adapter
     *
     * @param headerData Data to be shown in HeaderView
     */
    fun <T> addHeader(headerData: T) {
        add(RecyclerHeader(headerData))
    }

    /**
     * Add LoadingView to the Adapter
     */
    fun showLoading() {
        if (!isLoading) {
            add(ProgressFooter())
        }
        isLoading = true
    }

    /**
     * Add RetryView to the adapter
     *
     * @param retryLayoutId      Layout ID of the retryView
     * @param retryTriggerViewId Id of the retryView element to trigger retry action on click
     * @param retryListener      RetryListener
     */
    fun showRetry(@LayoutRes retryLayoutId: Int, @IdRes retryTriggerViewId: Int, retryListener: OnRetryListener?) {
        add(RetryFooter(retryListener, retryLayoutId, retryTriggerViewId))
    }

    /**
     * Add EmptyView to the Adapter
     */
    fun showEmptyView(customLayout: Int) {
        add(EmptyView(customLayout))
    }

    /**
     * Clear all items in the adapter
     */
    fun clear() {
        itemsList.clear()
        notifyDataSetChanged()
    }

    fun getHeaderPosition(): Int {
        var position = -1
        for (p in itemsList) {
            if (p is RecyclerHeader<*>) {
                position = itemsList.indexOf(p)
                return position
            }
        }
        return position
    }

    private fun prepareLoadingView(context: Context): View {
        val relativeLayout = RelativeLayout(context)
        val relativeLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        val progressBar = ProgressBar(context)
        progressBar.setPadding(5, 5, 5, 5)
        progressBar.tag = 123
        relativeLayout.addView(progressBar, relativeLayoutParams)
        return relativeLayout
    }

    /*------------------------------------Abstract Methods to be implemented by child classes------------------------------------*/ /*To perform onCreateViewHolder tasks for custom item*/
    protected abstract fun onCreateCustomViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /*To perform onBindViewHolder tasks for custom item*/
    protected abstract fun onBindCustomViewHolder(holder: RecyclerView.ViewHolder?, position: Int)

    /*To perform onCreateViewHolder tasks for HeaderView*/
    protected abstract fun onCreateCustomHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder?

    /*To perform onBindViewHolder tasks for Header*/
    protected abstract fun onBindCustomHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int)

    /*------------------------------------Required Classes and Interface------------------------------------*/
    interface OnRetryListener {
        fun onRetry()
    }

    class ProgressFooter : Pageable
    class EmptyView(@field:LayoutRes val customLayoutId: Int) : Pageable
    class RetryFooter(val onRetryListener: OnRetryListener?, @field:LayoutRes @param:LayoutRes val retryLayoutId: Int, @field:IdRes @param:IdRes val retryActionTriggerViewId: Int) : Pageable
    class RecyclerHeader<T>(val headerData: T) : Pageable

    /*------------------------------------View Holders (Progress Holder and Retry Holder)------------------------------------*/
    private inner class ProgressHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewWithTag(123)

    }

    private inner class EmptyViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    private inner class RetryHolder constructor(itemView: View, @IdRes retryTriggerViewId: Int) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var onRetryListener: OnRetryListener? = null
        override fun onClick(v: View?) {
            if (onRetryListener != null) {
                clearFooter()
                onRetryListener!!.onRetry()
            }
        }

        fun setOnRetryListener(onRetryListener: OnRetryListener?) {
            this.onRetryListener = onRetryListener
        }

        init {
            if (retryTriggerViewId == 0) {
                itemView.setOnClickListener(this)
            } else {
//                itemView.findViewById(retryTrigerViewId).setOnClickListener(this)
            }
        }
    }

    companion object {
        private const val VIEW_LOADING = 0
        private const val VIEW_RETRY = 1
        private const val VIEW_ITEM = 2
        private const val VIEW_HEADER = 3
        private const val VIEW_EMPTY = 4
    }

}