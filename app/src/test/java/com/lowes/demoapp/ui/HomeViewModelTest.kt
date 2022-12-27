package com.lowes.demoapp.ui

import android.app.Application
import android.content.Context
import android.util.Log
import app.cash.turbine.test
import com.lowes.demoapp.domain.model.Album
import com.lowes.demoapp.usecases.DoSearchAlbumsUseCase
import com.lowes.demoapp.usecases.GetAccessTokenUseCase
import com.lowes.demoapp.usecases.GetNewReleasesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import junit.framework.TestCase.assertEquals

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.doReturn
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
@RunWith(PowerMockRunner::class)
@PrepareForTest(Log::class, GetAccessTokenUseCase::class)
class HomeViewModelTest {
    private lateinit var app: Application
    private lateinit var getAccessTokenUseCase: GetAccessTokenUseCase
    private lateinit var getNewReleasesUseCase: GetNewReleasesUseCase
    private lateinit var doSearchAlbumsUseCase: DoSearchAlbumsUseCase
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        PowerMockito.mockStatic(Log::class.java)
        Mockito.`when`(Log.d(any(), any())).then {1}
        getAccessTokenUseCase = mock(GetAccessTokenUseCase::class.java)
        getNewReleasesUseCase = mock(GetNewReleasesUseCase::class.java)
        doSearchAlbumsUseCase = mock(DoSearchAlbumsUseCase::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    fun setupViewModel(app: Application = mock(Application::class.java)) : HomeViewModel{
        this.app = app
        var context = mock(Context::class.java)
        Mockito.`when`(app.applicationContext)doReturn context

        var viewModel = HomeViewModel(app)
        viewModel.getAccessTokenUseCase = getAccessTokenUseCase
        viewModel.getNewReleasesUseCase = getNewReleasesUseCase
        viewModel.doSearchUseCase = doSearchAlbumsUseCase

        return viewModel
    }
    @Test
    fun init_accessToemInitializedFalse() {
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.accessTokenInitialized.test {
                assertEquals(false, awaitItem())
            }
        }
    }

    @Test
    fun getAccessToken_success_accessToemInitializedTrue() {
        val viewModel = setupViewModel()
        runBlocking {
            Mockito.`when`(viewModel.getAccessTokenUseCase.invoke(app))doReturn "token"
            viewModel.accessTokenInitialized.test {
                assertEquals(false, awaitItem())
                viewModel.getAcessToken()
                verify(viewModel.getAccessTokenUseCase).invoke(app)
                assertEquals(true, awaitItem())
            }
        }
    }

    @Test
    fun getAccessToken_failure_accessToemInitializedFalse() {
        val viewModel = setupViewModel()
        runBlocking {
            Mockito.`when`(viewModel.getAccessTokenUseCase.invoke(app))doReturn(null)
            //doReturn("token").`when`(viewModel.getAccessTokenUseCase.invoke(app))
            viewModel.accessTokenInitialized.test {
                assertEquals(false, awaitItem())
                viewModel.getAcessToken()
                verify(viewModel.getAccessTokenUseCase).invoke(app)
                //no change in accessTokenInitialized as its already false
            }
        }
    }

    @Test
    fun getNewReleases_success_validNewReleases(){
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.accessToken = "token"
            var album = Album()
            Mockito.`when`(viewModel.getNewReleasesUseCase.invoke(app, viewModel.accessToken!!))doReturn(Arrays.asList(album))
            viewModel.newReleases.test {
                assertEquals(0,awaitItem().size)
                viewModel.getNewReleases()
                verify(viewModel.getNewReleasesUseCase).invoke(app,viewModel.accessToken!!)
                assertEquals(1,awaitItem().size)
            }
        }
    }

    @Test
    fun getNewReleases_failure_noUpdateOfNewReleases(){
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.accessToken = null
            var mockToken = mock(CharSequence::class.java)
            var album = Album()
            Mockito.`when`(viewModel.getNewReleasesUseCase.invoke(app, mockToken))doReturn(Arrays.asList(album))
            viewModel.newReleases.test {
                assertEquals(0,awaitItem().size)
                viewModel.getNewReleases()
                verify(viewModel.getNewReleasesUseCase, never()).invoke(app,mockToken)
            }
        }
    }

    @Test
    fun doSearch_success_validNewReleases(){
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.accessToken = "token"
            var query = "query"
            var mockToken = mock(CharSequence::class.java)
            var mockQuery = mock(CharSequence::class.java)
            var album = Album()
            Mockito.`when`(viewModel.doSearchUseCase.invoke(app, viewModel.accessToken!!, query))doReturn(Arrays.asList(album))
            viewModel.newReleases.test {
                assertEquals(0,awaitItem().size)
                viewModel.doSearch(query)
                viewModel.searchQuery.test {
                    assertEquals(query, awaitItem())
                }
                verify(viewModel.doSearchUseCase).invoke(app,viewModel.accessToken!!, query)
                assertEquals(1,awaitItem().size)
            }
        }
    }

    @Test
    fun doSearch_failure_noUpdateOfNewReleases(){
        val viewModel = setupViewModel()
        runBlocking {
            viewModel.accessToken = null
            var query = "query"
            var mockToken = mock(CharSequence::class.java)
            var album = Album()
            Mockito.`when`(viewModel.doSearchUseCase.invoke(app, mockToken, query))doReturn(Arrays.asList(album))
            viewModel.newReleases.test {
                assertEquals(0,awaitItem().size)
                viewModel.doSearch(query)
                viewModel.searchQuery.test {
                    assertEquals(query, awaitItem())
                }
                verify(viewModel.doSearchUseCase, never()).invoke(app,mockToken, query)
            }
        }
    }
}