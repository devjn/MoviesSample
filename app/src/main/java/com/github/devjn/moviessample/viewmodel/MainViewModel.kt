package com.github.devjn.moviessample.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.devjn.moviessample.data.Movie
import com.github.devjn.moviessample.service.MovieAPI
import kotlinx.coroutines.experimental.launch

class MainViewModel : ViewModel() {

    val loadingBarVisibility = ObservableInt(View.GONE)
    val data = MutableLiveData<List<Movie>>()

    private var allData: List<Movie>? = null
    var lastQuery = ""
        private set

    init {
        doRequest()
    }

    fun doRequest() = launch {
        loadingBarVisibility.set(View.VISIBLE)
        val result = MovieAPI.movieService.getMovies().await()
        loadingBarVisibility.set(View.GONE)
        data.postValue(result.data)
        allData = result.data
    }

    //TODO: Replace with RxJava
    fun search(query: String) = launch {
        loadingBarVisibility.set(View.VISIBLE)
        lastQuery = query
        allData?.let { movies ->
            val filtered = movies.filter { it.title.startsWith(query, true) || it.genre.startsWith(query, true) }
            data.postValue(filtered)
        }
        loadingBarVisibility.set(View.GONE)
    }

}
