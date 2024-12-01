package com.domedav.namedays

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.domedav.namedays.ui.theme.NamedaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        renderContent()
        WorkerScheduler.scheduleDailyWork(applicationContext)
    }
    fun renderContent(){
        setContent {
            NamedaysTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        context = this
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, context: Context) {
    val scrollStateVertical = rememberScrollState()
    val scrollStateHorizontal = rememberScrollState()

    var dayOffsetLocal by remember { mutableIntStateOf(0) }

    var currentDate = java.time.LocalDate.now(java.time.ZoneId.systemDefault())
    if(dayOffsetLocal < 0){
        currentDate = currentDate.minusDays((-dayOffsetLocal).toLong())
    }
    else if(dayOffsetLocal > 0){
        currentDate = currentDate.plusDays(dayOffsetLocal.toLong())
    }

    val namedaysToday = NamedaysHelper.getStringResource(context, dayOffsetLocal)

    var namedaysHeader = context.getString(R.string.nameday_display_header)
    var shifterBackwards = context.getString(R.string.nameday_display_shifter_back0)
    var shifterForwards = context.getString(R.string.nameday_display_shifter_forward0)

    if(dayOffsetLocal == 1){
        namedaysHeader = context.getString(R.string.nameday_display_header_shift_forward1)
        shifterForwards = context.getString(R.string.nameday_display_shifter_forward1)
        shifterBackwards = context.getString(R.string.nameday_display_shifter_today)
    }
    else if(dayOffsetLocal == 2){
        namedaysHeader = context.getString(R.string.nameday_display_header_shift_forward2)
        shifterForwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
        shifterBackwards = context.getString(R.string.nameday_display_shifter_forward0)
    }
    else if(dayOffsetLocal == 3){
        namedaysHeader = context.getString(R.string.nameday_display_header_shift1, currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd.")))
        shifterForwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
        shifterBackwards = context.getString(R.string.nameday_display_shifter_forward1)
    }
    else if(dayOffsetLocal == -1) {
        namedaysHeader = context.getString(R.string.nameday_display_header_shift_back1)
        shifterForwards = context.getString(R.string.nameday_display_shifter_today)
        shifterBackwards = context.getString(R.string.nameday_display_shifter_back1)
    }
    else if(dayOffsetLocal == -2) {
        namedaysHeader = context.getString(R.string.nameday_display_header_shift_back2)
        shifterForwards = context.getString(R.string.nameday_display_shifter_back0)
        shifterBackwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
    }
    else if(dayOffsetLocal == -3) {
        namedaysHeader = context.getString(R.string.nameday_display_header_shift1, currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd.")))
        shifterForwards = context.getString(R.string.nameday_display_shifter_back1)
        shifterBackwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
    }
    else if (dayOffsetLocal != 0){
        namedaysHeader = context.getString(R.string.nameday_display_header_shift1, currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd.")))
        shifterForwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
        shifterBackwards = context.getString(R.string.nameday_display_shifter_universal, currentDate.minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("MM.dd.")))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollStateVertical)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = namedaysHeader,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(12.dp))
        Text(
            text = namedaysToday,
            color = MaterialTheme.colorScheme.primary,
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(36.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .horizontalScroll(scrollStateHorizontal),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = {
                    dayOffsetLocal -= 1
                },
            ){
                Image(
                    painter = painterResource(id = R.drawable.rounded_arrow_back),
                    contentDescription = shifterBackwards,
                    modifier = Modifier.size(24.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        MaterialTheme.colorScheme.onPrimary
                    )
                )
                Spacer(modifier = Modifier.padding(end = 8.dp, start = 0.dp))
                Text(
                    text = shifterBackwards,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = {
                    dayOffsetLocal = 0
                },
            ){
                Image(
                    painter = painterResource(id = R.drawable.rounded_cabin_24),
                    contentDescription = context.getString(R.string.nameday_display_shifter_today),
                    modifier = Modifier.size(18.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Button(
                onClick = {
                    dayOffsetLocal += 1
                },
            ){
                Text(
                    text = shifterForwards,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(start = 8.dp, end = 0.dp))
                Image(
                    painter = painterResource(id = R.drawable.rounded_arrow_forward),
                    contentDescription = shifterForwards,
                    modifier = Modifier.size(24.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                        MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=440,isRound=true", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun Preview() {
    NamedaysTheme {
        //MainContent()
    }
}