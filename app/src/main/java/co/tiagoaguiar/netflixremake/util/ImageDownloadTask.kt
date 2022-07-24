package co.tiagoaguiar.netflixremake.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection

class ImageDownloadTask(private val callback: Callback) {

    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    fun execute(url: String) {

        executor.execute {

            var urlConnection: HttpsURLConnection? = null
            var stream: InputStream? = null

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
                val bitmap = BitmapFactory.decodeStream(stream)

                handler.post{
                    callback.onResult(bitmap)
                }
            } catch (e: IOException) {
                val message = e.message ?: "ERROR DESCONHECIDO"
                Log.e("ERROR", message, e)
            } finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }

}