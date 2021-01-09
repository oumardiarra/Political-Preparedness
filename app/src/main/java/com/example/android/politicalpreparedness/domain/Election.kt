package com.example.android.politicalpreparedness.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Election(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division
) : Parcelable