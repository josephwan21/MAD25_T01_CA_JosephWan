package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, db: AppDatabase, userModel: UserModel) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Sign In / Sign Up") })
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(onClick = {
                        // SIGN UP
                        if (username.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Enter username and password", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val existingUser = db.userDao().getUserByCredentials(username, password)
                        if (existingUser != null) {
                            Toast.makeText(context, "Username already exists", Toast.LENGTH_SHORT).show()
                        } else {
                            db.userDao().insertUser(User(username = username, password = password))
                            Toast.makeText(context, "User created! Please Sign In", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text("Sign Up")
                    }

                    Button(onClick = {
                        // SIGN IN
                        val user = db.userDao().getUserByCredentials(username, password)
                        if (user == null || user.password != password) {
                            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        } else {
                            // Navigate to GameScreen with current user
                            userModel.setUser(user)
                            navController.navigate("game") {
                                popUpTo("signin") { inclusive = true }
                            }
                        }
                    }) {
                        Text("Sign In")
                    }
                }
            }
        }
    )
}
