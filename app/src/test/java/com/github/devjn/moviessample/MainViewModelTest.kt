package com.github.devjn.moviessample

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.devjn.moviessample.data.MovieData
import com.github.devjn.moviessample.service.MovieService
import com.github.devjn.moviessample.viewmodel.MainViewModel
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.launch
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


/**
 * Created by @author Jahongir on 25-Aug-18
 * devjn@jn-arts.com
 * MainViewModelTest
 */
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()
    private val movieData: MovieData = RestServiceUnitTestHelper.load("movies.json", MovieData::class.java)

    @Mock
    private lateinit var movieService: MovieService
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Provider.componentProvider = object : Provider.ComponentProvider {
            override fun provideMovieService() = movieService
            override fun provideCoroutineContextProvider() = TestContextProvider()
        }

        When calling movieService.getMovies() itReturns  CompletableDeferred(movieData)
        viewModel = MainViewModel()
    }


    @Test
    fun testDataLoaded(): Unit = viewModel.run {
        doRequest()

        data.value.shouldNotBeNull()
        data.value shouldEqual movieData.data
        emptyViewVisibility.get() shouldEqual View.GONE
        loadingBarVisibility.get() shouldEqual View.GONE
    }

    @Test
    fun testSearchNotFound(): Unit = viewModel.run {
        doRequest()
        search("BlaBla")

        data.value.shouldNotBeNull()
        data.value!!.shouldBeEmpty()
        emptyViewVisibility.get() shouldEqual View.VISIBLE
        loadingBarVisibility.get() shouldEqual View.GONE
    }

    @Test
    fun testSearchFound(): Unit = viewModel.run {
        doRequest()
        search("Action")

        data.value.shouldNotBeNull()
        data.value!!.size shouldEqual 6
        emptyViewVisibility.get() shouldEqual View.GONE
        loadingBarVisibility.get() shouldEqual View.GONE
    }

    @Test
    fun testOnError() {
        val deferred = Mockito.mock(Deferred::class.java)
        launch(context = Unconfined) { When calling deferred.await() itThrows RuntimeException() }
        Mockito.doAnswer { deferred }.`when`(movieService).getMovies()

        viewModel.apply {
            data.value = null
            doRequest(movieService)

            data.value.shouldBeNull()
            emptyViewVisibility.get() shouldEqual View.VISIBLE
            loadingBarVisibility.get() shouldEqual View.GONE
        }
    }


}


