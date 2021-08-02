package com.example.notesapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.NotesRepository
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.entityRelations.NoteWithDraftModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import xute.markdeditor.EditorControlBar
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.components.TextComponentItem.MODE_PLAIN
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.DraftModel

private const val TAG = "NotesViewModel"

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    var notesList: LiveData<List<NoteWithDraftModel>>
    private val notesRepository: NotesRepository
    var editorControlListener: EditorControlBar.EditorControlListener = EditorControlBarListener()

    init {
        Log.d(TAG, "initializing viewModel...")
        val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
        notesRepository = NotesRepository(notesDao)
        notesList = notesRepository.allNotesList
//        Log.d(TAG, """
//            notesList:
//                ${notesList.value}
//        """.trimIndent())
    }

//    val allNotesList = notesRepository.allNotesList

    fun addNewNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.addNote(note)
            Log.d(TAG, "Note Added: ID:${note.ID}; Content:${note.noteText}")
        }
    }

    fun addDraftModel(draftModel: com.example.notesapp.database.entities.DraftModel) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.addDraftModel(draftModel)
        }
        Log.d(
            TAG, """
            addDraftModel: new draftModel added with
            ID = ${draftModel.ID}
            Content = ${draftModel.content}
        """.trimIndent()
        )
    }

//    fun deleteNote(noteId: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesRepository.deleteNote(noteId)
//        }
//    }
//
//    fun deleteDraftModel(noteId: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            notesRepository.deleteDraftModel(noteId)
//        }
//    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
        }
    }

    fun deleteDraftModel(draftModel: com.example.notesapp.database.entities.DraftModel) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteDraftModel(draftModel)
        }
    }

    fun getAllNotes(): LiveData<List<NoteWithDraftModel>> {
        Log.d(
            TAG, """
            
            ${notesRepository.allNotesList.value}
            
            
        """.trimIndent()
        )
        return notesRepository.allNotesList
    }

    fun getNoteIdUsingNoteText(noteText: String): Long {
        return runBlocking {
            notesRepository.getNoteIdUsingNoteText(noteText)
        }
    }

    fun getNoteUsingId(id: Long): Note {
        return runBlocking {
            notesRepository.getNoteUsingId(id)
        }
    }

    fun getNoteWithDraftModelUsingId(id: Long): NoteWithDraftModel {
        return runBlocking {
            notesRepository.getNoteWithDraftModelUsingId(id).first()
        }
    }

    /*
    -- noteDraftModel: size = 1
    -- noteDraftModel.draftModel: size = number of draftModel items retrieved from database
     */
    fun getDraftContentForEditor(id: Long): DraftModel {
        return runBlocking {
            val draftContent = ArrayList<DraftDataItemModel>()
            val noteAndDraftModelList = notesRepository.getNoteWithDraftModelUsingId(id)
            with(noteAndDraftModelList.first()) {
                this.draftModel.forEach { draftModelItem ->
                    val newDraftDataItem = DraftDataItemModel().apply {
                        this.itemType = draftModelItem.itemType
                        this.mode = draftModelItem.mode
                        this.style = draftModelItem.style
                        this.content = draftModelItem.content
                        this.downloadUrl = draftModelItem.imageDownloadURL
                        this.caption = draftModelItem.imageCaption
                    }
                    draftContent.add(newDraftDataItem)
                    Log.d(
                        TAG, """
                        getDraftContentForEditor: 
                        new draftModelItem object added to draftContents list with contents: 
                        content = ${newDraftDataItem.content}
                        draftContents list size = ${draftContent.size}
                    """.trimIndent()
                    )
                }
            }
            Log.d(
                TAG,
                "getDraftContentForEditor: returning DraftModel with size = ${draftContent.size}"
            )
            DraftModel(draftContent)
        }
    }

    fun getEditorDraftContent(): DraftModel {
        val contentType = ArrayList<DraftDataItemModel>()
        val heading = DraftDataItemModel().apply {
            this.itemType = DraftModel.ITEM_TYPE_TEXT
            this.content = "New Note Testing"
            this.mode = MODE_PLAIN
            this.style = H1
        }
        contentType.add(heading)
        Log.d(TAG, "getEditorDraftContent: ${DraftModel(contentType)}")
        return DraftModel(contentType)
    }

    fun updateNote(id: Long, noteText: String, bannerImageUrl: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.updateNote(id, noteText, bannerImageUrl)
        }
    }

    fun updateDraftModel(
        id: Long,
        itemType: Int,
        mode: Int,
        style: Int,
        content: String,
        imageUrl: String?,
        caption: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.updateDraftModel(
                id,
                itemType,
                mode,
                style,
                content,
                imageUrl,
                caption
            )
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
            if (it.content.isNotEmpty()) {
                isEmpty = false
            }
        }
        return isEmpty
    }

    fun printDraftModelContents(draftModelList: ArrayList<DraftDataItemModel>) {
        draftModelList.forEach {
            println(
                """
                content: ${it.content}
                ________________________________________
            """.trimIndent()
            )
        }
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