package com.example.guiaturistica

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio () {
    val categorias = CategoriaLugaresTuristicos.entries
    val estadoPager = rememberPagerState (pageCount = {categorias.size}) //.currentPage para la página actual
    val scope = rememberCoroutineScope()

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Guia Turistica, Barcelona")})
        }
    ) {innerPadding ->
        Column (modifier = Modifier.padding(innerPadding)) {
            PrimaryTabRow(selectedTabIndex = estadoPager.currentPage) {
                categorias.forEachIndexed { indice, categoria ->
                    Tab(
                        selected = estadoPager.currentPage == indice,
                        onClick = { scope.launch { estadoPager.animateScrollToPage(indice) } },
                        text = { Text(categoria.titulo) }
                    )
                }
            }
            HorizontalPager(
                state = estadoPager,
                modifier = Modifier.fillMaxSize()
            ) { pagina ->
                val categoria = categorias[pagina]
                ListaLugares(categoria = categoria)
            }
        }
    }
}

@Composable
fun TarjetaLugar (lugar : Lugar, alPulsar: () -> Unit ){
    Card (
        onClick = alPulsar,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column {
            Image (
                painter = painterResource(id = lugar.imagen),
                contentDescription = lugar.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f)
            )
            Text(
                text = lugar.nombre,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ListaLugares (categoria : CategoriaLugaresTuristicos) {

    val lugares = LugaresTuristicos.filtroCategoria(categoria)
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lugares) {lugar ->
            TarjetaLugar(
                lugar = lugar,
                alPulsar = {
                    val intent = Intent(context, DetalleActivity::class.java)
                    intent.putExtra(LUGAR_ID, lugar.id)
                    context.startActivity(intent)
                }
            )
        }
    }
}

