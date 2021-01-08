package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")
@Parcelize
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
):Parcelable

/*
fun List<Election>.asDomainModel():List<com.example.android.politicalpreparedness.domain.Election>{
    return map {
        com.example.android.politicalpreparedness.domain.Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division
        )
    }

}*/
