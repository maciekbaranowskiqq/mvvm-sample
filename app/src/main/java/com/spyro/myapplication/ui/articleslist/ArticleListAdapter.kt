package com.spyro.myapplication.ui.articleslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spyro.myapplication.databinding.ItemArticleBinding

private const val ARTICLE_ITEM_TYPE = 1

class ArticleListAdapter : ListAdapter<ArticleItem, RecyclerView.ViewHolder>(DiffCallback) {
    private var onItemClickedAction: ((ArticleItem.Article) -> Unit)? = null

    fun doOnItemClicked(action: (ArticleItem.Article) -> Unit) {
        onItemClickedAction = action
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ARTICLE_ITEM_TYPE -> ArticleViewHolder.create(parent) { item -> onItemClickedAction?.invoke(item) }
            else -> error("viewType: $viewType not handled")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ArticleItem.Article -> ARTICLE_ITEM_TYPE
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        when (holder) {
            is ArticleViewHolder -> holder.bind(item as ArticleItem.Article)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ArticleItem>() {
        override fun areItemsTheSame(
            oldItem: ArticleItem,
            newItem: ArticleItem
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ArticleItem,
            newItem: ArticleItem
        ) = oldItem == newItem
    }
}

class ArticleViewHolder(
    private val binding: ItemArticleBinding,
    private val action: (ArticleItem.Article) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ArticleItem.Article) = with(binding) {
        id.text = item.id.toString()
        title.text = item.title
        subtitle.text = item.subtitle
        root.setOnClickListener { action(item) }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (ArticleItem.Article) -> Unit
        ): ArticleViewHolder {
            val binding =
                ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ArticleViewHolder(binding, action)
        }
    }
}

sealed class ArticleItem {
    data class Article(
        val id: Long,
        val title: String,
        val subtitle: String,
    ) : ArticleItem()
}
