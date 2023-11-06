package com.learning.noteapproomdb.ViewModel

import androidx.lifecycle.*
import com.learning.noteapproomdb.Model.Note
import com.learning.noteapproomdb.Repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    val myAllNotes : LiveData<List<Note>> = repository.allNotes.asLiveData() // Flow -> LiveData

    // Suspend function should be called only inside of coroutine scope
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        // Choose which threads of process will be performed on. Dispatcher is essentially a thread pool.
        // Dispatcher.IO is used for Database Input Output operation.
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}

class NoteViewModelFactory(private var repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown View Model")
        }
    }
}