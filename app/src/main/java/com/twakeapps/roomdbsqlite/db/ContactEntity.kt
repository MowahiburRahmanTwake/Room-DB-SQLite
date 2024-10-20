package com.twakeapps.roomdbsqlite.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbConstants.TABLE_CONTACT_2)
data class ContactEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "first_name") val firstName:String?,
        @ColumnInfo(name = "last_name") val lastName:String?,
        @ColumnInfo(name = "phone_number") val phoneNumber:String?
                        )
