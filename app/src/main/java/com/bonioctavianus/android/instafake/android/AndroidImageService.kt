package com.bonioctavianus.android.instafake.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bonioctavianus.android.instafake.BuildConfig
import com.bonioctavianus.android.instafake.InstaApp
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.service.ImageService
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AndroidImageService(
    private val mApplication: InstaApp
) : ImageService {

    override fun requestPictureFromCamera(fragment: Fragment, requestCode: Int): String? {
        val context = fragment.context ?: return null

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context.packageManager)?.also {
                val photoFile = try {
                    createImageFile(context)
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoPath = it.absolutePath
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        BuildConfig.APPLICATION_ID.plus(".fileprovider"),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(takePictureIntent, requestCode)
                    return photoPath
                }
            }
        }
        return null
    }

    @Throws(IOException::class)
    private fun createImageFile(context: Context): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = timeStamp.plus(".jpg")
        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            context.getString(R.string.app_name)
        )
        return try {
            if (!path.exists()) {
                path.mkdirs()
            }
            File(path, fileName)
        } catch (e: Exception) {
            null
        }
    }

    override fun requestPictureFromGallery(fragment: Fragment, requestCode: Int): Boolean {
        val context = fragment.context ?: return false
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        return if (intent.resolveActivity(context.packageManager) != null) {
            fragment.startActivityForResult(intent, requestCode)
            true
        } else {
            false
        }
    }

    override fun scanMediaForImage(uri: String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(uri)
            mediaScanIntent.data = Uri.fromFile(f)
            mApplication.sendBroadcast(mediaScanIntent)
        }
    }
}