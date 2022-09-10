package com.example.filters.ImageFilter

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.filters.databinding.ActivityFilterBinding
import com.zomato.photofilters.FilterPack
import com.zomato.photofilters.imageprocessors.Filter

class FilterActivity : AppCompatActivity(), FilterAdapter.ThumnailClickListener {

    companion object {

        init {
            System.loadLibrary("NativeImageProcessor")
        }

    }

    private var mBitmap: Bitmap? = null
    private var filterBitmap: Bitmap? = null
    private lateinit var binding: ActivityFilterBinding
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imageUri = intent.getParcelableExtra<Uri>("imageUri")
        supportActionBar?.hide()

        mBitmap = ImageUtils.getBitmap(imageUri!!, contentResolver)


        Glide.with(this).load(imageUri).into(binding.imageView)

        processRecyclerView()

        shareIt()


    }


    private fun processRecyclerView() {


        imageUri?.let {

            val bitmap = ImageUtils.getBitmap(it, contentResolver)
            val filterPack = FilterPack.getFilterPack(this)

            ThumnailManager.clearThumnail()
            for (filter in filterPack) {
                val thumnailItem = ThumbnailItem(bitmap, filter)
                ThumnailManager.addThumnailItem(thumnailItem)


            }

            val list = ThumnailManager.processThumnail()


            val adapter = FilterAdapter(list, imageUri!!, this)
            binding.rec.adapter = adapter
            binding.rec.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rec.setHasFixedSize(true)

        }

    }

    override fun onThumnailClickListener(filter: Filter) {

        filterBitmap = filter.processFilter(
            Bitmap.createScaledBitmap(
                mBitmap!!,
                mBitmap?.width ?: 1000,
                mBitmap?.height ?: 1000,
                false
            )
        )



        Glide.with(applicationContext).load(
            filterBitmap

        ).into(binding.imageView)


        Toast.makeText(this, filter.name, Toast.LENGTH_SHORT).show()


    }

    private fun shareIt() {

        binding.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            shareIntent.putExtra(Intent.EXTRA_STREAM,
                filterBitmap?.let { filterOne -> ImageUtils.saveImage(filterOne, this) })
            startActivity(shareIntent)

        }


    }
}