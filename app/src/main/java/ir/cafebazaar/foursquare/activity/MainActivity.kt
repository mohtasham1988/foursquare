package ir.cafebazaar.foursquare.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import ir.cafebazaar.foursquare.R

class MainActivity : AppCompatActivity() {

    private var mViewContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewContainer = findViewById(R.id.container)
    }
}
