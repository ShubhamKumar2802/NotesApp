package com.example.notesapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.NotesRepository
import com.example.notesapp.database.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xute.markdeditor.EditorControlBar
import xute.markdeditor.datatype.DraftDataItemModel

private const val TAG = "NotesViewModel"

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    var notesList: LiveData<List<Note>>
    private val notesRepository: NotesRepository
    var editorControlListener: EditorControlBar.EditorControlListener = EditorControlBarListener()

    init {
        Log.d(TAG, "initializing viewModel...")
        val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
        notesRepository = NotesRepository(notesDao)
        notesList = notesRepository.allNotesList
    }

    fun addNewNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.addNote(note)
            Log.d(TAG, "Note Added: ID:${note.ID}; Content:${note.noteContents}")
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
        }
    }

    fun getNoteUsingId(noteId: Long): Note {
        return runBlocking {
            notesRepository.getNoteUsingId(noteId)
        }
    }

    fun emptyNote(draftModel: ArrayList<DraftDataItemModel>): Boolean {
        Log.d(
            TAG, """
            emptyNote: started
            draftModel received with ${draftModel.size} items
        """.trimIndent()
        )
        var isEmpty = true
        if (draftModel.size == 0) {
            return true
        }
        draftModel.forEach {
            if (it.itemType != 2) {
                if (it.content.isNotEmpty()) {
                    isEmpty = false
                }
            }
        }
        return isEmpty
    }

    inner class EditorControlBarListener : EditorControlBar.EditorControlListener {
        override fun onInsertImageClicked() {
            TODO("Not yet implemented")
        }

        override fun onInserLinkClicked() {
            TODO("Not yet implemented")
        }
    }
}