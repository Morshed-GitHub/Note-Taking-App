package com.learning.noteapproomdb.Room
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.learning.noteapproomdb.Model.Note

@Dao // Consider it as DAO through Annotation declaration
interface NoteDAO {

    @Insert // Just Annotate this is Insert method & Room database will do the rest, no need to define its body
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)
    // By default, Room database doesn't allow us to do these query operations in Main Thread.
    // So we use suspend operation from coroutine which can execute tasks in different thread asynchronously

    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAllNotes() : kotlinx.coroutines.flow.Flow<List<Note>>
    // In order to get live data, we need to make it as observable
    // Here we don't use suspend operation from kotlin coroutine,
    // because Room database already do this without using Main Thread

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()

}