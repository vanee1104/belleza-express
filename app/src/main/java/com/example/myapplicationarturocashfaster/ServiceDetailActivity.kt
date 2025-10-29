package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var serviceImage: ImageView
    private lateinit var serviceName: TextView
    private lateinit var serviceDescription: TextView
    private lateinit var servicePrice: TextView
    private lateinit var btnBookService: Button
    private lateinit var btnBack: Button

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_detail)

        initViews()
        setupServiceData()
        setupListeners()
    }

    private fun initViews() {
        serviceImage = findViewById(R.id.service_detail_image)
        serviceName = findViewById(R.id.service_detail_name)
        serviceDescription = findViewById(R.id.service_detail_description)
        servicePrice = findViewById(R.id.service_detail_price)
        btnBookService = findViewById(R.id.btnBookService)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupServiceData() {
        val service = intent.getParcelableExtra<Service>("service")

        service?.let { s ->
            serviceImage.setImageResource(s.imageResId)
            serviceName.text = s.name
            serviceDescription.text = s.detailedDescription
            servicePrice.text = "$${s.price}"

            // Set up the booking button
            btnBookService.text = "Reservar ${s.name} - $${s.price}"
        } ?: run {
            Toast.makeText(this, "Error loading service details", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            val service = intent.getParcelableExtra<Service>("service")
            service?.let { s ->
                bookService(s)
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bookService(service: Service) {
        // Show loading state
        btnBookService.isEnabled = false
        btnBookService.text = "Procesando..."

        scope.launch {
            try {
                // Simulate API call to book service
                // Replace this with your actual Supabase booking logic
                Thread.sleep(1000) // Simulate network delay

                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar ${service.name} - $${service.price}"

                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "¡${service.name} reservado exitosamente!",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navigate to booking confirmation or back to main
                    val intent = Intent(this@ServiceDetailActivity, UserDashboardActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar ${service.name} - $${service.price}"
                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "Error al reservar: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}