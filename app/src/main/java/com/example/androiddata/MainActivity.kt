package com.example.androiddata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddata.databinding.MainActivityBinding
import com.example.androiddata.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}
