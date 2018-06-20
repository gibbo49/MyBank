package com.chrisgibson.mybank.Controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.chrisgibson.mybank.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        animation_view.setOnClickListener {
            animation_view.playAnimation()
        }
    }
}
