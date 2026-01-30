package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController, db: AppDatabase, userModel: UserModel) {
    val currentUser = userModel.currentUser
    var leaderboard by remember { mutableStateOf(listOf<UserScore>()) }

    //Load leaderboard from Room
    LaunchedEffect(Unit) {
        leaderboard = db.scoreDao().getLeaderboard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Top Players", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(leaderboard) { entry ->
                        val isCurrentUser = entry.username == currentUser?.username
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = entry.username,
                                style = if (isCurrentUser) MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                                else MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = entry.score.toString(),
                                style = if (isCurrentUser) MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                                else MaterialTheme.typography.bodyLarge
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    )
}
