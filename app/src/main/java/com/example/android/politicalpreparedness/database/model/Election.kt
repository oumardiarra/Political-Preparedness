package com.example.android.politicalpreparedness.database.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") val division: Division,
        val isSaved: Boolean = false
)

fun List<Election>.asDomainModel(): List<com.example.android.politicalpreparedness.domain.Election> {
    return map {
        com.example.android.politicalpreparedness.domain.Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDomainModel(),
                isSaved = it.isSaved
        )
    }
}