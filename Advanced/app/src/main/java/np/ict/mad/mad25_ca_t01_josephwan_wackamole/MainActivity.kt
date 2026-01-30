package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import np.ict.mad.mad25_ca_t01_josephwan_wackamole.ui.theme.MAD25_CA_T01_JosephWan_WackAMoleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD25_CA_T01_JosephWan_WackAMoleTheme {
                WackAMoleApp()
            }
        }
    }
}

@Composable
fun WackAMoleApp() {
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "wackamole_db"
    ).allowMainThreadQueries().build()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "signin") {
        composable("game") { GameScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}