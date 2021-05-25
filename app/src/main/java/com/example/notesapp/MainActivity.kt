package com.example.notesapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.viewModel.NotesViewModel

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: started")

        noteViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        noteViewModel.notesList.observe(this, Observer {  })
    }
}