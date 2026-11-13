package com.example.guiaturistica

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.guiaturistica.ui.theme.GuiaTuristicaTheme

const val LUGAR_ID = "id_lugar"
class DetalleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val idLugar = intent.getIntExtra(LUGAR_ID, -1)
        val lugar = LugaresTuristicos.buscar(idLugar)
        if (lugar == null) {
            finish()
            return
        }
        setContent {
            GuiaTuristicaTheme {
                PantallaDetalle(lugar = lugar, alVolver = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle (lugar : Lugar, alVolver: () -> Unit) {
    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(lugar.nombre) },
                navigationIcon = {
                    IconButton(onClick = alVolver) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${lugar.nombre}\n${lugar.direccion}\n${lugar.web}"
                                )
                            }
                            LanzarIntent(context, Intent.createChooser(intent, "Compartir con"))
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = lugar.imagen),
                contentDescription = lugar.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f)
            )
            Column (modifier = Modifier.padding(16.dp)) {
                Text(text = lugar.nombre, style = MaterialTheme.typography.headlineSmall)
                Text(text = lugar.descripcion, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))

                DatosEnlace(
                    etiqueta = "Dirección",
                    valor = lugar.direccion,
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(
                            "geo:${lugar.latitud},${lugar.longitud}" +
                            "?q=${lugar.latitud},${lugar.longitud}(${Uri.encode(lugar.nombre)})"
                        )
                    )
                )

                DatosEnlace(
                    etiqueta = "Teléfono",
                    valor = lugar.telefono,
                    intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${lugar.telefono.replace(" ", "")}")
                    )
                )

                DatosEnlace(
                    etiqueta = "Web",
                    valor = lugar.web,
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(lugar.web))
                )
            }
        }
    }
}

@Composable
fun DatosEnlace (etiqueta :String, valor: String, intent: Intent) {

    val context = LocalContext.current

    Row (verticalAlignment = Alignment.CenterVertically) {
        Text(text = "$etiqueta: ")
        Text(
            text = valor,
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { LanzarIntent(context, intent) }.padding(vertical = 8.dp)
        )
    }
}
fun LanzarIntent (context : Context, intent : Intent) {
    try {
        context.startActivity(intent)
    } catch (e : ActivityNotFoundException) {
     Toast.makeText(context, "No se encontró la aplicación", Toast.LENGTH_SHORT).show()
    }
}