package com.github.devjn.moviessample.data


/**
 * Created by @author Jahongir on 23-Aug-18
 * devjn@jn-arts.com
 * Movie
 */
data class MovieData(var data: List<Movie>? = null)

data class Movie(val title: String,
                 val year: String,
                 val genre: String,
                 val poster: String)