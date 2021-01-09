package com.example.android.politicalpreparedness.database.model

data class Division(
        val id: String,
        val country: String,
        val state: String
)

fun Division.asDomainModel(): com.example.android.politicalpreparedness.domain.Division {
    return com.example.android.politicalpreparedness.domain.Division(
            id = this.id,
            country = this.country,
            state = this.state
    )
}