package com.example.notesapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.NotesRepository
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.database.entities.Note
import com.example.notesapp.database.entities.NoteDraftDataItem
import com.example.notesapp.database.entities.NoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteAndNoteDraftModel
import com.example.notesapp.database.entities.entityRelations.NoteDraftModelWithNoteDraftDataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xute.markdeditor.EditorControlBar
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.components.TextComponentItem.MODE_PLAIN
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.models.DraftModel

private const val TAG = "NotesViewModel"

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val notesList: LiveData<List<NoteAndNoteDraftModel>>
    private val notesRepository: NotesRepository
    var editorControlListener: EditorControlBar.EditorControlListener = EditorControlBarListener()

    init {
        val notesDao = NoteDatabase.getNoteDatabase(application).getNoteDao()
        notesRepository = NotesRepository(notesDao)
        notesList = notesRepository.getNotesList
    }

    fun insertNote(note: Note, draftModel: NoteDraftModel) {
        viewModelScope.launch(Dispatchers.IO) {
            with(notesRepository) {
                insertNote(note)
                insertNoteDraftModel(draftModel)
            }
            Log.d(TAG, "Note Added: ID:${note.ID}; Content:${note.noteText}")
        }
    }

    fun insertDraftDataItem(draftDataItem: NoteDraftDataItem) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.insertNoteDraftDataItem(draftDataItem)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.deleteNote(note)
            Log.d(TAG, "Note deleted: $note")
        }
    }

    fun getAllNotes(): LiveData<List<NoteAndNoteDraftModel>> {
        return notesRepository.getNotesList
    }

//    fun getAllDrafts(): LiveData<List<NoteDraftModelWithNoteDraftDataItem>> {
//        return notesRepository.getDraftModelList
//    }

    // TODO ("Method to get DraftModel from Database")
//    fun getDraftFromDatabase(draftID: Long): DraftModel {
//        val draftContent = ArrayList<DraftDataItemModel>()
//
//        val draftDataItemList: List<NoteDraftModelWithNoteDraftDataItem> =
//            notesRepository.getNoteDraftModelAndNoteDraftDataItem(draftID)
//
//        // traverse through the draftItemList and create a DraftDataItemModel and set its properties
//        draftDataItemList.forEach {
////            if (it.noteDraftModel.draftID == draftID) {
//            it.draftDataItem.forEach { dataItem ->
//                val newDraftDataItem = DraftDataItemModel().apply {
//                    this.itemType = dataItem.itemType
//                    this.mode = dataItem.mode
//                    this.style = dataItem.style
//                    this.content = dataItem.content
//                    this.downloadUrl = dataItem.imageDownloadURL
//                    this.caption = dataItem.imageCaption
//                }
//                draftContent.add(newDraftDataItem)
//            }
////                val newDraftDataItem = DraftDataItemModel().apply {
////                    this.itemType = it.draftDataItem.first().itemType
////                    this.mode = it.draftDataItem.first().mode
////                    this.style = it.draftDataItem.first().style
////                    this.content = it.draftDataItem.first().content
////                    this.downloadUrl = it.draftDataItem.first().imageDownloadURL
////                    this.caption = it.draftDataItem.first().imageCaption
////                }
////                draftContent.add(newDraftDataItem)
////            }
//        }
//        Log.d(TAG, "getDraftFromDatabase: ${DraftModel(draftContent)}")
//        return DraftModel(draftContent)
//    }

    fun getDraftFromDatabase(draftID: Long?): DraftModel {
        val draftContent = ArrayList<DraftDataItemModel>()
        viewModelScope.launch(Dispatchers.IO) {
            val draftDataItemList: List<NoteDraftModelWithNoteDraftDataItem> =
                notesRepository.getNoteDraftModelAndNoteDraftDataItem(draftID)

            // traverse through the draftItemList and create a DraftDataItemModel and set its properties
            draftDataItemList.forEach {
                val newDraftItem = DraftDataItemModel().apply {
                    this.itemType = it.draftDataItem.first().itemType
                    this.mode = it.draftDataItem.first().mode
                    this.style = it.draftDataItem.first().style
                    this.content = it.draftDataItem.first().content
                    this.downloadUrl = it.draftDataItem.first().imageDownloadURL
                    this.caption = it.draftDataItem.first().imageCaption
                }
                draftContent.add(newDraftItem)
            }

//            DraftModel(draftContent)
        }
        Log.d(
            TAG, """
                getDraftFromDatabase: 
                draft ID: ${DraftModel(draftContent).draftId}
                draft contents: ${DraftModel(draftContent).items}
                draft size: ${DraftModel(draftContent).items.size}
                """.trimIndent()
        )
        return DraftModel(draftContent)
    }

    fun getEditorDraftContent(): DraftModel {
        val contentType = ArrayList<DraftDataItemModel>()
        val heading = DraftDataItemModel()
        heading.itemType = DraftModel.ITEM_TYPE_TEXT
        heading.content = "New Note Testing"
        heading.mode = MODE_PLAIN
        heading.style = H1
        Log.d(TAG, "getEditorDraftContent: ${DraftModel(contentType)}")
        return DraftModel(contentType)
    }
//
//    /** TODO "Complete this method"
//     * -> Take the note and return an ArrayList<DraftDataItemModel> to render the editor
//     * -> Implementation idea:
//     *          1. Split the note in various DraftDataItemModel of suitable TYPE
//     *          2. Then prepare an ArrayList
//    */
//    fun renderNotesList(noteContent: Note): ArrayList<DraftDataItemModel> {
//        val notesList = noteContent.noteText.split("\n")
//        Log.d(
//            TAG, """
//            renderNotesList:
//            noteContent after splitting up with "\n" for Note with ID ${noteContent.ID}:
//            $notesList
//            #######################################
//
//
//        """.trimIndent()
//        )
//        val draftDataList = ArrayList<DraftDataItemModel>()
//        for (note in notesList) {
//            val newDraftItem = DraftDataItemModel()
//            when(newDraftItem.itemType) {
//                MODE_PLAIN -> {}
//                MODE_OL -> {}
//                MODE_UL -> {}
//            }
//            with(newDraftItem) {
//                content = note
//            }
//            draftDataList.add(newDraftItem)
//            Log.d(
//                TAG, """
//                renderNotesList():
//                $newDraftItem
//                """.trimIndent()
//            )
//        }
//        return draftDataList
//    }

    inner class EditorControlBarListener : EditorControlBar.EditorControlListener {
        override fun onInsertImageClicked() {
            TODO("Not yet implemented")
        }

        override fun onInserLinkClicked() {
            TODO("Not yet implemented")
        }
    }
}