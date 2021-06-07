package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.fragments.horizontalRecyclerView.NotesHorizontalAdapter
import com.example.notesapp.viewModel.NotesViewModel

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val homeFragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        homeFragmentBinding.buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }

        // Horizontal RecyclerView
        val horizontalRecyclerViewAdapter = NotesHorizontalAdapter()
        val rvHorizontal = homeFragmentBinding.rvNotesHorizontal
        rvHorizontal.adapter = horizontalRecyclerViewAdapter
        rvHorizontal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // NotesViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            horizontalRecyclerViewAdapter.setData(it)
        })

        return homeFragmentBinding.root
    }

}