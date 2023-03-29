package com.gaurav.serviceproviderapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import androidx.navigation.NavController
import androidx.navigation.findNavController
import java.io.ByteArrayOutputStream
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController=findNavController(R.id.navHost)
    }
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    //use this method to convert the selected image to bitmap to save in database
    fun encodeTobase64(image: Bitmap): String? {
        var imageEncoded: String = ""
        var imageConverted = getResizedBitmap(image, 500)
        val baos = ByteArrayOutputStream()
        imageConverted?.let {
            imageConverted.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b: ByteArray = baos.toByteArray()
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        }

        return imageEncoded
    }

    //use this method to convert the saved string to bitmap
    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory
            .decodeByteArray(decodedByte, 0, decodedByte.size)
    }
}