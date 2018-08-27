package com.github.devjn.moviessample.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.devjn.moviessample.App
import com.github.devjn.moviessample.Provider
import com.github.devjn.moviessample.data.Movie
import com.github.devjn.moviessample.service.MovieService
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class MainViewModel : ViewModel() {

    val loadingBarVisibility = ObservableInt(View.GONE)
    val emptyViewVisibility = ObservableInt(View.GONE)
    val data = MutableLiveData<List<Movie>>()
    private val compositeDisposable = Job()

    private var allData: List<Movie>? = null
    var lastQuery = ""
        private set

    init {
        doRequest()
    }

    // This can be done cleaner using RxJava and Dagger but it's overhead for such small project
    fun doRequest(movieService: MovieService =  Provider.movieService) = launch(Provider.coroutineContext.Main, parent = compositeDisposable) {
        loadingBarVisibility.set(View.VISIBLE)
        try {
            withContext(Provider.coroutineContext.IO) { movieService.getMovies().await() }.let {
                data.value = it.data
                allData = it.data
            }
            emptyViewVisibility.set(View.GONE)
        } catch (e: Exception) {
            Log.w(App.TAG, "Exception during loading movie data", e)
            emptyViewVisibility.set(View.VISIBLE)
        }
        loadingBarVisibility.set(View.GONE)
    }

    fun search(query: String) = launch(Provider.coroutineContext.Main, parent = compositeDisposable) {
        val allData = allData ?: return@launch
        loadingBarVisibility.set(View.VISIBLE)
        lastQuery = query
        val filtered = filter(allData, query)
        emptyViewVisibility.set(if (filtered.isEmpty()) View.VISIBLE else View.GONE)
        loadingBarVisibility.set(View.GONE)
        data.value = filtered
    }

    private suspend fun filter(list: List<Movie>, query: String): List<Movie> = withContext(Provider.coroutineContext.IO) {
        return@withContext list.filter { it.title.startsWith(query, true) || it.genre.startsWith(query, true) }
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.cancel()
    }

}

