package com.maxk.marvy.model

class ImageSize(val width: Int, val height: Int) {
    class AspectRatio(ratio: Float) {
        val isPortrait: Boolean = ratio < 1f
        val isLandscape: Boolean = ratio > 1f
        val isSquare: Boolean = ratio == 1f
    }

    val aspectRatio: AspectRatio = AspectRatio(width / height.toFloat())
}