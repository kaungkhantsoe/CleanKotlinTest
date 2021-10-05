package com.kks.cleankotlintest.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<VB: ViewBinding, O> protected constructor(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    abstract val binding: VB

    abstract fun bind(item: O)
}