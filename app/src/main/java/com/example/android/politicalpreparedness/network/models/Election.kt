package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*


data class Election(
        val id: Int,
        val name: String,
        val electionDay: Date,
        @Json(name = "ocdDivisionId") val division: Division
)

fun List<Election>.asDataBaseModel(): List<com.example.android.politicalpreparedness.database.model.Election> {
    return map {
        com.example.android.politicalpreparedness.database.model.Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDataBaseModel()
        )
    }

}
