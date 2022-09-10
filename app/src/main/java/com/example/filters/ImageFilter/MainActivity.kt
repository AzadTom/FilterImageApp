package com.example.filters.ImageFilter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.widget.Toast
import com.example.filters.databinding.ActivityMainBinding
import java.net.URI

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    companion object
    {
      const val IMAGE_CAPTURE_CODE =1
    }

    private var mimageURL :Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



       binding.btn.setOnClickListener {


           openCamera()



       }


        sendSendClickListener()




    }

    private fun sendSendClickListener() {

    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mimageURL = ImageUtils.getOutputMediaFileUri(MEDIA_TYPE_IMAGE,this)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,mimageURL)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         if(requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK)
         {

             val i = Intent(this@MainActivity ,FilterActivity::class.java)
             i.putExtra("imageUri",mimageURL)

             startActivity(i)




         }
    }
}