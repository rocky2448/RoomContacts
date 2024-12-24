package com.rocky.roomcontakts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person_table")
data class Person(
    @ColumnInfo(name = "text") var name: String,
    @ColumnInfo(name = "phone") var phone: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}