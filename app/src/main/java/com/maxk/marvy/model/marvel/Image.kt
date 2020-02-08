package com.maxk.marvy.model.marvel

import android.net.Uri
import com.maxk.marvy.model.ImageSize

data class Image(val path: String?, val extension: String?) {
    companion object {
        private fun buildImageUrl(path: String, name: String, extension: String): String {
            val url = Uri.parse("$path/$name.$extension").buildUpon()
            url.scheme("https")
            return url.build().toString()
        }
    }

    fun url(width: Int, height: Int): String? {
        if (path == null || extension == null) {
            return null
        }

        return ImageVariant.image(ImageSize(width, height))?.let {
            buildImageUrl(path, it.name, extension)
        }
    }

    fun thumbnailUrl(width: Int, height: Int): String? {
        if (path == null || extension == null) {
            return null
        }

        return ImageVariant.thumbnail(ImageSize(width, height))?.let {
            buildImageUrl(path, it.name, extension)
        }
    }
}

