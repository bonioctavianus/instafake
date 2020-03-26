package com.bonioctavianus.android.instafake.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonioctavianus.android.instafake.R

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        title = ""
    }
}
