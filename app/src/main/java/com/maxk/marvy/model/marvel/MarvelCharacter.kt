package com.maxk.marvy.model.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarvelCharacter(val name: String?,
                           val description: String?,
                           val thumbnail: Image?) : Parcelable