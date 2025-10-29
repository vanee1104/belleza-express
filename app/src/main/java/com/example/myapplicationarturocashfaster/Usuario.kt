package com.example.myapplicationarturocashfaster

import java.io.Serializable

data class Usuario(
    val Id_Usuario: Long? = null,
    val Identificacion: Long,
    val Nombre_Usuario: String,
    val Clave_Encriptada: String,
    val Usuario_Normal: Long = 1,
    val Usuario_Administrador: Long = 0,
    val Usuario_Superadministrador: Long = 0,
    val email: String? = null
) : Serializable  // <- Usa Serializable de Java en lugar de Kotlin serialization