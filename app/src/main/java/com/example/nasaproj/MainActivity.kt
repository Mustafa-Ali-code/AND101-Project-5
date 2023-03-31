package com.example.nasaproj

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var currentDate: LocalDate

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize currentDate with today's date
        currentDate = LocalDate.now()

        // Call the function to fetch the APOD data
        fetchAPOD()

        // Set up the button to fetch a new APOD image and text when pressed
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            // Decrement currentDate by one day to get the previous day's date
            currentDate = currentDate.minusDays(1)

            // Call the function to fetch the new APOD data
            fetchAPOD()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchAPOD() {
        val client = AsyncHttpClient()

        // NASA APOD API key
        val apiKey = "7nOoBGTUEV25vuXvaa78kOrpSm2wCfTSL5jJScNB"

        // Format the date as YYYY-MM-DD
        val date = currentDate.format(DateTimeFormatter.ISO_DATE)

        // APOD API URL for the specified date
        val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey&date=$date"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                // Parse the JSON response and get the URL of the image
                val imageUrl = json.jsonObject.getString("url")

                // Get the title and explanation of the image
                val title = json.jsonObject.getString("title")
                val explanation = json.jsonObject.getString("explanation")

                // Load the image into the background of the activity
                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .centerCrop()
                    .into(findViewById<ImageView>(R.id.img))

                // Set the title and explanation in the text views
                findViewById<TextView>(R.id.apodtext1).text = title
                findViewById<TextView>(R.id.apodtext2).text = explanation

                Log.d("NASA APOD", "Image URL: $imageUrl")
            }


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.d("NASA APOD Error", response.toString())
            }
        })
    }
}
