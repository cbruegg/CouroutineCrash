package com.cbruegg.couroutinecrash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var job: Job? = null
    var isResumedCompat = false
    val pool = newSingleThreadContext("thread")

    fun awaitableDelay() = async(pool) {
        delay(2, TimeUnit.MILLISECONDS)
        Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            while (true) {
                Thread.sleep(100)
                triggerOnPause()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isResumedCompat = true
        job = launch(UI) {
            while (true) {
                awaitableDelay().await()
                if (!isResumedCompat) throw IllegalStateException()
            }
        }
    }

    override fun onPause() {
        job?.cancel(CancellationException("e"))
        super.onPause()
        isResumedCompat = false
    }

    fun triggerOnPause() {
        Intent(this, TransparentActivity::class.java).also { startActivity(it) }
    }
}
