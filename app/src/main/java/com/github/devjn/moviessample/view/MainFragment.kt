package com.github.devjn.moviessample.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.devjn.moviessample.R
import com.github.devjn.moviessample.data.Movie
import com.github.devjn.moviessample.databinding.ListItemMovieBinding
import com.github.devjn.moviessample.databinding.MainFragmentBinding
import com.github.devjn.moviessample.viewmodel.MainViewModel


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private lateinit var binding: MainFragmentBinding;
    private lateinit var adapter: Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.list.apply {
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
        }
        adapter = Adapter()
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(this, Observer { data ->
            data?.let { adapter.data = it }
        })
    }


    private inner class Adapter() : RecyclerView.Adapter<Adapter.SimpleViewHolder>() {

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

        override fun getItemCount() = data.size

        inner class SimpleViewHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Movie) {
                binding.movie = data
            }
        }

    }

}
