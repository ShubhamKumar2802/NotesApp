package com.example.notesapp.fragments

import android.app.Activity
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import kotlinx.coroutines.*
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.utilities.DraftManager

class EditNoteFragment : Fragment() {
    //TODO "banner image does not change when a new image is selected to replace the existing one."
    private val args by navArgs<EditNoteFragmentArgs>()
    private lateinit var binding: NoteDetailScreenBinding
    private lateinit var notesViewModel: NotesViewModel
    private val TAG = "EditNoteFragment"
    private val REQUEST_CODE = 100
    private var bannerImageUrl: String? = ""

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

        bannerImageUrl = args.selectedNote.note.noteImageBannerURL
        loadBannerImage(args.selectedNote.note.noteImageBannerURL)

        binding.ivBackButton.setOnClickListener {
            val noteId = args.selectedNote.note.ID
            val updatedNoteText = editor.markdownContent
            val updatedDraftModel = DraftManager().processDraftContent(editor).items

            CoroutineScope(Dispatchers.IO).launch {
                updateNote(
                    updatedNoteText = updatedNoteText,
                    updatedDraftModel = updatedDraftModel
                )
                delay(100)
//                notesViewModel.updateNote(
//                    id = noteId,
//                    noteText = updatedNoteText,
//                    bannerImageUrl = if (bannerImageUrl.isNullOrEmpty()) null else bannerImageUrl
//                )
//                // in case the updated draftModel has less or equal number of items
//                if (updatedDraftModel.size <= args.selectedNote.draftModel.size) {
//                    for (index in args.selectedNote.draftModel.indices) {
//                        val oldDraftModel = args.selectedNote.draftModel
//                        val draftId = args.selectedNote.draftModel[index].draftID
//                        if (index < updatedDraftModel.size) { // updating draftModel in database
//                            notesViewModel.updateDraftModel(
//                                id = draftId,
//                                itemType = updatedDraftModel[index].itemType,
//                                mode = updatedDraftModel[index].mode,
//                                style = updatedDraftModel[index].style,
//                                content = updatedDraftModel[index].content,
//                                imageUrl = updatedDraftModel[index].downloadUrl,
//                                caption = updatedDraftModel[index].caption
//                            )
//                        } else { // deleting the remaining draftModel items from database
//                            Log.d(
//                                TAG,
//                                "onCreateView: DELETING remaining draftModel items from database"
//                            )
//                            notesViewModel.deleteDraftModel(
//                                DraftModel(
//                                    draftID = draftId,
//                                    ID = noteId,
//                                    itemType = oldDraftModel[index].itemType,
//                                    mode = oldDraftModel[index].mode,
//                                    style = oldDraftModel[index].style,
//                                    content = oldDraftModel[index].content,
//                                    imageDownloadURL = oldDraftModel[index].imageDownloadURL,
//                                    imageCaption = oldDraftModel[index].imageCaption
//                                )
//                            )
//                        }
//                    }
//                } else { // in case the updated draftModel has more number of items
//                    for (index in updatedDraftModel.indices) {
//                        if (index < args.selectedNote.draftModel.size) {
//                            val draftId = args.selectedNote.draftModel[index].draftID
//                            notesViewModel.updateDraftModel(
//                                id = draftId,
//                                itemType = updatedDraftModel[index].itemType,
//                                mode = updatedDraftModel[index].mode,
//                                style = updatedDraftModel[index].style,
//                                content = updatedDraftModel[index].content,
//                                imageUrl = updatedDraftModel[index].downloadUrl,
//                                caption = updatedDraftModel[index].caption
//                            )
//                        } else { // adding the new items to database
//                            Log.d(TAG, "onCreateView: Adding new items to database.")
//                            notesViewModel.addDraftModel(
//                                DraftModel(
//                                    draftID = 0,
//                                    ID = noteId,
//                                    itemType = updatedDraftModel[index].itemType,
//                                    mode = updatedDraftModel[index].mode,
//                                    style = updatedDraftModel[index].style,
//                                    content = updatedDraftModel[index].content,
//                                    imageDownloadURL = updatedDraftModel[index].downloadUrl,
//                                    imageCaption = updatedDraftModel[index].caption
//                                )
//                            )
//                        }
//                    }
//                }
//                delay(50)
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
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bannerImage.setOnClickListener {
            startActivityForResult(
                UnsplashPickerActivity.getStartingIntent(
                    requireContext(),
                    isMultipleSelection = false
                ),
                REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // getting the photos
            val photos: ArrayList<UnsplashPhoto>? =
                data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            with(photos?.first()) {
                this?.urls?.regular?.let {
                    bannerImageUrl = it
                    Log.d(
                        TAG, """
                            onActivityResult: 
                            noteId = ${args.selectedNote.note.ID}
                            bannerImageUrl = $it
                        """.trimIndent()
                    )
                    loadBannerImage(bannerImageUrl)
                }
            }
        }
    }

    private suspend fun updateNote(
        updatedNoteText: String,
        updatedDraftModel: ArrayList<DraftDataItemModel>
    ) {
        val noteId = args.selectedNote.note.ID
        // Updating notes_table entity first
        notesViewModel.updateNote(
            id = noteId,
            noteText = updatedNoteText,
            bannerImageUrl = if (bannerImageUrl.isNullOrEmpty()) null else bannerImageUrl
        )

        // Then updating DraftModel entity
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
    }

    private fun loadBannerImage(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) {
            Log.d(TAG, "loadBannerImage: empty URL")
        } else {
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.bannerImage)
        }
    }
}
