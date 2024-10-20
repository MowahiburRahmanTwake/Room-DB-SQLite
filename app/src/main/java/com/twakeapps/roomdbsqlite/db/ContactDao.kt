package com.twakeapps.roomdbsqlite.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactDao
{
    @Query("SELECT * FROM ${DbConstants.TABLE_CONTACT_2}")
    suspend fun getAllContract():List<ContactEntity>

    @Insert
    suspend fun insertAll(vararg contracts: ContactEntity)

    @Update
    suspend fun updateData(vararg contracts : ContactEntity )

    @Delete
    suspend fun deleteData(vararg contracts : ContactEntity)

}