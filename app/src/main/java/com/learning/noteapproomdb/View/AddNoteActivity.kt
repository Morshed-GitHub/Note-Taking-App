package com.learning.noteapproomdb.View

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.learning.noteapproomdb.R

class AddNoteActivity : AppCompatActivity() {

    private lateinit var etTitle : EditText
    private lateinit var etDescription : EditText
    private lateinit var cancel : Button
    private lateinit var save : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        supportActionBar?.title = "Add Note" // Change the AppBar name

        etTitle = findViewById(R.id.editTextTitle)
        etDescription = findViewById(R.id.editTextDescription)
        cancel = findViewById(R.id.buttonCancel)
        save = findViewById(R.id.buttonSave)

        save.setOnClickListener {
            saveNote()
        }

        cancel.setOnClickListener {
            finish() // Close the current activity
            Toast.makeText(applicationContext, "Nothing saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNote() {
        val title : String = etTitle.text.toString()
        val description : String = etDescription.text.toString()

        // Another way of sending data to another activity through intent
        val intent : Intent = Intent() // We will start the intent in main activity to get a result, that's why no parameter
        intent.putExtra("title", title)
        intent.putExtra("description", description)
        setResult(RESULT_OK, intent)
        finish() // Close the current activity
    }
}