package com.maxk.marvy.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.maxk.marvy.R
import com.maxk.marvy.extensions.resolveAttribute

class UrlImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatImageView(context, attrs, defStyleAttr) {

    var cornerRadius: Float

    var imageUrl: String? = null

    var thumbnailUrl: String? = null

    var placeholder: Drawable? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.UrlImageView,
            defStyleAttr,
            0
        ).apply {
            try {
                cornerRadius = getDimension(R.styleable.UrlImageView_cornerRadius, 0f)
                placeholder = getDrawable(R.styleable.UrlImageView_placeholder)
            } finally {
                recycle()
            }
        }
    }

    fun fetchImage() {
        Glide.with(context).clear(this)

        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .thumbnail(Glide.with(context).asBitmap().load(thumbnailUrl))
            .placeholder(placeholder)
            .error(ColorDrawable(context.getColor(R.color.colorSurface)))
            .into(object : BitmapImageViewTarget(this) {
                override fun setResource(resource: Bitmap?) {
                    if (cornerRadius > 0f) {
                        setImageDrawable(roundCorners(resource))
                    } else {
                        super.setResource(resource)
                    }
                }
            })
    }

    private fun roundCorners(bitmap: Bitmap?): RoundedBitmapDrawable {
        return RoundedBitmapDrawableFactory.create(resources, bitmap).apply {
            cornerRadius = this@UrlImageView.cornerRadius
            setAntiAlias(true)
        }
    }
}

@BindingAdapter("imageUrl", "thumbnailUrl", requireAll = false)
fun setImageUrl(imageView: UrlImageView, url: String?, thumbnailUrl: String?) {
    imageView.imageUrl = url
    imageView.thumbnailUrl = thumbnailUrl
    imageView.fetchImage()
}
