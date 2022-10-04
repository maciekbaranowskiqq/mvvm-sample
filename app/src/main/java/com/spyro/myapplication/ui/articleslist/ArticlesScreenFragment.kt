package com.spyro.myapplication.ui.articleslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.spyro.myapplication.databinding.FragmentArticlesScreenBinding
import com.spyro.myapplication.extensions.observeNotNull
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesScreenFragment : Fragment() {

    private val viewModel: ArticlesScreenViewModel by viewModels()
    private lateinit var binding: FragmentArticlesScreenBinding
    private lateinit var adapter: ArticleListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArticlesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initRendering()
        viewModel.onInteraction(ArticlesScreenInteractions.ScreenEntered)
    }

    private fun initRendering() = with(viewModel) {
        isProgressIndicatorVisibleLiveData.observeNotNull(viewLifecycleOwner, ::renderProgressIndicator)
        articlesLiveData.observeNotNull(viewLifecycleOwner, ::renderArticles)
    }

    private fun renderProgressIndicator(isVisible: Boolean) = with(binding) {
        progress.isVisible = isVisible
    }

    private fun initView() {
        adapter = ArticleListAdapter()
        binding.articlesRecycler.adapter = adapter
        adapter.doOnItemClicked { viewModel.onInteraction(ArticlesScreenInteractions.OnArticleItemClicked(it.id)) }
    }

    private fun renderArticles(articles: List<ArticleItem.Article>) {
        adapter.submitList(articles)
    }
}
