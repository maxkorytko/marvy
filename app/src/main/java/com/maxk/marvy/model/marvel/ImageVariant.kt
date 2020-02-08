package com.maxk.marvy.model.marvel

import com.maxk.marvy.model.ImageSize

/**
 * A list of Marvel provided image variants.
 */
private val variants: List<ImageVariant> = listOf(
    // Portrait
    ImageVariant(50, 75, "portrait_small"),
    ImageVariant(168, 252, "portrait_fantastic"),
    // Square
    ImageVariant(45, 45, "standard_small"),
    ImageVariant(250, 250, "standard_fantastic"),
    // Landscape
    ImageVariant(50, 75, "landscape_small"),
    ImageVariant(250, 156, "landscape_amazing")
)

class ImageVariant(val size: ImageSize, val name: String) {
    constructor(width: Int, height: Int, name: String): this(ImageSize(width, height), name)

    companion object {
        private fun select(imageSize: ImageSize): List<ImageVariant> {
            return variants.filterByAspectRatio(imageSize.aspectRatio)
                .sortedBy { it.size.width }
                .sortedBy { it.size.height }
        }

        fun image(imageSize: ImageSize): ImageVariant? {
            val selectedVariants = select(imageSize)

            val matchingWidth = selectedVariants.filterOrNullIfEmpty {
                it.size.width >= imageSize.width } ?: selectedVariants

            val matchingVariant = matchingWidth.firstOrNull { it.size.height >= imageSize.height }
            return matchingVariant ?: matchingWidth.lastOrNull()
        }

        fun thumbnail(imageSize: ImageSize): ImageVariant? {
            return select(imageSize).firstOrNull()
        }
    }
}

private fun List<ImageVariant>.filterByAspectRatio(aspectRatio: ImageSize.AspectRatio):
        List<ImageVariant> = when {
    aspectRatio.isPortrait -> filter { it.size.aspectRatio.isPortrait }
    aspectRatio.isLandscape -> filter { it.size.aspectRatio.isLandscape }
    else -> filter { it.size.aspectRatio.isSquare }
}

private fun List<ImageVariant>.filterOrNullIfEmpty(predicate: (ImageVariant) -> Boolean):
        List<ImageVariant>? {

    val filteredList = filter(predicate)
    return if (filteredList.isEmpty()) null else filteredList
}
