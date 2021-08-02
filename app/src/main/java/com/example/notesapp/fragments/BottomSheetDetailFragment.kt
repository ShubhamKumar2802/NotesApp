package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentNoteDetailBottomSheetBinding
import com.example.notesapp.viewModel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BottomSheetDetailFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private val TAG = "BottomSheetFragment"
    private val args by navArgs<BottomSheetDetailFragmentArgs>()
    private lateinit var binding: FragmentNoteDetailBottomSheetBinding
    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBottomSheetBinding.inflate(inflater, container, false)
//        setStyle(STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
        Log.d(TAG, "onCreateView: started")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: started")
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        binding.tvOptionDelete.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.tvOptionDelete.id -> {
                MaterialAlertDialogBuilder(view.context)
                    .setTitle("Delete Note?")
                    .setMessage("This action cannot be undone!")
                    .setIcon(R.drawable.ic_delete_note)
                    .setNegativeButton("Cancel") { _, _ ->
                        findNavController().popBackStack(R.id.editNoteFragment, false)
                    }
                    .setPositiveButton("Delete") { _, _ ->
                        val note = args.bottomSheetArg.note
                        val draftModel = args.bottomSheetArg.draftModel
                        notesViewModel.deleteNote(note)
                        draftModel.forEach {
                            notesViewModel.deleteDraftModel(it)
                        }
//                        val action =
//                            BottomSheetDetailFragmentDirections.actionBottomSheetDetailFragmentToHomeFragment()
                        //TODO "Show a SnackBar confirming that note was deleted"
//                        Snackbar.make(view, "Note Deleted!", Snackbar.LENGTH_SHORT).show()
//                        view.post {
//                            findNavController().popBackStack(R.id.homeFragment, false)
//                        }
                        findNavController().popBackStack(R.id.editNoteFragment, true)
                    }
                    .show()
            }
        }
    }
}