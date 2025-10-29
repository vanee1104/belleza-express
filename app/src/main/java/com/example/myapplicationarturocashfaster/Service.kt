package com.example.myapplicationarturocashfaster

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Service(
    val id: String = "", // ID único del servicio
    val name: String,
    val description: String,
    val imageUrl: String = "", // URL de imagen desde internet
    val imageResId: Int = 0, // Resource ID para imágenes locales
    val price: Double,
    val currency: String = "USD", // Moneda
    val detailedDescription: String,
    val category: String = "General", // Categoría del servicio
    val duration: String = "", // Duración estimada
    val rating: Double = 0.0, // Rating de 0-5
    val reviewCount: Int = 0, // Número de reseñas
    val isAvailable: Boolean = true, // Disponibilidad
    val features: List<String> = emptyList(), // Características del servicio
    val requirements: List<String> = emptyList(), // Requisitos
    val tags: List<String> = emptyList() // Etiquetas para búsqueda
) : Parcelable {

    // Constructor secundario para compatibilidad
    constructor(
        name: String,
        description: String,
        imageResId: Int,
        price: Double,
        detailedDescription: String
    ) : this(
        id = "",
        name = name,
        description = description,
        imageUrl = "",
        imageResId = imageResId,
        price = price,
        detailedDescription = detailedDescription,
        category = "General",
        duration = "",
        rating = 0.0,
        reviewCount = 0,
        isAvailable = true,
        features = emptyList(),
        requirements = emptyList(),
        tags = emptyList()
    )

    // Constructor para servicios con URL de imagen
    constructor(
        name: String,
        description: String,
        imageUrl: String,
        price: Double,
        detailedDescription: String,
        category: String
    ) : this(
        id = "",
        name = name,
        description = description,
        imageUrl = imageUrl,
        imageResId = 0,
        price = price,
        detailedDescription = detailedDescription,
        category = category,
        duration = "",
        rating = 0.0,
        reviewCount = 0,
        isAvailable = true,
        features = emptyList(),
        requirements = emptyList(),
        tags = emptyList()
    )

    // Métodos de utilidad
    fun getFormattedPrice(): String {
        return "$${String.format("%.2f", price)} $currency"
    }

    fun getRatingText(): String {
        return if (reviewCount > 0) {
            String.format("%.1f", rating)
        } else {
            "No ratings"
        }
    }

    fun getReviewsText(): String {
        return if (reviewCount == 1) {
            "($reviewCount review)"
        } else {
            "($reviewCount reviews)"
        }
    }

    fun getAvailabilityText(): String {
        return if (isAvailable) {
            "Available"
        } else {
            "Not Available"
        }
    }

    fun getAvailabilityColor(): Int {
        return if (isAvailable) {
            android.graphics.Color.parseColor("#4CAF50") // Verde
        } else {
            android.graphics.Color.parseColor("#F44336") // Rojo
        }
    }

    // Verificar si tiene imagen
    fun hasImage(): Boolean {
        return imageUrl.isNotEmpty() || imageResId != 0
    }

    // Obtener la fuente de imagen preferida
    fun getPreferredImageSource(): String {
        return if (imageUrl.isNotEmpty()) {
            "url"
        } else if (imageResId != 0) {
            "resource"
        } else {
            "none"
        }
    }

    // Crear copia con nuevos valores
    fun copyWith(
        id: String? = null,
        name: String? = null,
        description: String? = null,
        imageUrl: String? = null,
        imageResId: Int? = null,
        price: Double? = null,
        currency: String? = null,
        detailedDescription: String? = null,
        category: String? = null,
        duration: String? = null,
        rating: Double? = null,
        reviewCount: Int? = null,
        isAvailable: Boolean? = null,
        features: List<String>? = null,
        requirements: List<String>? = null,
        tags: List<String>? = null
    ): Service {
        return this.copy(
            id = id ?: this.id,
            name = name ?: this.name,
            description = description ?: this.description,
            imageUrl = imageUrl ?: this.imageUrl,
            imageResId = imageResId ?: this.imageResId,
            price = price ?: this.price,
            currency = currency ?: this.currency,
            detailedDescription = detailedDescription ?: this.detailedDescription,
            category = category ?: this.category,
            duration = duration ?: this.duration,
            rating = rating ?: this.rating,
            reviewCount = reviewCount ?: this.reviewCount,
            isAvailable = isAvailable ?: this.isAvailable,
            features = features ?: this.features,
            requirements = requirements ?: this.requirements,
            tags = tags ?: this.tags
        )
    }

    companion object {

        // Servicios de ejemplo
        fun getSampleServices(): List<Service> {
            return listOf(
                Service(
                    id = "1",
                    name = "Architectural Design",
                    description = "Custom architectural designs for residential and commercial buildings",
                    imageUrl = "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=400",
                    price = 2500.00,
                    currency = "USD",
                    detailedDescription = "Our architectural design service provides comprehensive planning and design solutions for residential and commercial projects. We work closely with clients to create functional, sustainable, and aesthetically pleasing spaces that meet their specific needs and budget.",
                    category = "Design",
                    duration = "4-6 weeks",
                    rating = 4.8,
                    reviewCount = 124,
                    isAvailable = true,
                    features = listOf(
                        "3D Modeling",
                        "Site Analysis",
                        "Building Codes Compliance",
                        "Sustainable Design",
                        "Construction Documents"
                    ),
                    requirements = listOf(
                        "Property Survey",
                        "Budget Estimate",
                        "Project Timeline"
                    ),
                    tags = listOf("architecture", "design", "planning", "3d")
                ),
                Service(
                    id = "2",
                    name = "Structural Engineering",
                    description = "Professional structural analysis and design services",
                    imageUrl = "https://images.unsplash.com/photo-1541888946425-d81bb19240f5?w=400",
                    price = 1800.00,
                    currency = "USD",
                    detailedDescription = "Our structural engineering services ensure the safety and stability of your building projects. We provide detailed analysis, calculations, and design solutions for various structural systems and materials.",
                    category = "Engineering",
                    duration = "2-3 weeks",
                    rating = 4.9,
                    reviewCount = 89,
                    isAvailable = true,
                    features = listOf(
                        "Structural Analysis",
                        "Load Calculations",
                        "Material Specifications",
                        "Seismic Design",
                        "Inspection Services"
                    ),
                    requirements = listOf(
                        "Architectural Drawings",
                        "Site Conditions",
                        "Local Building Codes"
                    ),
                    tags = listOf("engineering", "structural", "safety", "analysis")
                ),
                Service(
                    id = "3",
                    name = "Interior Design",
                    description = "Complete interior design solutions for modern living",
                    imageUrl = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400",
                    price = 1200.00,
                    currency = "USD",
                    detailedDescription = "Transform your interior spaces with our professional interior design services. We create beautiful, functional, and personalized environments that reflect your style and enhance your quality of life.",
                    category = "Design",
                    duration = "3-4 weeks",
                    rating = 4.7,
                    reviewCount = 156,
                    isAvailable = true,
                    features = listOf(
                        "Space Planning",
                        "Color Consultation",
                        "Furniture Selection",
                        "Lighting Design",
                        "Material Selection"
                    ),
                    requirements = listOf(
                        "Room Measurements",
                        "Style Preferences",
                        "Budget Range"
                    ),
                    tags = listOf("interior", "design", "decor", "furniture")
                ),
                Service(
                    id = "4",
                    name = "Project Management",
                    description = "Comprehensive construction project management",
                    imageUrl = "https://images.unsplash.com/photo-1504307651254-35680f356dfd?w=400",
                    price = 3000.00,
                    currency = "USD",
                    detailedDescription = "Our project management service ensures your construction project stays on schedule and within budget. We coordinate all aspects of the project from planning to completion.",
                    category = "Management",
                    duration = "Project-based",
                    rating = 4.6,
                    reviewCount = 67,
                    isAvailable = true,
                    features = listOf(
                        "Budget Management",
                        "Schedule Coordination",
                        "Quality Control",
                        "Contractor Management",
                        "Risk Assessment"
                    ),
                    requirements = listOf(
                        "Project Scope",
                        "Budget Details",
                        "Timeline Requirements"
                    ),
                    tags = listOf("management", "coordination", "planning", "supervision")
                ),
                Service(
                    id = "5",
                    name = "Landscape Architecture",
                    description = "Beautiful and sustainable landscape designs",
                    imageUrl = "https://images.unsplash.com/photo-1560717789-0ac7c58ac90a?w=400",
                    price = 1500.00,
                    currency = "USD",
                    detailedDescription = "Create stunning outdoor spaces with our landscape architecture services. We design sustainable, functional, and beautiful landscapes that complement your property and lifestyle.",
                    category = "Design",
                    duration = "3-5 weeks",
                    rating = 4.5,
                    reviewCount = 92,
                    isAvailable = true,
                    features = listOf(
                        "Site Planning",
                        "Plant Selection",
                        "Irrigation Design",
                        "Hardscape Design",
                        "Sustainable Practices"
                    ),
                    requirements = listOf(
                        "Property Size",
                        "Soil Conditions",
                        "Climate Considerations"
                    ),
                    tags = listOf("landscape", "garden", "outdoor", "sustainable")
                ),
                Service(
                    id = "6",
                    name = "Building Inspection",
                    description = "Thorough building inspection and assessment",
                    imageUrl = "https://images.unsplash.com/photo-1560518883-ce09059eeffa?w=400",
                    price = 500.00,
                    currency = "USD",
                    detailedDescription = "Our comprehensive building inspection service identifies potential issues and ensures your property meets all safety standards. We provide detailed reports with recommendations for maintenance and repairs.",
                    category = "Inspection",
                    duration = "1-2 days",
                    rating = 4.8,
                    reviewCount = 203,
                    isAvailable = true,
                    features = listOf(
                        "Structural Assessment",
                        "Electrical Inspection",
                        "Plumbing Check",
                        "Safety Compliance",
                        "Detailed Reporting"
                    ),
                    requirements = listOf(
                        "Property Access",
                        "Previous Documentation",
                        "Specific Concerns"
                    ),
                    tags = listOf("inspection", "safety", "assessment", "maintenance")
                )
            )
        }

        // Obtener servicios por categoría
        fun getServicesByCategory(category: String): List<Service> {
            return getSampleServices().filter { it.category.equals(category, ignoreCase = true) }
        }

        // Buscar servicios por tags
        fun searchServices(query: String): List<Service> {
            return getSampleServices().filter { service ->
                service.name.contains(query, ignoreCase = true) ||
                        service.description.contains(query, ignoreCase = true) ||
                        service.tags.any { it.contains(query, ignoreCase = true) }
            }
        }

        // Obtener todas las categorías
        fun getAllCategories(): List<String> {
            return getSampleServices().map { it.category }.distinct()
        }
    }
}