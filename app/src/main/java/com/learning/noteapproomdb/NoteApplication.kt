package com.learning.noteapproomdb
import android.app.Application
import com.learning.noteapproomdb.Repository.NoteRepository
import com.learning.noteapproomdb.Room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication : Application() { // Need to specify this in "AndroidManifest.xml" file
    // This function creates a supervisor job object in an active state
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Property delegation used in kotlin. Instance will be created only once
    // when the application first needed. Not every time it run's.
    private val database by lazy {
        NoteDatabase.getDatabase(this, applicationScope)
    }
    val repository by lazy {
        NoteRepository(database.getNoteDao())
    }
}