package com.example.notesapp.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.viewModel.NotesViewModel


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        binding.buttonAddNote.setOnClickListener {
            Log.d(
                TAG,
                "onCreateView: current Fragment is ${findNavController().currentDestination?.toString()}"
            )
            val action = HomeFragmentDirections.actionHomeFragmentToNewNoteFragment()
            binding.root.findNavController().navigate(action)
        }

        binding.tvHomePageGreeting.setText(R.string.home_fragment_welcome_message)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollViewNotesRecyclerView.setOnScrollChangeListener { view, scrollX, scrollY, previousX, previousY ->
                if (scrollY > previousY + 1 && binding.buttonAddNote.isExtended) {
                    binding.buttonAddNote.shrink()
                }
                if (scrollY < previousY - 1 && !binding.buttonAddNote.isExtended) {
                    binding.buttonAddNote.extend()
                }

                // if the nestedScrollView is at the first item of the list then the
                // extended floating action should be in extended state
                if (scrollY == 0) {
                    binding.buttonAddNote.extend()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: started")
        // Horizontal RecyclerView
        val recyclerViewAdapter = NotesHorizontalAdapter()
        val rvHorizontal = binding.rvNotesHorizontal
        rvHorizontal.adapter = recyclerViewAdapter
        rvHorizontal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // NotesViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.setData(it)
        })
    }
}