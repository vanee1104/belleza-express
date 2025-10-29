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
        setupListeners()

        // Mostrar datos de ejemplo
        showSampleServiceData()
    }

    private fun initViews() {
        serviceImage = findViewById(R.id.service_detail_image)
        serviceName = findViewById(R.id.service_detail_name)
        serviceDescription = findViewById(R.id.service_detail_description)
        servicePrice = findViewById(R.id.service_detail_price)
        btnBookService = findViewById(R.id.btnBookService)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun showSampleServiceData() {
        // Datos de ejemplo para el servicio
        serviceImage.setImageResource(android.R.drawable.ic_menu_edit) // Icono genérico
        serviceName.text = "Diseño Arquitectónico Residencial"
        serviceDescription.text = "Servicio completo de diseño y planos para viviendas residenciales. Incluye consultoría, planos arquitectónicos y seguimiento del proyecto."
        servicePrice.text = "$500.00"

        btnBookService.text = "Reservar Diseño Residencial - $500.00"
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            bookService()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bookService() {
        // Show loading state
        btnBookService.isEnabled = false
        btnBookService.text = "Procesando..."

        scope.launch {
            try {
                // Simulate API call to book service
                Thread.sleep(1000) // Simulate network delay

                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar Diseño Residencial - $500.00"

                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "¡Servicio reservado exitosamente!",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navigate to booking confirmation or back to main
                    val intent = Intent(this@ServiceDetailActivity, UserDashboardActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar Diseño Residencial - $500.00"
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