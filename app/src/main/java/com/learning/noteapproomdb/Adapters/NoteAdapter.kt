package com.learning.noteapproomdb.Adapters
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.learning.noteapproomdb.Model.Note
import com.learning.noteapproomdb.R
import com.learning.noteapproomdb.View.MainActivity
import com.learning.noteapproomdb.View.UpdateActivity

class NoteAdapter(private val activity : MainActivity) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() { // Connect NoteViewHolder with NoteAdapter

    var notes : List<com.learning.noteapproomdb.Model.Note> = ArrayList()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) { // Hold's the View Components
        val tvTitle : TextView = itemView.findViewById(R.id.textViewTitle)
        val tvDescription : TextView = itemView.findViewById(R.id.textViewDescription)
        val cardView : CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item, parent, false) // layout, ViewGroup?, attachToRoot
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size // Length of the Recycler View
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        var currentNote : com.learning.noteapproomdb.Model.Note = notes[position]
        holder.tvTitle.text = currentNote.title
        holder.tvDescription.text = currentNote.description

        holder.cardView.setOnClickListener {
            val intent : Intent = Intent(activity, UpdateActivity::class.java)
            intent.putExtra("currentTitle", currentNote.title)
            intent.putExtra("currentDescription", currentNote.description)
            intent.putExtra("currentId", currentNote.id)

            // Activity Result Launcher
            activity.updateActivityResultLauncher.launch(intent)
        }
    }

    // Observer Data Consumption Method
    fun setNote (myNotes : List<com.learning.noteapproomdb.Model.Note>) {
        notes = myNotes
        notifyDataSetChanged() // Alert the adapter if there's a change in the database
    }

    fun getNote(position: Int) : Note {
        return notes[position]
    }
}