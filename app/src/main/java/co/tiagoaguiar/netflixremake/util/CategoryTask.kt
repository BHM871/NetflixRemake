package co.tiagoaguiar.netflixremake.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class CategoryTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())

    interface Callback {
        fun onPreExecute()
        fun onResult(categories: List<Category>)
        fun onFailure(message: String)
    }

    fun execute (url: String){
        callback.onPreExecute()

        val executor = Executors.newSingleThreadExecutor()

        executor.execute{

            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null
            var buffer: BufferedInputStream? = null

            try {

                val requestUrl = URL(url)
                urlConnection = requestUrl.openConnection() as HttpsURLConnection
                urlConnection.readTimeout = 2000
                urlConnection.connectTimeout = 2000

                val statusCode: Int = urlConnection.responseCode
                if (statusCode >= 400) {
                    throw IOException("Erro de conecção com o servidor")
                }

                stream = urlConnection.inputStream
                //Method one
                //val jsonAsString = stream.bufferedReader().use { it.readText() }

                //Method two
                buffer = BufferedInputStream(stream)
                val jsonAsString2 = toString(buffer)

                val categories = toCategories(jsonAsString2)

                handler.post {
                    callback.onResult(categories)
                }

            } catch (e: IOException){
                val message = e.message ?: "ERROR DESCONHECIDO"
                Log.e("ERROR", message, e)
                handler.post {
                    callback.onFailure(message)
                }
            } finally {
                urlConnection?.disconnect()
                stream?.close()
                buffer?.close()
            }
        }
    }

    private fun toCategories(json: String): List<Category>{
        val categories = mutableListOf<Category>()

        val jsonRoot = JSONObject(json)
        val jsonArrayCategories = jsonRoot.getJSONArray("category")

        for (i in 0 until jsonArrayCategories.length()){
            val jsonCategoryNow = jsonArrayCategories.getJSONObject(i)

            val id = jsonCategoryNow.getInt("id")
            val title = jsonCategoryNow.getString("title")
            val jsonArrayMovies = jsonCategoryNow.getJSONArray("movie")

            val movies = mutableListOf<Movie>()

            for (j in 0 until jsonArrayMovies.length()){
                val jsonMovieNow = jsonArrayMovies.getJSONObject(j)

                val idMovie = jsonMovieNow.getInt("id")
                val coverUrl = jsonMovieNow.getString("cover_url")

                movies.add(Movie(idMovie, coverUrl))
            }

            categories.add(Category(id, title, movies))
        }

        return categories
    }

    private fun toString(stream: InputStream) : String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int

        while(true) {
            read = stream.read(bytes)
            if (read <= 0) {
                break
            }
            baos.write(bytes, 0, read)
        }
        return String(baos.toByteArray())
    }
}