package co.tiagoaguiar.netflixremake

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {

    private lateinit var list: MutableList<Category>
    private lateinit var adapterMain: AdapterMain

    private lateinit var progress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = findViewById(R.id.progress_main)

        list = mutableListOf()

        adapterMain = AdapterMain(this, R.layout.category_item, list)
        val rvMain: RecyclerView = findViewById(R.id.recycler_main)
        rvMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMain.adapter = adapterMain

        CategoryTask(this).execute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=cd5f8656-687d-4ba0-a4e7-3f29c90f801a")
    }

    override fun onPreExecute() {
        progress.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResult(categories: List<Category>) {
        this.list.clear()
        this.list.addAll(categories)
        adapterMain.notifyDataSetChanged()
        progress.visibility = View.GONE
    }

    override fun onFailure(message: String) {
        progress.visibility = View.GONE
    }

}