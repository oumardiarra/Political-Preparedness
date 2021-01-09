package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.database.model.Election

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( elections: List<Election>)
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>
    @Query("select * from election_table where id=:id")
    fun getSingleElection(id: Int): LiveData<Election>
    @Delete
    suspend fun deleteElection(election: Election)
    @Query("delete from election_table")
    suspend fun deleteAllElections()

}