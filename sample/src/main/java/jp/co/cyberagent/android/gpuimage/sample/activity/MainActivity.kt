package jp.co.cyberagent.android.gpuimage.sample.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.co.cyberagent.android.gpuimage.sample.R

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button_gallery).setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }
        findViewById<View>(R.id.button_camera).setOnClickListener {
            if (!hasRequiredCameraPermissions()) {
                // Use a valid request code (less than 65536)
                ActivityCompat.requestPermissions(
                    this,
                    getCameraPermissions(),
                    REQUEST_CAMERA // This should be a small integer like 100
                )
            } else {
                startActivity(Intent(this, CameraActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                startActivity(Intent(this, CameraActivity::class.java))
            } else {
                Toast.makeText(this, "Camera permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasRequiredCameraPermissions(): Boolean {
        val requiredPermissions = getCameraPermissions()
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun getCameraPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10-12
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            // Older Android versions
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    companion object {
        // Make sure this is a small integer (less than 65536)
        private const val REQUEST_CAMERA = 100
    }
}
