package com.spyro.myapplication.ui.articledetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.spyro.myapplication.databinding.FragmentArticleDetailsScreenBinding
import com.spyro.myapplication.extensions.observeNotNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailsScreenFragment : Fragment() {

    private val viewModel: ArticleDetailsScreenViewModel by viewModels()
    private val arguments: ArticleDetailsScreenFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleDetailsScreenBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentArticleDetailsScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRendering()
        viewModel.onInteraction(ArticleDetailsScreenInteractions.ScreenEntered(arguments.articleId))
    }

    private fun initRendering() = with(viewModel) {
        isProgressIndicatorVisibleLiveData.observeNotNull(viewLifecycleOwner, ::renderProgressIndicator)
        articleDetailsLiveData.observeNotNull(viewLifecycleOwner, ::renderArticleDetails)
    }

    private fun renderProgressIndicator(isVisible: Boolean) = with(binding) {
        progress.isVisible = isVisible
    }

    private fun renderArticleDetails(articleDetails: ArticleDetailsScreenViewModel.ArticleDetailsUiModel) {
        with(binding) {
            id.text = articleDetails.id.toString()
            title.text = articleDetails.title
            subtitle.text = articleDetails.subtitle
            body.text = articleDetails.body
            date.text = articleDetails.date
        }
    }
}
