package com.example.walkie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walkie.ui.main.WalkieFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, WalkieFragment.newInstance())
                    .commitNow()
        }
    }
}