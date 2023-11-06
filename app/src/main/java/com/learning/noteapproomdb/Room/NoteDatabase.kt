package com.learning.noteapproomdb.Room
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.learning.noteapproomdb.Model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// This class will connect DAO & Entity with each other
@Database(entities = [Note::class], version = 1)
// Entities, Version -> Need to change version number whenever any update occurs in database
abstract class NoteDatabase : RoomDatabase() { // Must be an Abstract Class and inherit from RoomDatabase
    abstract fun getNoteDao() : NoteDAO

    // Singleton -> Need to prevent creation of more than one instance/ object of database
    companion object { // In order to access the instance of database from anywhere on the application
        @Volatile // INSTANCE will be visible to all other thread's (Updated Value Case)
        private var INSTANCE : NoteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : NoteDatabase{
            return INSTANCE ?: synchronized(this){ // Locking Mechanism
                // If more than one thread tries to create an instance of the
                // database at the same time, it's going to block it
                // In short, it allows creation of database instance one at a time
                val instance = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "note_database") // Context, NoteDatabase Class, Database Name
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance  // In order to return instance object
            }
        }
    }

    // In order pre add data's in the table
    private class NoteDatabaseCallback(private val scope : CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let { database ->
            // Room doesn't allow us to do this task on main thread. Must be on coroutine
            // database.getNoteDao().insert(Note("t", "d"))

                // Coroutine Scope
                scope.launch {
                    val dao = database.getNoteDao()
                    dao.insert(Note("title 1", "description 1"))
                    dao.insert(Note("title 2", "description 2"))
                    dao.insert(Note("title 3", "description 3"))
                }

            }
        }
    }
}