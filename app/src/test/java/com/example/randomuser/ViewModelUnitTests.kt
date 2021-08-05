package com.example.randomuser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.randomuser.mocks.FakeFailureUserRepository
import com.example.randomuser.model.User
import com.example.randomuser.ui.UserListViewState
import com.example.randomuser.ui.viewmodel.UserViewModel
import com.example.randomuser.util.Constants.repoFailureMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks

/**
 * Tests for the ViewModels
 */
@ExperimentalCoroutinesApi
class ViewModelUnitTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()
    private val failureRepo = FakeFailureUserRepository()

    @Mock
    private lateinit var observer: Observer<UserListViewState<List<User>>>
    @Captor
    private lateinit var captor: ArgumentCaptor<UserListViewState<List<User>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        initMocks(this)
    }

    @Test
    fun `refreshUsers emits failure, state should be Failure with error message`() =
        runBlocking {
            val viewModel = UserViewModel(failureRepo)
            viewModel.refreshUsers()
            val liveData = viewModel.state
            liveData.observeForever(observer)
            verify(observer).onChanged(captor.capture())
            assertTrue(captor.value is UserListViewState.Failure<*>)
            assertEquals(repoFailureMessage,
                (captor.value as UserListViewState.Failure).errorMessage)
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }
}