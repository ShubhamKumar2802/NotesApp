package com.example.notesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.BuildConfig
import com.example.notesapp.databinding.ActivityMainBinding
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "onCreate: started")

        //navController
//        setupActionBarWithNavController(findNavController(R.id.fragmentContainerHome))
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragmentContainerHome) as NavHostFragment
//        val navController = navHostFragment.navController

        UnsplashPhotoPicker.init(
            application = this.application, // application
            accessKey = BuildConfig.UNSPLASH_ACCESS_KEY,
            secretKey = BuildConfig.UNSPLASH_SECRET_KEY
            /* optional page size */
        )
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.fragmentContainerHome)
//        return navController.navigateUp()
//    }
}