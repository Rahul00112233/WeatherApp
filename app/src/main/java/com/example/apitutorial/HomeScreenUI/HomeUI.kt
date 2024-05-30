package com.example.apitutorial.HomeScreenUI

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apitutorial.R
import com.example.apitutorial.WeatherViewModel
import com.example.apitutorial.api.NetworkResponse
import com.example.apitutorial.model.WeatherModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: WeatherViewModel) {

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = R.drawable.bg3),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize())
        Column(modifier = Modifier.fillMaxSize()) {
            var city by remember { mutableStateOf("") }

            val result = viewModel.weatherResult.observeAsState()

            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(12.dp))
                //textfield
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search for a city or area") },
                    trailingIcon = {
                        IconButton(onClick = { viewModel.getData(city) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        }

                    },
                    shape = RoundedCornerShape(32.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        focusedTrailingIconColor = Color.White
                    )
                )

                when (val result = result.value) {
                    is NetworkResponse.Error -> {
                        Text(text = result.message)
                    }

                    NetworkResponse.Loading -> {
                        CircularProgressIndicator()
                    }

                    is NetworkResponse.Success -> {
                        WeatherCard(data = result.data)
                    }

                    null -> {}
                }
            }
        }
    }
}
    @Composable
    fun WeatherCard(data: WeatherModel) {
        //Temperature
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = "https:/${data.current.condition.icon}",
                contentDescription = "",
                modifier = Modifier
                    .size(130.dp)
                    .align(CenterHorizontally)
            )
            Text(
                text = "${data.current.temp_c}°",
                Modifier
                    .align(CenterHorizontally),
                fontSize = 80.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = "Feels like ${data.current.feelslike_c}°",
                Modifier
                    .align(CenterHorizontally),
                color = Color.White
            )
            //Location
            Text(
                text = "${data.location.name}, ${data.location.region}, ${data.location.country}",
                Modifier
                    .align(CenterHorizontally),
                fontSize = 22.sp,
                color = Color.White
            )
            Text(
                text = "(Latt:${data.location.lat}° , Long:${data.location.lon}°)",
                Modifier
                    .align(CenterHorizontally),
                color = Color.White
            )
            Text(
                text = data.current.condition.text,
                Modifier
                    .align(CenterHorizontally),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            //FeaturesCard
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FeatureCard(name = "Humidity", unit = "${data.current.humidity}%", icon = painterResource(id = R.drawable.h))
                    FeatureCard(name = "Pressure", unit = "${data.current.pressure_mb} mb", icon = painterResource(R.drawable.pressure))
                    FeatureCard(name = "Heat Index", unit = "${data.current.heatindex_c}°", icon = painterResource(R.drawable.heatindex))
                    FeatureCard(name = "Dew", unit = "${data.current.dewpoint_c}°", icon = painterResource(R.drawable.dew))
                }
                    Row(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround){
                        FeatureCard(name = "Visibility", unit = "${data.current.vis_km} km", icon = painterResource(R.drawable.vis))
                        FeatureCard(name = "UV", unit = "${data.current.uv}", icon = painterResource(R.drawable.uv))
                        FeatureCard(name = "Cloud", unit = "${data.current.cloud}%", icon = painterResource(R.drawable.cloud))
                        FeatureCard(name = "PPT", unit = "${data.current.precip_mm} mm", icon = painterResource(R.drawable.ppt))
                    }
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    FeatureCard(name = "Wind", unit = "${data.current.wind_kph} kph", icon = painterResource(R.drawable.wind))
                    FeatureCard(name = "Wind Temp", unit = "${data.current.wind_degree}°", icon = painterResource(R.drawable.windtemp))
                    FeatureCard(name = "Wind Dir", unit = data.current.wind_dir, icon = painterResource(R.drawable.winddir))
                    FeatureCard(name = "Gust", unit = "${data.current.gust_kph} kph", icon = painterResource(R.drawable.gust))
                }
               
                }
            }
        }

@Composable
fun FeatureCard(name: String, unit : String, icon: Painter){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ){
            Card(modifier = Modifier.size(50.dp),
                shape = CircleShape,
                border = BorderStroke(2.dp, color = Color.White),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ){
                Icon(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(text = name)
            Text(text = unit, fontSize = 14.sp, color = Color.White)
        }
}
@Composable
fun CardUI(suntime: String, time: String){
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(horizontal = 12.dp)){
        Column(modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center
        ){
            Card(modifier = Modifier,
                shape = CircleShape,
                border = BorderStroke(2.dp, color = Color.White),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ){
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "",
                    modifier = Modifier.padding(12.dp),
                )
            }
            Text(text = suntime)
            Text(text = time, fontSize = 20.sp)
        }
    }

}