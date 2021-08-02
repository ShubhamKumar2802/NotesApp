package com.example.notesapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.entities.DraftModel
import com.example.notesapp.database.entities.Note
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.H1
import xute.markdeditor.datatype.DraftDataItemModel
import xute.markdeditor.utilities.DraftManager

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment(), View.OnClickListener {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: NoteDetailScreenBinding
    private lateinit var navController: NavController
    private lateinit var editor: MarkDEditor
    private lateinit var editorControlBar: EditorControlBar
    private val REQUEST_CODE = 100
    private var bannerImageUrl: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NoteDetailScreenBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: started")
        // ViewModel
        notesViewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        // Setting up navController
        navController = Navigation.findNavController(view)

        binding.ivOptionsButton.alpha = 0F
        // Configure the Editor
        editor = binding.editor
        editorControlBar = binding.editorControlBar

        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            false,              //isDraft: set true when you are loading draft(fresh editor window)
            "Type here...",  //default hint of input box
            H1                       //default heading type
        )
        editorControlBar.setEditor(editor)
        binding.ivBackButton.setOnClickListener(this)
        binding.bannerImage.setOnClickListener(this)
    }

    private fun saveNote(
        noteText: String,
        bannerURL: String?,
        draftDataItemsList: ArrayList<DraftDataItemModel>
    ) {
        Log.d(TAG, "saveNote: started")
        if (!notesViewModel.emptyNote(draftDataItemsList)) {
            val newNote = Note(
                ID = 0,
                noteText = noteText,
                noteImageBannerURL = bannerURL
            )
            notesViewModel.addNewNote(newNote)

            notesViewModel.printDraftModelContents(draftDataItemsList)
            draftDataItemsList.forEach {
                val newDraftModelItem = DraftModel(
                    draftID = 0,
                    ID = notesViewModel.getNoteIdUsingNoteText(noteText),
                    itemType = it.itemType,
                    mode = it.mode,
                    style = it.style,
                    content = it.content,
                    imageDownloadURL = it.downloadUrl,
                    imageCaption = it.caption
                )
                notesViewModel.addDraftModel(newDraftModelItem)
            }
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
                    loadBannerImage(bannerImageUrl)
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

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ivBackButton.id -> {
                val noteText = editor.markdownContent
//                val dm = editor.draft
//                val jsonString = Gson().toJson(dm)
//                printJsonString(jsonString)
                val draftDataItemsList = DraftManager().processDraftContent(editor).items
                if (draftDataItemsList.isEmpty()) {
                    Log.d(TAG, "!!!!!!!!!!!!!!!!    List empty    !!!!!!!!!!!!!!!!")
                } else {
                    notesViewModel.printDraftModelContents(draftDataItemsList)
                }
                saveNote(
                    noteText = noteText,
                    bannerURL = bannerImageUrl,
                    draftDataItemsList = draftDataItemsList
                )
                activity?.onBackPressed()
            }
            binding.bannerImage.id -> {
                startActivityForResult(
                    UnsplashPickerActivity.getStartingIntent(
                        requireContext(),
                        isMultipleSelection = false
                    ),
                    REQUEST_CODE
                )
            }
        }
    }

    private fun printJsonString(json: String) {
        Log.d(TAG, json)
    }
}