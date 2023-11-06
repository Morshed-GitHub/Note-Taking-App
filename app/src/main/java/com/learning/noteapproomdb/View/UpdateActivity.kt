package com.learning.noteapproomdb.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.learning.noteapproomdb.R

class UpdateActivity : AppCompatActivity() {
    private lateinit var etTitle : EditText
    private lateinit var etDescription : EditText
    private lateinit var cancel : Button
    private lateinit var save : Button

    private var noteId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        supportActionBar?.title = "Update Note" // Change the AppBar name

        etTitle = findViewById(R.id.editTextTitleUpdate)
        etDescription = findViewById(R.id.editTextDescriptionUpdate)
        cancel = findViewById(R.id.buttonCancelUpdate)
        save = findViewById(R.id.buttonSaveUpdate)

        getAndSetData()

        save.setOnClickListener {
            updateNote()
        }

        cancel.setOnClickListener {
            finish() // Close the current activity
            Toast.makeText(applicationContext, "Nothing updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateNote() {
        val updatedTitle : String = etTitle.text.toString()
        val updatedDescription : String = etDescription.text.toString()

        // Set updated data in current activity (UpdateActivity) and passed this set data to the calling activity (MainActivity) through Intent
        val intent : Intent = Intent()
        intent.putExtra("updatedTitle", updatedTitle)
        intent.putExtra("updatedDescription", updatedDescription)
        if (noteId != -1) {
            intent.putExtra("noteId", noteId)
            setResult(RESULT_OK, intent)
            finish() // Close the current activity
        }
    }

    private fun getAndSetData(){
        // Getter
        val noteTitle : String = intent.getStringExtra("currentTitle").toString()
        val noteDescription : String = intent.getStringExtra("currentDescription").toString()
        noteId = intent.getIntExtra("currentId", -1)

        // Setter
        etTitle.setText(noteTitle)
        etDescription.setText(noteDescription)
    }
}