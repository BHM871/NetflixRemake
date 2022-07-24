package co.tiagoaguiar.netflixremake

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.ImageDownloadTask
import com.squareup.picasso.Picasso

class AdapterMain(private val context: Context, private val layoutInflaterRes: Int, val list: MutableList<Category>) : RecyclerView.Adapter<AdapterMain.HolderMain>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMain =
        HolderMain(LayoutInflater.from(parent.context).inflate(layoutInflaterRes, parent, false))

    override fun onBindViewHolder(holder: HolderMain, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class HolderMain(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(item: Category){
            itemView.findViewById<TextView>(R.id.text_title_category).text = item.title

            val rv = itemView.findViewById<RecyclerView>(R.id.recycler_category_in_main)
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val a = AdapterMovieInCategory(item.movies, R.layout.movie_item){
                context.startActivity(Intent(context, MovieDetailActivity::class.java))
            }
            rv.adapter = a
        }

    }
}

class AdapterMovieInCategory(private val list: List<Movie>, @LayoutRes val layoutRes: Int, val listener: (() -> Unit)?) : RecyclerView.Adapter<AdapterMovieInCategory.HolderMovieInCategory>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderMovieInCategory =
        HolderMovieInCategory(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))

    override fun onBindViewHolder(holder: HolderMovieInCategory, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class HolderMovieInCategory(itemView: View) : RecyclerView.ViewHolder(itemView), ImageDownloadTask.Callback{

        private val imgView: ImageView = itemView.findViewById(R.id.image_movie_in_category)

        fun bind(item: Movie) {
            ImageDownloadTask(this@HolderMovieInCategory).execute(item.coverUrl)
            //Picasso.get().load(item.coverUrl).placeholder(R.drawable.placeholder_movies).into(this)

            imgView.setOnClickListener{
                listener?.invoke()
            }
        }

        override fun onResult(bitmap: Bitmap) {
            imgView.setImageBitmap(bitmap)
        }

    }
}