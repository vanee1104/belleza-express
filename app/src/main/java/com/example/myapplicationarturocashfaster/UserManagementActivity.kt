package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.security.MessageDigest

class UserManagementActivity : AppCompatActivity() {

    // Views
    private lateinit var userIdEditText: EditText
    private lateinit var identificationEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var searchIdentificationEditText: EditText
    private lateinit var btnSearch: Button
    private lateinit var errorTextView: TextView
    private lateinit var normalUserRadio: RadioButton
    private lateinit var adminUserRadio: RadioButton
    private lateinit var superadminUserRadio: RadioButton
    private lateinit var btnCreate: Button
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button
    private lateinit var btnGoogle: Button

    // Estados
    private var currentMode: String = "view"
    private var currentUser: JSONObject? = null
    private var currentUserId: String? = null

    // Corrutina
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        initViews()
        setupListeners()
        // ✅ COMENTADO TEMPORALMENTE
        // testConnection()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    /** --- INICIALIZAR --- **/
    private fun initViews() {
        userIdEditText = findViewById(R.id.editUserId)
        identificationEditText = findViewById(R.id.editIdentification)
        usernameEditText = findViewById(R.id.editUsername)
        emailEditText = findViewById(R.id.editEmail)
        passwordEditText = findViewById(R.id.editPassword)
        searchIdentificationEditText = findViewById(R.id.editSearchIdentification)
        btnSearch = findViewById(R.id.btnSearch)
        errorTextView = findViewById(R.id.errorTextView)

        normalUserRadio = findViewById(R.id.radioNormal)
        adminUserRadio = findViewById(R.id.radioAdmin)
        superadminUserRadio = findViewById(R.id.radioSuperadmin)

        btnCreate = findViewById(R.id.btnCreate)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)
        btnGoogle = findViewById(R.id.btnGoogle)
    }

    private fun setupListeners() {
        btnCreate.setOnClickListener { switchMode("create") }
        btnEdit.setOnClickListener { switchMode("edit") }
        btnDelete.setOnClickListener { confirmDelete() }
        btnCancel.setOnClickListener { switchMode("view", true) }
        btnSave.setOnClickListener { saveUser() }
        btnSearch.setOnClickListener { searchUserByIdentification() }
        btnGoogle.setOnClickListener { abrirGoogle() }
    }

    /** --- ENCRIPTACIÓN --- **/
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    /** --- CONEXIÓN --- **/
    private fun testConnection() {
        // ✅ COMENTADO TEMPORALMENTE
        /*
        scope.launch {
            val (isConnected, message) = SupabaseManager.testConnection()
            withContext(Dispatchers.Main) {
                if (isConnected) showSuccess("✅ $message")
                else showErrorDetails("❌ $message")
            }
        }
        */
    }

    /** --- BÚSQUEDA --- **/
    private fun searchUserByIdentification() {
        val identification = searchIdentificationEditText.text.toString().trim()
        if (identification.isEmpty()) {
            showError("Por favor, ingresa una identificación")
            return
        }

        // Show loading
        btnSearch.isEnabled = false
        btnSearch.text = "Buscando..."

        scope.launch {
            // ✅ COMENTADO TEMPORALMENTE - Reemplazar con mensaje temporal
            /*
            val (user, message) = SupabaseManager.searchUserByIdentification(identification)
            withContext(Dispatchers.Main) {
                btnSearch.isEnabled = true
                btnSearch.text = "Buscar"

                if (user != null) {
                    currentUser = user
                    currentUserId = user.optString("Id_Usuario")
                    populateForm(user)
                    showSuccess("✅ $message")
                } else {
                    showErrorDetails("❌ $message")
                    clearForm()
                }
            }
            */

            // ✅ VERSIÓN TEMPORAL - Simular búsqueda
            withContext(Dispatchers.Main) {
                btnSearch.isEnabled = true
                btnSearch.text = "Buscar"
                showErrorDetails("⚠️ Función de búsqueda temporalmente deshabilitada")
            }
        }
    }

    /** --- MODO --- **/
    private fun switchMode(mode: String, cancel: Boolean = false) {
        currentMode = mode
        when (mode) {
            "create" -> {
                clearForm()
                enableForm(true)
                showSuccess("Modo creación activado")
            }
            "edit" -> {
                if (currentUser == null) {
                    showError("Selecciona un usuario antes de editar")
                    return
                }
                enableForm(true)
                showSuccess("Modo edición activado")
            }
            "view" -> {
                if (cancel) showSuccess("Acción cancelada")
                enableForm(false)
                clearForm()
            }
        }
        clearErrorDetails()
    }

    private fun enableForm(enabled: Boolean) {
        identificationEditText.isEnabled = enabled
        usernameEditText.isEnabled = enabled
        emailEditText.isEnabled = enabled
        passwordEditText.isEnabled = enabled
        normalUserRadio.isEnabled = enabled
        adminUserRadio.isEnabled = enabled
        superadminUserRadio.isEnabled = enabled
    }

    /** --- GUARDAR / ELIMINAR --- **/
    private fun saveUser() {
        val nombreUsuario = usernameEditText.text.toString().trim()
        val identificacion = identificationEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val contrasena = passwordEditText.text.toString()

        val roles = listOf(
            normalUserRadio.isChecked,
            adminUserRadio.isChecked,
            superadminUserRadio.isChecked
        )

        // Validaciones
        if (nombreUsuario.isEmpty() || identificacion.isEmpty()) {
            showError("Completa los campos obligatorios")
            return
        }
        if (email.isNotEmpty() && !isValidEmail(email)) {
            showError("Correo inválido")
            return
        }
        if (roles.count { it } != 1) {
            showError("Debes seleccionar exactamente un rol")
            return
        }

        // Show loading
        btnSave.isEnabled = false
        btnSave.text = "Guardando..."

        scope.launch {
            try {
                // ✅ COMENTADO TEMPORALMENTE - Reemplazar con mensaje temporal
                /*
                val result: Pair<Boolean, String> = when (currentMode) {
                    "create" -> {
                        val userData = mapOf<String, Any>(
                            "Identificacion" to identificacion.toLong(),
                            "Nombre_Usuario" to nombreUsuario,
                            "email" to (if (email.isNotEmpty()) email else ""),
                            "Clave_Encriptada" to hashPassword(if (contrasena.isNotEmpty()) contrasena else "default123"),
                            "Usuario_Normal" to (if (roles[0]) 1 else 0),
                            "Usuario_Administrador" to (if (roles[1]) 1 else 0),
                            "Usuario_Superadministrador" to (if (roles[2]) 1 else 0)
                        )
                        SupabaseManager.createUser(userData)
                    }
                    "edit" -> {
                        if (currentUserId == null) {
                            withContext(Dispatchers.Main) {
                                btnSave.isEnabled = true
                                btnSave.text = "Guardar"
                                showErrorDetails("❌ ID de usuario no válido")
                            }
                            return@launch
                        }
                        val userData = mutableMapOf<String, Any>(
                            "Identificacion" to identificacion.toLong(),
                            "Nombre_Usuario" to nombreUsuario,
                            "email" to (if (email.isNotEmpty()) email else ""),
                            "Usuario_Normal" to (if (roles[0]) 1 else 0),
                            "Usuario_Administrador" to (if (roles[1]) 1 else 0),
                            "Usuario_Superadministrador" to (if (roles[2]) 1 else 0)
                        )
                        if (contrasena.isNotBlank()) {
                            userData["Clave_Encriptada"] = hashPassword(contrasena)
                        }
                        SupabaseManager.updateUser(currentUserId!!, userData)
                    }
                    else -> {
                        withContext(Dispatchers.Main) {
                            btnSave.isEnabled = true
                            btnSave.text = "Guardar"
                            showErrorDetails("❌ Modo no válido")
                        }
                        return@launch
                    }
                }

                withContext(Dispatchers.Main) {
                    btnSave.isEnabled = true
                    btnSave.text = "Guardar"

                    if (result.first) {
                        showSuccess("✅ ${result.second}")
                        switchMode("view")
                    } else {
                        showErrorDetails("❌ ${result.second}")
                    }
                }
                */

                // ✅ VERSIÓN TEMPORAL - Simular guardado
                delay(1000) // Simular proceso
                withContext(Dispatchers.Main) {
                    btnSave.isEnabled = true
                    btnSave.text = "Guardar"
                    showErrorDetails("⚠️ Función de guardado temporalmente deshabilitada")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnSave.isEnabled = true
                    btnSave.text = "Guardar"
                    showErrorDetails("❌ Error: ${e.message}")
                }
            }
        }
    }

    private fun confirmDelete() {
        if (currentUserId == null) {
            showError("Selecciona un usuario antes de eliminar")
            return
        }
        AlertDialog.Builder(this)
            .setTitle("Eliminar usuario")
            .setMessage("¿Seguro de eliminar este usuario?")
            .setPositiveButton("Sí") { _, _ -> deleteUser(currentUserId!!) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteUser(userId: String) {
        // Show loading
        btnDelete.isEnabled = false

        scope.launch {
            try {
                // ✅ COMENTADO TEMPORALMENTE - Reemplazar con mensaje temporal
                /*
                val result = SupabaseManager.deactivateUser(userId)
                withContext(Dispatchers.Main) {
                    btnDelete.isEnabled = true

                    if (result.first) {
                        showSuccess("✅ ${result.second}")
                        switchMode("view")
                        currentUser = null
                        currentUserId = null
                    } else {
                        showErrorDetails("❌ ${result.second}")
                    }
                }
                */

                // ✅ VERSIÓN TEMPORAL - Simular eliminación
                delay(1000) // Simular proceso
                withContext(Dispatchers.Main) {
                    btnDelete.isEnabled = true
                    showErrorDetails("⚠️ Función de eliminación temporalmente deshabilitada")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    btnDelete.isEnabled = true
                    showErrorDetails("❌ Error: ${e.message}")
                }
            }
        }
    }

    /** --- FORMULARIO --- **/
    private fun clearForm() {
        listOf(
            userIdEditText, identificationEditText, usernameEditText,
            emailEditText, passwordEditText, searchIdentificationEditText
        ).forEach { it.setText("") }

        normalUserRadio.isChecked = true
        adminUserRadio.isChecked = false
        superadminUserRadio.isChecked = false
    }

    private fun populateForm(user: JSONObject) {
        userIdEditText.setText(user.optString("Id_Usuario"))
        identificationEditText.setText(user.optString("Identificacion"))
        usernameEditText.setText(user.optString("Nombre_Usuario"))
        emailEditText.setText(user.optString("email"))
        passwordEditText.setText("")

        val isNormal = user.optInt("Usuario_Normal") == 1
        val isAdmin = user.optInt("Usuario_Administrador") == 1
        val isSuperadmin = user.optInt("Usuario_Superadministrador") == 1

        normalUserRadio.isChecked = isNormal
        adminUserRadio.isChecked = isAdmin
        superadminUserRadio.isChecked = isSuperadmin
    }

    /** --- UTILIDADES --- **/
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showSuccess(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        errorTextView.visibility = TextView.GONE
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDetails(error: String) {
        errorTextView.text = error
        errorTextView.visibility = TextView.VISIBLE
    }

    private fun clearErrorDetails() {
        errorTextView.text = ""
        errorTextView.visibility = TextView.GONE
    }

    /** --- ABRIR GOOGLE --- **/
    private fun abrirGoogle() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
        startActivity(intent)
    }
}