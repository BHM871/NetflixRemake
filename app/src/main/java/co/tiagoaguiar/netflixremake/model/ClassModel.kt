package co.tiagoaguiar.netflixremake.model

data class Category(
    val id: Int,
    val title: String,
    val movies: List<Movie>)

data class Movie(
    val id: Int,
    val coverUrl: String)