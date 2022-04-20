package com.example.periodictable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.periodictable.databinding.ActivityElementObjectBinding

class ElementObjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElementObjectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElementObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}