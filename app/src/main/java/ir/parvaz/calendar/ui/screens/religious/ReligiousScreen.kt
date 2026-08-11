package ir.parvaz.calendar.ui.screens.religious

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.parvaz.calendar.core.religious.ReligiousContent

private val BarBlue = Color(0xFF0E6BA8)

@Composable
fun ReligiousScreen(onBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedText by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BarBlue)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                when {
                    selectedText != null -> selectedText = null
                    selectedCategory != null -> selectedCategory = null
                    else -> onBack()
                }
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "بازگشت", tint = Color.White)
            }
            Text(
                text = when {
                    selectedText != null -> "متن"
                    selectedCategory != null -> selectedCategory!!
                    else -> "زیارات و دعاها"
                },
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.size(48.dp))
        }

        when {
            selectedText != null -> {
                val text = ReligiousContent.all.find { it.id == selectedText }
                if (text != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Text(
                                    text = text.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BarBlue
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = text.content,
                                    fontSize = 16.sp,
                                    lineHeight = 28.sp,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                    }
                }
            }
            selectedCategory != null -> {
                val texts = ReligiousContent.byCategory(selectedCategory!!)
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(texts) { text ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selectedText = text.id },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = text.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
