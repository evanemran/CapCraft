package com.evanemran.capcraft.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.evanemran.capcraft.R
import com.evanemran.capcraft.data.repository.VideoRepositoryImpl
import com.evanemran.capcraft.databinding.ActivityMainBinding
import com.evanemran.capcraft.utils.PermissionUtils
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        PermissionUtils.requestPermission(
            this,
            PermissionUtils.PERMISSION_READ_MEDIA_VIDEO,
            "This app needs access to videos."
        ) { // Callback when permission is granted

            lifecycleScope.launch {
                val list = VideoRepositoryImpl(this@MainActivity)
                    .getVideoFiles()
                binding.textviewHome.text = list.size.toString()

            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.onRequestPermissionsResult(requestCode, grantResults) {
            // Handle permission granting here
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
        }
    }
}