package com.example.myapplicationarturocashfaster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookings)
        Toast.makeText(this, "Bookings - Coming Soon!", Toast.LENGTH_SHORT).show()
    }
}