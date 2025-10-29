package com.example.myapplicationarturocashfaster

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // ✅ CORRECTO: Ya NO llamar a initialize()
        // El SupabaseManager actual es un objeto simple que no necesita inicialización

        // Puedes poner aquí otras inicializaciones si necesitas:
        // - Firebase
        // - Analytics
        // - Base de datos local
        // - etc.
    }
}