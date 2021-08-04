package com.example.notesapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.ui.RecyclerViewAdapter
import com.example.notesapp.viewModel.NotesViewModel

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentHomeBinding
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: started")
        navController = Navigation.findNavController(view)
        notesViewModel = ViewModelProvider(requireActivity()).get(NotesViewModel::class.java)

        //RecyclerView
        val recyclerViewAdapter = RecyclerViewAdapter()
        val notesRecyclerView = binding.notesRv
        notesRecyclerView.adapter = recyclerViewAdapter
        notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        notesViewModel.notesList.observe(viewLifecycleOwner, Observer { allNotesList ->
            Log.d(TAG, "onCreateView: Observing list...")
            recyclerViewAdapter.setData(allNotesList)
        })
        binding.buttonAddNote.setOnClickListener(this)
        binding.tvHomePageGreeting.setText(R.string.home_fragment_welcome_message)

        // collapsable FAB
        val offset = 5
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollViewNotesRecyclerView.setOnScrollChangeListener { view, scrollX, scrollY, previousX, previousY ->
                if (scrollY > previousY + offset && binding.buttonAddNote.isExtended) {
                    binding.buttonAddNote.shrink()
                }
                if (scrollY < previousY - offset && !binding.buttonAddNote.isExtended) {
                    binding.buttonAddNote.extend()
                }
                if (scrollY == 0) {
                    binding.buttonAddNote.extend()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.buttonAddNote.id -> {
                navController.navigate(R.id.action_homeFragment_to_newNoteFragment)
            }
        }
    }
}