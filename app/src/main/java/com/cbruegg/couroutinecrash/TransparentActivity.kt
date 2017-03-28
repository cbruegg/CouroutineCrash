package com.cbruegg.couroutinecrash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class TransparentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
