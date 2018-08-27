package com.github.devjn.moviessample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.arlib.floatingsearchview.util.view.SearchInputView
import com.github.devjn.moviessample.RecyclerViewItemCountAssertion.withItemCount
import com.github.devjn.moviessample.data.MovieData
import com.github.devjn.moviessample.rules.FragmentTestRule
import com.github.devjn.moviessample.service.MovieAPI
import com.github.devjn.moviessample.view.MainActivity
import com.github.devjn.moviessample.view.MainFragment
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import java.util.concurrent.Callable


@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @Rule
    @JvmField
    val rule = FragmentTestRule(MainActivity::class.java, MainFragment.newInstance())

    @Rule
    @JvmField
    val archRule: TestRule = InstantTaskExecutorRule()

    private lateinit var server: MockWebServer
    private val fileName = "movies.json"
    private val movieData = RestServiceTestHelper.load(InstrumentationRegistry.getContext(), fileName, MovieData::class.java)

    @Before
    @Throws(Exception::class)
    fun setup() {
        server = MockWebServer()
        server.start()
        MovieAPI.changeApiBaseUrl(server.url("/").toString())
        Provider.componentProvider = object : Provider.ComponentProvider {
            override fun provideCoroutineContextProvider(): Provider.CoroutineContextProvider {
                return TestContextProvider()
            }
        }
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(RestServiceTestHelper.getStringFromFile(InstrumentationRegistry.getContext(), fileName)))
    }

    @Test
    @Throws(Exception::class)
    fun testMoviesAreDisplayed() {
        await().until(viewModelData, equalTo(movieData.data));

        onView(withText(R.string.no_data)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.loading_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.list)).check(withItemCount(25))
    }

    @Test
    @Throws(Exception::class)
    fun testSearchFound() {
        onView(withId(R.id.search_view)).perform(click())
        onView(isAssignableFrom(SearchInputView::class.java)).perform(typeText("Action"), pressImeActionButton())

        // Check empty view is not displayed
        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())))
        // Check correct number of items found
        onView(withId(R.id.list)).check(withItemCount(6))
    }


    @After
    @Throws(Exception::class)
    fun teardown() {
        server.shutdown()
    }

    private val viewModelData = Callable {
        rule.fragment.viewModel.data.value // The condition supplier part
    }

}
