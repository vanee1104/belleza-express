package com.example.myapplicationarturocashfaster

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserProfileActivity : AppCompatActivity() {  // <- CAMBIÉ el nombre aquí
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)  // <- Y el layout aquí
        Toast.makeText(this, "Profile - Coming Soon!", Toast.LENGTH_SHORT).show()
    }
}