package com.slack.exercise

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

/**
 * Launcher activity. Kept light and simple to delegate view logic to fragment(s) it attaches.
 */
class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
