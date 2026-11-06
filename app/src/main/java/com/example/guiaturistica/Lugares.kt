package com.example.guiaturistica

import androidx.annotation.DrawableRes
enum class CategoriaLugaresTuristicos {HISTORICOS, GASTRONOMIA, MUSEOS}

data class Lugar (
    val id: Int,
    val nombre : String,
    val descripcion: String,
    val direccion: String,
    val telefono: String,
    val web: String,
    val categoria: CategoriaLugaresTuristicos,
    @DrawableRes val imagen: Int,
    val latitud: Double,
    val longitud: Double
)

object LugaresTuristicos {
    val lugares = listOf(
        Lugar(1, "Sagrada Familia", "Basilica",
            "Carrer de Mallorca 401", "932 080 414", "https://sagradafamilia.org/home",
            CategoriaLugaresTuristicos.HISTORICOS, R.drawable.sagrada_familia,
            41.403241, 2.175014),
        Lugar(2, "CosmoCaixa", "Museo de Ciencias",
            "Carrer d'Isaac Newton 26", "932 126 050", "https://www.cosmocaixa.org",
            CategoriaLugaresTuristicos.MUSEOS, R.drawable.cosmocaixa_barcelona,
            41.413501, 2.130776),
        Lugar(3, "El Nacional", "Zona de alta restauración",
            "Passeig de Gràcia 24", "934 876 123", "https://www.elnacionalbcn.com",
            CategoriaLugaresTuristicos.GASTRONOMIA, R.drawable.el_nacional,
            41.390149, 2.167782)
    )

    fun filtroCategoria (categoria: CategoriaLugaresTuristicos) =
        lugares.filter {it.categoria == categoria}

    fun buscar (id: Int) =
        lugares.find {it.id == id}
}

