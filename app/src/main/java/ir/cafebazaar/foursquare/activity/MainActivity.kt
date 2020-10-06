package ir.cafebazaar.foursquare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.fragment.view.MainFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
