package ir.cafebazaar.foursquare.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.fragment.view.MainFragment

class MainActivity : AppCompatActivity() {

    private var tgs = "MainActivity"
    private var code = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showFragment()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                code
            )
        }
    }

    private fun showFragment() {
        findViewById<View>(R.id.container).apply {
            supportFragmentManager.beginTransaction().replace(
                this.id,
                MainFragment()
            )
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0)
            super.onBackPressed()
        else
            supportFragmentManager.popBackStack()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i(tgs, "onRequestPermissionResult")
        if (requestCode == code) {
            if (grantResults.isEmpty()) {
                Log.i(tgs, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFragment()
            } else {
                Toast.makeText(this, "App Need Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
