package com.learning.noteapproomdb.Model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table") // Declare this class as entity. Pass table name.
// Each Entity represents a Table in Database
class Note(val title : String, val description: String) { // Takes two field values from user through constructor
    @PrimaryKey(autoGenerate = true) // Given unique identity to each data entry, which will be auto generated
    var id : Int = 0
}