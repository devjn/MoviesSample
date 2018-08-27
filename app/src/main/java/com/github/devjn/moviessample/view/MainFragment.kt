package com.github.devjn.moviessample.view

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.github.devjn.moviessample.App
import com.github.devjn.moviessample.R
import com.github.devjn.moviessample.data.Movie
import com.github.devjn.moviessample.databinding.ListItemMovieBinding
import com.github.devjn.moviessample.databinding.MainFragmentBinding
import com.github.devjn.moviessample.utils.AndroidUtils
import com.github.devjn.moviessample.viewmodel.MainViewModel
import com.github.devjn.moviessample.widgets.GridSpacingItemDecoration


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    internal val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private lateinit var binding: MainFragmentBinding;
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.list.apply {
            val spans = if (resources.configuration.orientation == ORIENTATION_PORTRAIT) 3 else 4
            layoutManager = GridLayoutManager(context, spans)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(GridSpacingItemDecoration(spans, AndroidUtils.dp(16)))
        }
        adapter = Adapter()
        binding.list.adapter = adapter
        binding.swipetorefresh.setOnRefreshListener { viewModel.doRequest() }
        setupSearchView()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(this, Observer { data ->
            data?.let { adapter.data = it }
            binding.swipetorefresh.isRefreshing = false
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
                viewModel.search(searchSuggestion.body)
            }

            override fun onSearchAction(query: String) {
                viewModel.search(query)
                Log.d(App.TAG, "onSearchAction, query: $query")
            }
        })

        binding.searchView.setOnBindSuggestionCallback { _, leftIcon, _, _, _ ->
            leftIcon.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_history_black_24dp, null))
            leftIcon.alpha = .36f
        }
    }

    private inner class Adapter() : RecyclerView.Adapter<Adapter.SimpleViewHolder>() {

        init {
            setHasStableIds(true)
        }

        var data: List<Movie> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SimpleViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.list_item_movie, parent, false))

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemId(position: Int) = data[position].id
        override fun getItemCount() = data.size

        inner class SimpleViewHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Movie) {
                binding.movie = data
            }
        }
    }

}
