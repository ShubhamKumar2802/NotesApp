package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesapp.R
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import kotlinx.coroutines.*
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.utilities.DraftManager

class EditNoteFragment : Fragment() {

    private val args by navArgs<EditNoteFragmentArgs>()
    private lateinit var binding: NoteDetailScreenBinding
    private lateinit var notesViewModel: NotesViewModel
    private val TAG = "EditNoteFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteDetailScreenBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        // ViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        // Configure the Editor
        val editor: MarkDEditor = binding.editor
        val editorControlBar: EditorControlBar = binding.editorControlBar

        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            true,
            "Edit note here",
            H1
        )

        GlobalScope.launch(Dispatchers.Main) {
            val currentNoteDraft =
                notesViewModel.getDraftContentForEditor(args.selectedNote.note.ID)
            editor.loadDraft(currentNoteDraft)
        }

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)

        Log.d(TAG, "Editor loaded with draft: ${editor.draft}")
        editorControlBar.setEditor(editor)

        binding.ivBackButton.setOnClickListener {
            val noteId = args.selectedNote.note.ID
            val updatedNoteText = editor.markdownContent
            val updatedDraftModel = DraftManager().processDraftContent(editor).items

            CoroutineScope(Dispatchers.IO).launch {
                notesViewModel.updateNote(
                    id = noteId,
                    noteText = updatedNoteText,
                    bannerImageUrl = null
                )
                // in case the updated draftModel has less or equal number of items
                if (updatedDraftModel.size <= args.selectedNote.draftModel.size) {
                    for (index in args.selectedNote.draftModel.indices) {
                        val oldDraftModel = args.selectedNote.draftModel
                        val draftId = args.selectedNote.draftModel[index].draftID
                        if (index < updatedDraftModel.size) { // updating draftModel in database
                            notesViewModel.updateDraftModel(
                                id = draftId,
                                itemType = updatedDraftModel[index].itemType,
                                mode = updatedDraftModel[index].mode,
                                style = updatedDraftModel[index].style,
                                content = updatedDraftModel[index].content,
                                imageUrl = updatedDraftModel[index].downloadUrl,
                                caption = updatedDraftModel[index].caption
                            )
                        } else { // deleting the remaining draftModel items from database
                            Log.d(
                                TAG,
                                "onCreateView: DELETING remaining draftModel items from database"
                            )
                            notesViewModel.deleteDraftModel(
                                DraftModel(
                                    draftID = draftId,
                                    ID = noteId,
                                    itemType = oldDraftModel[index].itemType,
                                    mode = oldDraftModel[index].mode,
                                    style = oldDraftModel[index].style,
                                    content = oldDraftModel[index].content,
                                    imageDownloadURL = oldDraftModel[index].imageDownloadURL,
                                    imageCaption = oldDraftModel[index].imageCaption
                                )
                            )
                        }
                    }
                } else { // in case the updated draftModel has more number of items
                    for (index in updatedDraftModel.indices) {
                        if (index < args.selectedNote.draftModel.size) {
                            val draftId = args.selectedNote.draftModel[index].draftID
                            notesViewModel.updateDraftModel(
                                id = draftId,
                                itemType = updatedDraftModel[index].itemType,
                                mode = updatedDraftModel[index].mode,
                                style = updatedDraftModel[index].style,
                                content = updatedDraftModel[index].content,
                                imageUrl = updatedDraftModel[index].downloadUrl,
                                caption = updatedDraftModel[index].caption
                            )
                        } else { // adding the new items to database
                            Log.d(TAG, "onCreateView: Adding new items to database.")
                            notesViewModel.addDraftModel(
                                DraftModel(
                                    draftID = 0,
                                    ID = noteId,
                                    itemType = updatedDraftModel[index].itemType,
                                    mode = updatedDraftModel[index].mode,
                                    style = updatedDraftModel[index].style,
                                    content = updatedDraftModel[index].content,
                                    imageDownloadURL = updatedDraftModel[index].downloadUrl,
                                    imageCaption = updatedDraftModel[index].caption
                                )
                            )
                        }
                    }
                }
                delay(50)
            }
            Toast.makeText(requireContext(), "Note with ID: $noteId updated!", Toast.LENGTH_LONG)
                .show()
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_editNoteFragment_to_homeFragment)
        }

        binding.ivOptionsButton.setOnClickListener {
            val noteId = args.selectedNote.note.ID
            val noteWithDraftModel = notesViewModel.getNoteWithDraftModelUsingId(noteId)
            val action =
                EditNoteFragmentDirections.actionEditNoteFragmentToBottomSheetNoteDetailFragment(
                    noteWithDraftModel
                )
            findNavController().navigate(action)
//            val bottomSheet = BottomSheetDetailFragment()
//            bottomSheet.show(parentFragmentManager, "bottomSheet")
        }

//        if (args.selectedOption.isNotEmpty()) {
//            val currentNote = Note(
//                ID = args.selectedNote.note.ID,
//                noteText = editor.markdownContent,
//                noteImageBannerURL = null
//            )
//        }
        return binding.root
    }

//    private fun optionSelected(selectedOption: String, note: Note, draftModel: DraftModel) {
//        when (selectedOption) {
//            "Favorite" -> {
//
//            }
//            "Delete" -> {
//                notesViewModel.deleteNote(note)
//                notesViewModel.deleteDraftModel(draftModel)
//            }
//        }
//    }
}