package co.tiagoaguiar.netflixremake

import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Movie

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        setSupportActionBar(findViewById(R.id.toolbar_movie_detail))
        val actionBar = supportActionBar
        actionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        val txtTitle: TextView = findViewById(R.id.text_title_movie)
        val txtDesc: TextView = findViewById(R.id.text_desc)
        val txtCast: TextView = findViewById(R.id.text_cast)
        val rv: RecyclerView = findViewById(R.id.recycler_similar)

        val list = mutableListOf<Movie>()

        txtTitle.text = "Meu Ovo - O Esquerdo"
        txtDesc.text = "Meu ovo equedo é o mais louco que existe, ele é muito doidão"
        txtCast.text = getString(R.string.cast, "Meu ovo esquerdo, o direito, meu p1nt0")
        rv.layoutManager = GridLayoutManager(this, 3)
        rv.adapter = AdapterMovieInCategory(list, R.layout.movie_item_similar) {
            Toast.makeText(this, "Agora não pai", Toast.LENGTH_SHORT).show()
        }

        //cd5f8656-687d-4ba0-a4e7-3f29c90f801a

        val img: ImageView = findViewById(R.id.image_detail)
        val layerDrawable = ContextCompat.getDrawable(this, R.drawable.shadows) as LayerDrawable
        val shadowImageView = ContextCompat.getDrawable(this, R.drawable.movie_4)
        layerDrawable.setDrawableByLayerId(R.id.image_shadow, shadowImageView)
        img.setImageDrawable(layerDrawable)

    }
}