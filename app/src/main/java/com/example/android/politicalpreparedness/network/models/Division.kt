package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable



fun Division.asDataBaseModel(): com.example.android.politicalpreparedness.database.model.Division {
    return com.example.android.politicalpreparedness.database.model.Division(
            id=this.id,
            country = this.country,
            state = this.state
    )

}