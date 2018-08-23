package com.github.devjn.moviessample.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.devjn.moviessample.data.Movie
import com.github.devjn.moviessample.service.MovieAPI
import kotlinx.coroutines.experimental.launch

class MainViewModel : ViewModel() {

    val data = MutableLiveData<List<Movie>>()

    init {
        doRequest()
    }

    fun doRequest() = launch {
        val result = MovieAPI.movieService.getMovies().await()
        data.postValue(result.data)
    }

}
