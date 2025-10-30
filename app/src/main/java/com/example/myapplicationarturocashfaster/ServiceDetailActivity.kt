package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
    private lateinit var serviceDuration: TextView
    private lateinit var btnBookService: Button
    private lateinit var btnContact: Button
    private lateinit var btnBackBottom: Button
    private lateinit var btnBack: ImageButton

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
        // ✅ CORREGIDO: ImageButton para el botón de retroceso
        btnBack = findViewById(R.id.btnBack)
        serviceImage = findViewById(R.id.service_detail_image)
        serviceName = findViewById(R.id.service_detail_name)
        serviceDescription = findViewById(R.id.service_detail_description)
        servicePrice = findViewById(R.id.service_detail_price)
        serviceDuration = findViewById(R.id.service_detail_duration)
        btnBookService = findViewById(R.id.btnBookService)
        btnContact = findViewById(R.id.btnContact)
        btnBackBottom = findViewById(R.id.btnBackBottom)
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
        // Datos de ejemplo para el servicio
        serviceImage.setImageResource(R.drawable.tratamiento_facial)
        serviceName.text = "Tratamiento Facial Premium"
        serviceDescription.text = "Servicio profesional de tratamiento facial que incluye limpieza profunda, exfoliación, hidratación y mascarilla personalizada según tu tipo de piel. Resultados visibles desde la primera sesión."
        servicePrice.text = "$120.000"
        serviceDuration.text = "Duración: 60 minutos"

        // Texto del botón más descriptivo
        btnBookService.text = "Reservar por $120.000"
    }

    private fun setupListeners() {
        btnBookService.setOnClickListener {
            bookService()
        }

        btnContact.setOnClickListener {
            contactService()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnBackBottom.setOnClickListener {
            finish()
        }
    }

    private fun bookService() {
        btnBookService.isEnabled = false
        btnBookService.text = "Procesando reserva..."

        scope.launch {
            try {
                // Simular proceso de reserva
                Thread.sleep(1500)

                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar por $120.000"

                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "✅ Servicio reservado exitosamente",
                        Toast.LENGTH_LONG
                    ).show()

                    // Navegar al perfil del usuario después de reservar
                    val intent = Intent(this@ServiceDetailActivity, UserProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    btnBookService.isEnabled = true
                    btnBookService.text = "Reservar por $120.000"
                    Toast.makeText(
                        this@ServiceDetailActivity,
                        "❌ Error al reservar: ${e.message ?: "Error desconocido"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun contactService() {
        Toast.makeText(
            this,
            "📞 Contactando con el especialista...",
            Toast.LENGTH_SHORT
        ).show()

        // Aquí puedes agregar lógica para:
        // - Abrir WhatsApp
        // - Hacer una llamada
        // - Abrir formulario de contacto
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}