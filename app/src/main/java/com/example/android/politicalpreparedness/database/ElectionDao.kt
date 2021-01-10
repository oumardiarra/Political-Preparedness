package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.database.model.Election

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(elections: List<Election>)

    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    @Query("select * from election_table where id=:id")
    fun getSingleElection(id: Int): LiveData<Election>

    @Delete
    suspend fun deleteElection(election: Election)

    @Query("delete from election_table")
    suspend fun deleteAllElections()

    @Query("update election_table set isSaved=1 where id in (:electionIds)")
    suspend fun updateElections(electionIds: List<Int>)

    @Query("update election_table set isSaved=1 where id=:electionId")
    suspend fun saveElection(electionId: Int)

    @Query("select * from election_table where isSaved=1")
     fun getAllSavedElection(): LiveData<List<Election>>
    @Query("select * from election_table where isSaved=1")
    suspend fun getSavedElection(): List<Election>
    @Query("update election_table set isSaved=0 where id=:electionId")
    suspend fun unFollowElection(electionId: Int)


}