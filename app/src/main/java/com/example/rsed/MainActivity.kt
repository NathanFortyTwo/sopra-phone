package com.example.rsed

import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rsed.ui.theme.RsedTheme
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var sendRequestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val myButton: Button = findViewById(R.id.sendRequestButton)
        myButton.setOnClickListener {
            Log.d("MainActivity", "Hello World!")
            sendDataToApi()
        }
    }


    private fun sendDataToApi() {
        val apiService = RetrofitClient.apiService
        val call = apiService.sendData("78")

        call.enqueue(object : Callback<String> { // Change ApiResponse to String
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    // Log the response
                    Log.d("ApiCall", "Response: $result")
                } else {
                    // Handle error
                    Log.e("ApiCall", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // Handle failure
                Log.e("ApiCall", "Failure: ${t.message}")
            }
        })
    }
}