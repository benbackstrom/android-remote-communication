package com.example.talkie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talkie.ui.main.TalkieFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, TalkieFragment.newInstance())
                    .commitNow()
        }
    }
}