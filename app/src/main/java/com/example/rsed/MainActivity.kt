package com.example.rsed

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
        var counter = 0
        setContentView(R.layout.activity_main)
        val myButton: Button = findViewById(R.id.sendRequestButton)
        myButton.setOnClickListener {
            Log.d("MainActivity", "Hello World!")
            counter %= 3
            counter+=1

            val suffix = counter.toString()
            val image_name = "image_"+suffix+".png"
            sendDataToApi(image_name)

        }
    }


    private fun sendDataToApi(image_name:String) {

        val ims = assets.open(image_name)
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
        val bitmap: Bitmap = BitmapFactory.decodeByteArray(resbuffer, 0, resbuffer.size)

        // Display the Bitmap in an ImageView
        val imageView: ImageView = findViewById(R.id.imageView)
        imageView.setImageBitmap(bitmap)

        val size = resbuffer.size
        println(size)
        val requestFile= resbuffer.toRequestBody("image/*".toMediaTypeOrNull(), 0,resbuffer.size)
        val body = MultipartBody.Part.createFormData("file", "myImage.png", requestFile)
        val call = RetrofitClient.apiService.uploadImage(body)

        // Execute the call asynchronously
        call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                        val responseBody = response.body()
                        println(responseBody)
                        val tvMessage: TextView = findViewById(R.id.tvMessage)
                         tvMessage.text = responseBody
                    }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println("failed")
                    val tvMessage: TextView = findViewById(R.id.tvMessage)
                    tvMessage.text = "Something went wrong :( "


                }
            })
        }
    }
