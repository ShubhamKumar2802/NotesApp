package com.example.notesapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.NotesRepository
import com.example.notesapp.database.Note
import com.example.notesapp.database.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.components.TextComponentItem.MODE_PLAIN
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.DraftModel

private const val TAG = "NotesViewModel"

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val notesList: LiveData<List<Note>>
    private val notesRepository: NotesRepository


    init {
        val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
        notesRepository = NotesRepository(notesDao)
        notesList = notesRepository.getNotesList
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insertNote(note)
            Log.d(TAG, "Note Added: ID:${note.ID}; Content:${note.noteText}")
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
            Log.d(TAG, "Note deleted: $note")
        }
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return notesRepository.getNotesList
    }

    fun getEditorDraftContent(): DraftModel {
        val contentType = ArrayList<DraftDataItemModel>()
        val heading = DraftDataItemModel()
        heading.itemType = DraftModel.ITEM_TYPE_TEXT
        heading.content = "New Note Testing"
        heading.mode = MODE_PLAIN
        heading.style = H1
        return DraftModel(contentType)
    }

    @Override
    fun onInsertImageClicked() {
        //TODO "Add options to insert an image from local storage and from Unsplash"
    }
}