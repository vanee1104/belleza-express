package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplicationarturocashfaster.adapters.SliderAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewPagerSlider: ViewPager2
    private lateinit var layoutDots: LinearLayout
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var dots: Array<ImageView?>

    private val sliderImages = intArrayOf(
        R.drawable.slider1,           // Tu imagen 1 del slider
        R.drawable.slider1_interior,  // Tu imagen 2 del slider
        R.drawable.slider2,           // Tu imagen 3 del slider
        R.drawable.slider2_interior,  // Tu imagen 4 del slider
        R.drawable.slider3,           // Tu imagen 5 del slider
        R.drawable.slider3_interior,  // Tu imagen 6 del slider
        R.drawable.slider4,           // Tu imagen 7 del slider
        R.drawable.slider4_interior,  // Tu imagen 8 del slider
    )

    private var currentPage = 0
    private val sliderDelay: Long = 2000 // 2 segundos
    private val handler = Handler(Looper.getMainLooper())

    // Variable para controlar si el slider está activo
    private var isSliderActive = true

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (isSliderActive) {
                currentPage = (currentPage + 1) % sliderImages.size
                viewPagerSlider.setCurrentItem(currentPage, true)
                handler.postDelayed(this, sliderDelay)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        initViews()
        setupSlider()
        setupClickListeners()

        // Iniciar el slider automático
        startAutoSlider()
    }

    private fun initViews() {
        viewPagerSlider = findViewById(R.id.viewPagerSlider)
        layoutDots = findViewById(R.id.layoutDots)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
    }

    private fun setupSlider() {
        sliderAdapter = SliderAdapter(sliderImages.toList())
        viewPagerSlider.adapter = sliderAdapter
        setupDots()

        viewPagerSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                setCurrentDot(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        pauseAutoSlider() // Pausar cuando el usuario interactúa
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        // Reanudar después de 2 segundos de inactividad
                        handler.postDelayed({
                            startAutoSlider()
                        }, sliderDelay)
                    }
                }
            }
        })
    }

    // ✅ NUEVA FUNCIÓN: Iniciar slider automático
    private fun startAutoSlider() {
        isSliderActive = true
        handler.removeCallbacks(sliderRunnable) // Limpiar cualquier callback previo
        handler.postDelayed(sliderRunnable, sliderDelay)
    }

    // ✅ NUEVA FUNCIÓN: Pausar slider automático
    private fun pauseAutoSlider() {
        isSliderActive = false
        handler.removeCallbacks(sliderRunnable)
    }

    private fun setupDots() {
        dots = arrayOfNulls(sliderImages.size)
        layoutDots.removeAllViews()

        for (i in dots.indices) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.dot_inactive)
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            layoutDots.addView(dots[i], params)
        }

        setCurrentDot(0)
    }

    private fun setCurrentDot(position: Int) {
        for (i in dots.indices) {
            val drawable = if (i == position) {
                R.drawable.dot_active
            } else {
                R.drawable.dot_inactive
            }
            dots[i]?.setImageResource(drawable)
        }
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reanudar slider cuando la app vuelve al frente
        startAutoSlider()
    }

    override fun onPause() {
        super.onPause()
        // Pausar slider cuando la app va a segundo plano
        pauseAutoSlider()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar recursos
        pauseAutoSlider()
    }
}