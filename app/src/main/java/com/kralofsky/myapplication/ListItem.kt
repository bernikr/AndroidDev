package com.kralofsky.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListItem(val title: String, val imageId: Int, val description: String) : Parcelable {
}