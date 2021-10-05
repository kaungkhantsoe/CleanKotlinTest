package com.kks.cleankotlintest.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.kks.cleankotlintest.R
import com.kks.cleankotlintest.presentation.viewmodel.detail.MovieDetailViewModel
import com.kks.cleankotlintest.presentation.viewmodel.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { provideGlideInstance(androidContext(), get()) }
    single { provideRequestOptions() }

    viewModel { MainViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get()) }
}

fun provideRequestOptions(): RequestOptions {
    return RequestOptions
        .placeholderOf(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .centerCrop()
}

fun provideGlideInstance(
    context: Context,
    requestOptions: RequestOptions
): RequestManager {
    return Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
}