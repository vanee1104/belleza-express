package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
        setupBackPressedHandler()
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

    private fun setupBackPressedHandler() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun showSampleServiceData() {
        // ✅ ACTUALIZADO: Datos de ejemplo mejorados
        serviceImage.setImageResource(android.R.drawable.ic_menu_edit) // Puedes cambiar por una imagen específica
        serviceName.text = "Diseño Arquitectónico Premium"
        serviceDescription.text = "Servicio completo de diseño arquitectónico profesional incluyendo planos detallados, renders 3D y asesoría personalizada. Perfecto para proyectos residenciales y comerciales."
        servicePrice.text = "$500.00"

        // ✅ ACTUALIZADO: Texto del botón más descriptivo
        btnBookService.text = "Reservar por $500.00"
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            bookService()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bookService() {
        btnBookService.isEnabled = false
        btnBookService.text = "Procesando reserva..."

        scope.launch {
            try {
                Thread.sleep(1000)

                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar por $500.00"

                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "✅ Servicio reservado exitosamente",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navegar al perfil del usuario después de reservar
                    val intent = Intent(this@ServiceDetailActivity, UserProfileActivity::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar por $500.00"
                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "❌ Error al reservar: ${e.message ?: "Error desconocido"}",
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