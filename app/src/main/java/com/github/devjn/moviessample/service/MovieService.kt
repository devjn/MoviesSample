package com.github.devjn.moviessample.service

import com.github.devjn.moviessample.data.MovieData
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET


/**
 * Created by @author Jahongir on 23-Aug-18
 * devjn@jn-arts.com
 * MovieService
 */
interface MovieService {

    @GET("api/movies")
    fun getMovies(): Deferred<MovieData>

}