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
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView: started")

        binding.buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newNoteFragment)
        }

        binding.tvHomePageGreeting.setText(R.string.home_fragment_welcome_message)

        // Horizontal RecyclerView
        val horizontalRecyclerViewAdapter = NotesHorizontalAdapter()
        val rvHorizontal = binding.rvNotesHorizontal
        rvHorizontal.adapter = horizontalRecyclerViewAdapter
        rvHorizontal.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // NotesViewModel
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            horizontalRecyclerViewAdapter.setData(it)
        })

        // setting custom background color
//        val homeFragmentBackground: GradientDrawable? = binding.ivBackgroundGradient.background as GradientDrawable?
//        homeFragmentBackground.setColor(Color.GREEN)
        return binding.root
    }

//    fun Drawable.overrideColor(@ColorInt colorInt: Int) {
//        when (this) {
//            is GradientDrawable -> setColor(colorInt)
//            is ShapeDrawable -> paint.color = colorInt
//            is ColorDrawable -> color = colorInt
//        }
//    }
}