package com.learning.noteapproomdb.Repository
import androidx.annotation.WorkerThread
import com.learning.noteapproomdb.Model.Note
import com.learning.noteapproomdb.Room.NoteDAO
import kotlinx.coroutines.flow.Flow

// Don't need to expose whole DB, instead we have to use NoteDAO for CRUD operations
class NoteRepository(private val noteDao : NoteDAO) {
    val allNotes : Flow<List<Note>> = noteDao.getAllNotes()

    @WorkerThread // Operation can be done in a Single Thread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }
    @WorkerThread // Operation can be done in a Single Thread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }
    @WorkerThread // Operation can be done in a Single Thread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
    @WorkerThread // Operation can be done in a Single Thread
    suspend fun deleteAll() : Unit {
        noteDao.deleteAllNotes()
    }

    /* Our goal is to create only one instance of both the database and the repository
       An easy way is that, create a new class that inherits from the Application class
       Then they will be just retrieved from the application whenever they are needed
       rather than construct it every time */
}