package com.example.filters.ImageFilter

import android.graphics.Bitmap

object ThumnailManager {


    //empty initialization
    var thumbnailItem:ArrayList<ThumbnailItem> = ArrayList()

    fun addThumnailItem(item: ThumbnailItem)
    {
        thumbnailItem.add(item)


    }



    fun processThumnail(): ArrayList<ThumbnailItem> {


        for (thumb in thumbnailItem)
        {

            thumb.image = thumb.image?.let { Bitmap.createScaledBitmap(it,120,200,false) }
            thumb.image = thumb.filter.processFilter(thumb.image)


        }
        return thumbnailItem



    }

    fun clearThumnail()
    {

        thumbnailItem.clear()

    }


}