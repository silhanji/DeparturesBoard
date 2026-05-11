package dev.kluci_jak_buci.departuresboard.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
//import androidx.glance.appwidget.lazy.item
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class DepartureWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent()
            }
        }
    }

    @Composable
    private fun WidgetContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .background(GlanceTheme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.Top
        ) {
            // --- HEADER (Stays fixed at the top) ---
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    text = "Hlavní nádraží",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = GlanceTheme.colors.onSurface
                    ),
                    modifier = GlanceModifier.defaultWeight()
                )
                Text(
                    text = "⟳",
                    style = TextStyle(
                        color = GlanceTheme.colors.primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Divider()
            Spacer(GlanceModifier.height(8.dp))

            // --- SCROLLABLE LIST (Takes up the rest of the space) ---
            LazyColumn(modifier = GlanceModifier.defaultWeight()) {
                item {
                    Column {
                        DepartureRow(
                            line = "9",
                            destination = "Sídliště Řepy",
                            scheduledTime = "2 min",
                            actualTime = "3 min",
                            isDelayed = true,
                            vehicleType = "15T",
                            isTram = true
                        )
                        Divider()
                    }
                }

                item {
                    Column {
                        DepartureRow(
                            line = "26",
                            destination = "Divoká Šárka",
                            scheduledTime = "5 min",
                            actualTime = "5 min",
                            isDelayed = false,
                            vehicleType = "T3R.P",
                            isTram = true
                        )
                        Divider()
                    }
                }

                item {
                    Column {
                        DepartureRow(
                            line = "5",
                            destination = "Slivenec",
                            scheduledTime = "12 min",
                            actualTime = "16 min",
                            isDelayed = true,
                            vehicleType = "15T",
                            isTram = true
                        )
                        Divider()
                    }
                }

                item {
                    Column {
                        DepartureRow(
                            line = "100",
                            destination = "Letiště",
                            scheduledTime = "15 min",
                            actualTime = "15 min",
                            isDelayed = false,
                            vehicleType = "SOR NB 18",
                            isTram = false
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun DepartureRow(
        line: String,
        destination: String,
        scheduledTime: String,
        actualTime: String,
        isDelayed: Boolean,
        vehicleType: String,
        isTram: Boolean
    ) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            // Rounded Line Number Pill
            Box(
                modifier = GlanceModifier
                    .size(width = 44.dp, height = 28.dp)
                    .cornerRadius(8.dp)
                    .background(
                        if (isTram) ColorProvider(Color(0xFFD32F2F))
                        else ColorProvider(Color(0xFF1976D2))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = line,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(GlanceModifier.width(12.dp))

            // Destination and Vehicle Info
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = destination,
                    maxLines = 1,
                    style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                )
                Spacer(GlanceModifier.height(2.dp))
                Text(
                    text = vehicleType,
                    style = TextStyle(
                        color = GlanceTheme.colors.outline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            // Actual Time and Scheduled Time Status
            Column(horizontalAlignment = Alignment.Horizontal.End) {
                // The ACTUAL time the user needs to care about
                Text(
                    text = actualTime,
                    style = TextStyle(
                        color = if (isDelayed) ColorProvider(Color(0xFFD32F2F)) else GlanceTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                Spacer(GlanceModifier.height(2.dp))

                // Secondary info: Crossed out original time, or "On time" confirmation
                if (isDelayed) {
                    Text(
                        text = scheduledTime,
                        style = TextStyle(
                            color = GlanceTheme.colors.outline,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                } else {
                    Text(
                        text = "On time",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF388E3C)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }

    // Helper function for a clean horizontal line
    @Composable
    private fun Divider() {
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorProvider(Color.Gray.copy(alpha = 0.2f)))
        ) {}
    }
}

class DepartureWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DepartureWidget()
}