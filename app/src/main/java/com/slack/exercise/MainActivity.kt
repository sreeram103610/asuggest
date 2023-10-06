package com.slack.exercise

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import javax.inject.Inject

/**
 * Launcher activity. Kept light and simple to delegate view logic to fragment(s) it attaches.
 */
class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        if (scope.isActive)
            scope.cancel()
    }
}
