package com.example.rsed

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


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

        val ims = assets.open("logo_tsp.png")
        val buffer = ByteArray(1024)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (ims.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } finally {
            ims.close()
        }

        val resbuffer = output.toByteArray()
        val size = resbuffer.size
        println(size)
        val requestFile= resbuffer.toRequestBody("image/*".toMediaTypeOrNull(), 0,resbuffer.size)
        val body = MultipartBody.Part.createFormData("image", "myImage.png", requestFile)
        val call = RetrofitClient.apiService.uploadImage(body)

        println("Enqueing the file")
            // Execute the call asynchronously
        call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                        val responseBody = response.body()
                        println("success")
                        println(responseBody)
                    }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("failed")
                    println(t)
                }
            })
        }
    }
