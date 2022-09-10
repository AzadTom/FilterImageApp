package com.example.filters.ImageFilter


import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

object ImageUtils {

    fun getOutputMediaFileUri(mediaType: Int, context: Context): Uri? {
        // check for external storage
        if (isExternalStorageAvailable()) {
            // get the URI

            // 1. Get the external storage directory
            val mediaStorageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

            // 2. Create a unique file name
            var fileName = ""
            var fileType = ""
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            when (mediaType) {
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> {
                    fileName = "IMG_$timeStamp"
                    fileType = ".jpg"
                }
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> {
                    fileName = "VID_$timeStamp"
                    fileType = ".mp4"
                }
                else -> {
                    return null
                }
            }
            // 3. Create the file
            val mediaFile: File
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir)
                val tempUri = FileProvider.getUriForFile(context, context.packageName +".FileProvider", mediaFile)
                Log.i(
                    "DesiCoder",
                    "File: $tempUri"
                )

                // 4. Return the file's URI
                return tempUri
            } catch (e: IOException) {
                Log.e(
                    "DesiCoder", "Error creating file: " +
                            mediaStorageDir!!.absolutePath + fileName + fileType
                )
            }
        }

        return null
    }


    private fun isExternalStorageAvailable() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    fun getBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        var inputStream: InputStream? = null
        try {
            val IMAGE_MAX_SIZE = 1200000
            inputStream = contentResolver.openInputStream(uri)
            var options: BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inMutable = true
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            var scale = 1
            while ((options.outWidth * options.outHeight) * (1 / scale.toDouble().pow(2.0)) >
                IMAGE_MAX_SIZE
            ) {
                scale++;
            }
            var resultBitmap: Bitmap? = null
            inputStream = contentResolver.openInputStream(uri)
            if (scale > 1) {
                scale--;
                options = BitmapFactory.Options()
                options.inSampleSize = scale
                options.inMutable = true
                resultBitmap = BitmapFactory.decodeStream(inputStream, null, options)
                val height: Double = resultBitmap?.height?.toDouble() ?: 0.0
                val width: Double = resultBitmap?.width?.toDouble() ?: 0.0
                val y = sqrt(IMAGE_MAX_SIZE / (width / height))
                val x = (y / height) * width
                val scaledBitmap =
                    Bitmap.createScaledBitmap(resultBitmap!!, x.toInt(), y.toInt(), true)
                resultBitmap.recycle()
                resultBitmap = scaledBitmap
                System.gc()
            } else {
                resultBitmap = BitmapFactory.decodeStream(inputStream)
            }
            inputStream?.close()
            return resultBitmap
        } catch (e: Exception) {
            return null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun saveImage(image: Bitmap, context: Context): Uri? {
        if (isExternalStorageAvailable()) {
            val mediaStorageDir =
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val timeStamp =
                SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "IMG_$timeStamp"
            val fileType = ".jpg"

            val mediaFile: File
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir)
                val fos = FileOutputStream(mediaFile)
                image.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush()
                fos.close()
                val uri = FileProvider.getUriForFile(context, context.packageName +".FileProvider", mediaFile)

                return uri
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return null
    }
}
