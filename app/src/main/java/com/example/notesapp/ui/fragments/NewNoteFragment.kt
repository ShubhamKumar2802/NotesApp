package com.example.notesapp.ui.fragments

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
import com.example.notesapp.database.BannerImage
import com.example.notesapp.database.entities.Note
import com.example.notesapp.databinding.NoteDetailScreenBinding
import com.example.notesapp.viewModel.NotesViewModel
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import xute.markdeditor.EditorControlBar
import xute.markdeditor.MarkDEditor
import xute.markdeditor.Styles.TextComponentStyle.NORMAL
import xute.markdeditor.utilities.DraftManager

private const val TAG = "NewNoteFragment"

class NewNoteFragment : Fragment(), View.OnClickListener {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: NoteDetailScreenBinding
    private lateinit var navController: NavController
    private lateinit var editor: MarkDEditor
    private lateinit var editorControlBar: EditorControlBar
    private var bannerImage: BannerImage? = null
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
            NORMAL                       //default heading type
        )
        editorControlBar.setEditor(editor)
        binding.tvImageCredits.visibility = View.GONE
        binding.ivBackButton.setOnClickListener(this)
        binding.bannerImage.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // getting the photos
            val photos: ArrayList<UnsplashPhoto>? =
                data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            with(photos?.first()) {
                bannerImage = BannerImage(
                    imageUrl = this?.urls?.regular,
                    authorName = this?.user?.name,
                    authorProfileUrl = this?.user?.portfolio_url
                )
                binding.tvImageCredits.visibility = View.VISIBLE
                binding.tvImageCredits.text =
                    getString(R.string.banner_image_credits, bannerImage?.authorName)
                loadBannerImage(bannerImage?.imageUrl)
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
                val draftDataItemsList = DraftManager().processDraftContent(editor).items
                if (!notesViewModel.emptyNote(draftDataItemsList)) {
                    notesViewModel.addNewNote(
                        Note(
                            ID = 0,
                            noteContents = editor.draft,
                            bannerImage = bannerImage
                        )
                    )
                }
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
}