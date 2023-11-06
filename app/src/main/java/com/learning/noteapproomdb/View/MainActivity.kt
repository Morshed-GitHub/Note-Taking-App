package com.learning.noteapproomdb.View

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learning.noteapproomdb.Adapters.NoteAdapter
import com.learning.noteapproomdb.Model.Note
import com.learning.noteapproomdb.NoteApplication
import com.learning.noteapproomdb.R
import com.learning.noteapproomdb.ViewModel.NoteViewModel
import com.learning.noteapproomdb.ViewModel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.recyclerView)
        // LayoutManager provides data to be listed from top to bottom
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteAdapter : NoteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter

        // Register activity for result
        registerActivityResultLauncher()

        var noteViewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        noteViewModel =
            ViewModelProvider(this, noteViewModelFactory)[NoteViewModel::class.java] // Activity/ Owner, View Model Factory
        noteViewModel.myAllNotes.observe(this, Observer { notes ->
            // Update the UI
            noteAdapter.setNote(notes) // When data changes, corresponding data should be updated
        })

        // Can't create instance of an Abstract Class that's why indicate it as anonymous object
        ItemTouchHelper(object : ItemTouchHelper
        // 0 -> for drag & drop direction, Swipe Left Direction
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val selectedNotePosition = viewHolder.adapterPosition
                val selectedNote : Note = noteAdapter.getNote(selectedNotePosition)
                Toast.makeText(applicationContext, "${selectedNote.title} deleted successfully ✔", Toast.LENGTH_SHORT).show()
                noteViewModel.delete(selectedNote)
            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun registerActivityResultLauncher() {
        addActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback{ resultAddNote ->
                val resultCode = resultAddNote.resultCode
                val resultData = resultAddNote.data

                if (resultCode == RESULT_OK && resultData != null) {
                    val noteTitle : String = resultData.getStringExtra("title").toString()
                    val noteDescription : String = resultData.getStringExtra("description").toString()
                    val note = Note(noteTitle, noteDescription)
                    noteViewModel.insert(note)
                    Toast.makeText(applicationContext, "$noteTitle inserted successfully ✔", Toast.LENGTH_SHORT).show()
                }
        })

        updateActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback{ resultUpdateNote ->
                val resultCode = resultUpdateNote.resultCode
                val resultData = resultUpdateNote.data

                if (resultCode == RESULT_OK && resultData != null) {
                    val updatedTitle : String = resultData.getStringExtra("updatedTitle").toString()
                    val updatedDescription : String = resultData.getStringExtra("updatedDescription").toString()
                    val noteId : Int = resultData.getIntExtra("noteId", -1)
                    val newNote : Note = Note(updatedTitle, updatedDescription)
                    newNote.id = noteId
                    noteViewModel.update(newNote)
                    Toast.makeText(applicationContext, "$updatedTitle updated successfully ✔", Toast.LENGTH_SHORT).show()
                }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu) // Resource Menu, Menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_note -> {
                val intent : Intent = Intent(this, AddNoteActivity::class.java)
                addActivityResultLauncher.launch(intent) // Now we can register activity for a result
            }
            R.id.delete_note -> showDialogMessage()
        }
        return true
    }

    private fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Notes")
            .setMessage("If click Yes all notes will delete, if you want to delete any specific note, please swipe left or right.")
        dialogMessage.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
            noteViewModel.deleteAll()
            Toast.makeText(applicationContext, "All notes deleted successfully ✔", Toast.LENGTH_LONG).show()
        })
        dialogMessage.create().show()
    }
}