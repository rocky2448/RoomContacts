package com.rocky.roomcontakts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "timesTamp") var timesTamp: String
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}