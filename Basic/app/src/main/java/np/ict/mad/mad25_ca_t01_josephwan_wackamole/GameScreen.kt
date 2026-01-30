package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavController) {
    var score by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(30) }
    var currentMoleIndex by remember { mutableStateOf(-1) }
    var gameRunning by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val prefs = context.getSharedPreferences("wackamole_prefs", Context.MODE_PRIVATE)
    var highScore by remember { mutableStateOf(prefs.getInt("high_score", 0)) }

    var showDialog by remember { mutableStateOf(false) }

    var moleTrigger by remember { mutableStateOf(0)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wack-a-Mole") },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Score and Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("Score: $score")
                    Text("Time: $timeLeft")
                    Text("High Score: $highScore")
                }

                Spacer(modifier = Modifier.height(16.dp))

                //3x3 Grid
                Column {
                    for (row in 0 until 3) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (col in 0 until 3) {
                                val index = row * 3 + col
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(8.dp)
                                        .background(
                                            color = if (index == currentMoleIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                            shape = androidx.compose.foundation.shape.CircleShape
                                        )
                                        .clickable {
                                            if (gameRunning && index == currentMoleIndex) {
                                                score++
                                                currentMoleIndex = getNewMoleIndex(currentMoleIndex)
                                                moleTrigger++
                                            }
                                        }
                                ) {
                                    if (index == currentMoleIndex) {
                                        Text("M", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                //Start/Restart button
                Button(
                    onClick = {
                        score = 0
                        timeLeft = 30
                        currentMoleIndex = -1
                        gameRunning = true
                        showDialog = false
                        currentMoleIndex = Random.nextInt(0, 9)
                        moleTrigger++
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (gameRunning) "Restart" else "Start")
                }
            }
        }
    )

    //Countdown timer
    LaunchedEffect(gameRunning) {
        while (gameRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (timeLeft == 0) {
                gameRunning = false
                showDialog = true
                if (score > highScore) {
                    highScore = score
                    prefs.edit().putInt("high_score", highScore).apply()
                }
            }
        }
    }

    // Mole movement
    LaunchedEffect(gameRunning, moleTrigger) {
       if (!gameRunning) {
           return@LaunchedEffect
       }
        while (gameRunning) {
            delay(Random.nextLong(700, 1000))
            currentMoleIndex = getNewMoleIndex(currentMoleIndex)
        }
    }

    // Game over dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Game Over") },
            text = { Text("Your score: $score\nHigh score: $highScore") },
            confirmButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

fun getNewMoleIndex(current: Int): Int {
    var newIndex = current
    while (newIndex == current) {
        newIndex = Random.nextInt(0, 9)
    }
    return newIndex
}