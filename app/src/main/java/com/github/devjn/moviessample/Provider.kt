package com.github.devjn.moviessample

import com.github.devjn.moviessample.service.MovieAPI
import com.github.devjn.moviessample.service.MovieService
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext


/**
 * Created by @author Jahongir on 26-Aug-18
 * devjn@jn-arts.com
 * Provider
 */
object Provider {

    interface ComponentProvider {
        fun provideMovieService(): MovieService = MovieAPI.createService(MovieService::class.java)
        fun provideCoroutineContextProvider(): CoroutineContextProvider = DefaultCoroutineContextProvider()
    }

    var componentProvider: ComponentProvider = object : ComponentProvider {}

    val movieService by lazy { componentProvider.provideMovieService() }
    val coroutineContext by lazy { componentProvider.provideCoroutineContextProvider() }


    interface CoroutineContextProvider {
        val Main: CoroutineContext
        val IO: CoroutineContext
    }

    class DefaultCoroutineContextProvider : CoroutineContextProvider {
        override val Main: CoroutineContext by lazy { UI }
        override val IO: CoroutineContext by lazy { CommonPool }
    }

}