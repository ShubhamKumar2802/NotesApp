package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.notesapp.R
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.utilities.DraftManager

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: NoteDetailScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteDetailScreenBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        // ViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        binding.ivOptionsButton.alpha = 0F

        // Configure the Editor
        val editor: MarkDEditor = binding.editor
        val editorControlBar: EditorControlBar = binding.editorControlBar

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            false,              //isDraft: set true when you are loading draft(fresh editor window)
            "Type here...",  //default hint of input box
            H1                       //default heading type
        )

        editorControlBar.setEditor(editor)

        binding.ivBackButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val noteText = editor.markdownContent
                val draftDataItemsList = DraftManager().processDraftContent(editor).items
                if (draftDataItemsList.isEmpty()) {
                    Log.d(TAG, "!!!!!!!!!!!!!!!!    List empty    !!!!!!!!!!!!!!!!")
                } else {
                    printDraftModelContents(draftDataItemsList)
                }
                saveNote(
                    noteText = noteText,
                    bannerURL = null,
                    draftDataItemsList = draftDataItemsList
                )
            }
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_newNoteFragment_to_homeFragment)
        }
        return binding.root
    }

    private suspend fun saveNote(
        noteText: String,
        bannerURL: String?,
        draftDataItemsList: ArrayList<DraftDataItemModel>
    ) {
        if (noteText != "\\n# \\n") {
            val newNote = Note(
                ID = 0,
                noteText = noteText,
                noteImageBannerURL = null
            )
            notesViewModel.addNewNote(newNote)

            // get the ID for the current note that was just saved in notes_table
            var newNoteId: Long? = null
            try {
                newNoteId = notesViewModel.getNoteIdUsingNoteText(noteText)
                Log.d(TAG, "saveNote: newNoteID received from DB with ID = $newNoteId")
            } catch (exception: Exception) {
                Log.d(TAG, "saveNote: $exception")
            }

            printDraftModelContents(draftDataItemsList)
            draftDataItemsList.forEach {
                val newDraftModelItem = DraftModel(
                    draftID = 0,
                    ID = newNoteId,
                    itemType = it.itemType,
                    mode = it.mode,
                    style = it.style,
                    content = it.content,
                    imageDownloadURL = it.downloadUrl,
                    imageCaption = it.caption
                )
                notesViewModel.addDraftModel(newDraftModelItem)
                delay(50)
            }
        }
    }

    private fun printDraftModelContents(draftModelList: ArrayList<DraftDataItemModel>) {
        draftModelList.forEach {
            println(
                """
                content: ${it.content}
                ________________________________________
            """.trimIndent()
            )
        }
    }
}