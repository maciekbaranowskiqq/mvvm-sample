package com.spyro.myapplication.ui.articleslist

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.spyro.myapplication.R
import com.spyro.myapplication.activity.MainActivity
import com.spyro.myapplication.utility.MainActivityProvider
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ArticlesScreenNavigatorTest {
    private lateinit var subject: ArticlesScreenNavigator

    @RelaxedMockK
    private lateinit var mockedMainActivityProvider: MainActivityProvider

    @RelaxedMockK
    private lateinit var mockedActivity: MainActivity

    @RelaxedMockK
    private lateinit var mockedNavController: NavController

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockedMainActivityProvider.getMainActivity() } returns mockedActivity
        mockkStatic(Navigation::class)
        every { mockedActivity.findNavController(R.id.fragmentContainerView) } returns mockedNavController

        subject = ArticlesScreenNavigator(
            mainActivityProvider = mockedMainActivityProvider
        )
    }

    @Test
    fun `navigates to chosen article details`() {
        subject.navigateToArticleDetailsScreen(1)

        verify { mockedNavController.navigate(ArticlesScreenFragmentDirections.actionToArticleDetailsScreen(1)) }
    }
}
