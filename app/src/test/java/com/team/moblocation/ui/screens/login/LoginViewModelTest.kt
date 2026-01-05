package com.team.moblocation.ui.screens.login

import com.team.moblocation.core.data.repo.IUserRepo
import com.team.moblocation.core.data.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

	val repo: IUserRepo = mock()
	val viewModel = LoginViewModel(repo)

	@Before
	fun setup() {
		val testDispatcher = UnconfinedTestDispatcher()
		whenever(repo.getUser()).thenReturn("team")
		Dispatchers.setMain(testDispatcher)
	}

	@After
	fun cleanup() {
		Dispatchers.resetMain()
	}

	@Test
	fun `fetch user returns the mock value`() {
		assertEquals("team", viewModel.fetchUser())
	}

	@Test
	fun `greetings should return hello mocked value`() {
		assertEquals("team", viewModel.greetings())
	}

	@Test
	fun `fetchUser should update the greetings stateflow with greetings`() = runTest {
		viewModel.fetchUser()
		val greetings = viewModel.greetings.drop(1).first()
		assertEquals("Hello team", greetings)
	}

	@Test
	fun `Greet function should update the greetings stateflow with Hello $name`() = runTest {
		viewModel.greet("Jia")
		val msg = viewModel.greetings.drop(1).first()
		assertEquals("Hello Jia", msg, "Extra info about test")
	}

	@Test
	fun `Login function should login the user`() = runTest {
		var message: String? = null
		viewModel.loginWithEmail(
			"email@a.com", "password", onError = { error -> message = error })
		assertEquals(null, message, "Login failed")
	}

	@Test
	fun `Validation should fail for email and password`() {
		assert(viewModel.validate("email", "password") != null)
	}

	@Test
	fun `Validation should fail for email@a,com and pass`() {
		assert(viewModel.validate("email@a.com", "pass") != null)
	}

	@Test
	fun `Validation should pass for email@a,com and password`() {
		assert(viewModel.validate("email@a.com", "password") == null)
	}

	@Test
	fun `Validation should pass for email@gmail,com and password`() {
		assert(viewModel.validate("email@gmail.com", "password") == null)
	}
}