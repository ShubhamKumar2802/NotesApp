package com.example.notesapp.ui.fragments

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.notesapp.R
import com.example.notesapp.database.BannerImage
import com.example.notesapp.database.entities.Note
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.NORMAL

private const val TAG = "EditNoteFragment"

class EditNoteFragment : Fragment(), View.OnClickListener {

    private val args by navArgs<EditNoteFragmentArgs>()
    private lateinit var binding: NoteDetailScreenBinding
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var navController: NavController
    private lateinit var editor: MarkDEditor
    private lateinit var editorControlBar: EditorControlBar
    private lateinit var selectedNote: Note
    private var updatedBannerImage: BannerImage? = null
    private val REQUEST_CODE = 100

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
        navController = Navigation.findNavController(view)

        // ViewModel
        notesViewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)
        selectedNote = notesViewModel.getNoteUsingId(args.selectedNote)

        // Configure the Editor
        editor = binding.editor
        editorControlBar = binding.editorControlBar
        editor.configureEditor(
            "",               //server url for image upload
            "",            //serverToken
            true,
            "Edit note here",
            NORMAL
        )
        editor.loadDraft(selectedNote.noteContents)
        editorControlBar.setEditorControlListener(notesViewModel.editorControlListener)
        Log.d(TAG, "Editor loaded with draft: ${editor.draft}")
        editorControlBar.setEditor(editor)

        // loading BannerImage for the selected note
        loadBannerImage(selectedNote.bannerImage?.imageUrl)
        if (selectedNote.bannerImage == null) {
            binding.tvImageCredits.visibility = View.GONE
        } else {
            binding.tvImageCredits.text =
                getString(R.string.banner_image_credits, selectedNote.bannerImage?.authorName)
        }

        // OnClickListeners
        binding.ivBackButton.setOnClickListener(this)
        binding.ivOptionsButton.setOnClickListener(this)
        binding.bannerImage.setOnClickListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: started")
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // getting the photos
            val photos: ArrayList<UnsplashPhoto>? =
                data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            with(photos?.first()) {
                with(this) {
                    updatedBannerImage = BannerImage(
                        imageUrl = this?.urls?.regular,
                        authorName = this?.user?.name,
                        authorProfileUrl = this?.user?.portfolio_url
                    )
                }
            }
            loadBannerImage(updatedBannerImage?.imageUrl)
            binding.tvImageCredits.visibility = View.VISIBLE
            binding.tvImageCredits.text =
                getString(R.string.banner_image_credits, updatedBannerImage?.authorName)
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
                val updatedNote = Note(
                    ID = selectedNote.ID,
                    noteContents = editor.draft,
//                    noteImageBannerURL = bannerImageUrl
                    bannerImage = if (updatedBannerImage != null) updatedBannerImage else selectedNote.bannerImage
                )
                if (updatedNote.noteContents.items.size == 1 && updatedNote.noteContents.items[0].content.isBlank()) {
                    notesViewModel.deleteNote(selectedNote)
                }
                if (!updatedNote.equals(selectedNote)) {
                    notesViewModel.updateNote(
                        updatedNote
                    )
                    Toast.makeText(
                        requireContext(),
                        "Note with ID: ${selectedNote.ID} updated!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                activity?.onBackPressed()
            }
            binding.ivOptionsButton.id -> {
                val action =
                    EditNoteFragmentDirections.actionEditNoteFragmentToBottomSheetNoteDetailFragment(
                        args.selectedNote
                    )
                findNavController().navigate(action)
            }
            binding.bannerImage.id -> {
                Log.d(TAG, "onClick: clicked on bannerImage")
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
}
