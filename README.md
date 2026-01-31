# MAD25_T01_CA_JosephWan
This is a development done to satisfy the assignment portion for module “Mobile App development” under Ngee Ann Polytechnic for AY24/25


# Use of Large Language Models (LLMs)

## 1. Declaration of LLM Use
Large Language Models (LLMs) in this CA were used **as a support and learning tool**, not as a substitute for independent development.

**LLM tools used:**
- ChatGPT (OpenAI)

The LLM was used for:
- Clarifying Android, Kotlin, Jetpack Compose, and Room concepts
- Understanding compiler/runtime error messages
- Exploring various UI logic and database design
- Suggesting alternative implementations for small code fragments, which were then adapted and understood
  
---

## 2. Example Prompts Used

1. "Why does my Room database crash with `AppDatabase_Impl does not exist` even though my entities and DAOs compile?"
2. “Why does my mole jump twice when using multiple LaunchedEffect blocks?”
3. “How should foreign keys and indices be defined in Room entities?”

---

## 3. Code Influenced by LLM

### 3.1.1 Mole Movement Logic

With the intention to get the mole to immediately move to a different position upon clicking it, the mole was sometimes jumping twice or appearing to skip positions due to overlapping timers and state changes.

**Before (Initial Attempt):**
```kotlin
LaunchedEffect(gameRunning) {
        while (gameRunning) {
            delay(Random.nextLong(700, 1000))
            currentMoleIndex = Random.nextInt(0, 9)
        }
```

**After (Refactored with LLM Guidance):**
```kotlin
LaunchedEffect(gameRunning, moleTrigger) {
       if (!gameRunning) {
           return@LaunchedEffect
       }
        while (gameRunning) {
            delay(Random.nextLong(700, 1000))
            currentMoleIndex = getNewMoleIndex(currentMoleIndex)
        }

    }
```

**Why it was changed:** 
- Clicking the mole immediately moves the mole to a new position
- Delay timer resets cleanly on each interaction
- Prevents overlapping coroutine execution


### 3.1.2 User State Handling Across Screens (Navigation --> Shared User View Model)
Initially, user information was passed between screens using navigation arguments. While functional, this approach became cumbersome and error-prone as more screens required access to information of the current user.

**Before (Passing User via Navigation Arguments)**
```kotlin
navController.navigate(
    "game?userId=${user.userId}&username=${user.username}"
)
```

And in the destination:
```kotlin
val userId = backStackEntry.arguments?.getInt("userId")
val username = backStackEntry.arguments?.getString("username")
```

**Issues Identified**
- Tight coupling between navigation routes and user data
- Increased risk of null or mismatched arguments
- Poor scalability as more user-related data was added.

**After (Shared User View Model State)**
```kotlin
class UserModel : ViewModel() {
    var currentUser by mutableStateOf<User?>(null)
        private set
```

```kotlin
fun WackAMoleApp() {
    val db = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "wackamole_db"
    ).allowMainThreadQueries().fallbackToDestructiveMigration(dropAllTables = true).build()

    val navController = rememberNavController()
    val userModel: UserModel = viewModel()

    NavHost(navController = navController, startDestination = "signin") {
        composable("signin") { SignInScreen(navController, db, userModel) }
        composable("game") { GameScreen(navController, db, userModel) }
        composable("settings") { SettingsScreen(navController, userModel) }
        composable("leaderboard") { LeaderboardScreen(navController, db, userModel) }
    }
}
```

```kotlin
val currentUser = userModel.currentUser ?: return
```

**Why it was changed:**
- Centralises authenticated user state
- Simplifies navigation routes
- Reduces boilerplate and runtime errors
- Better aligns with Compose's state-driven design
  
---

### 3.2 Database Schema Improvements

- Added foreign key relationship between ``User`` and ``Score`` entities
- Added index on foreign key column in the Score entity
- Introduced timestamps for score tracking

These changes improved data integrity and eliminated Room warnings.

---

### 3.3 Game Logic Timing Fix
- Identified multiple ``LaunchedEffect`` blocks updating the same state
- Refactored logic to ensure controlled and intended mole movement
- Prevented unintended double jumps during gameplay

---

## 4. Room Schema Error Resolution

### During development, the following runtime error was encountered:
``Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number.
``

Given the scope of this CA, Destructive Migration was used during development.

### Resolution
```kotlin
.fallbackToDestructiveMigration(dropAllTables = true)
```

This approach allowed rapid iteration while ensuring application stability. No production data was required to be preserved.

---

## 5. Key Takeaways
- Jetpack Compose requires careful handling of side effects (LaunchedEffect) to avoid unintended recompositions.
- Room databases must be versioned consistently; schema changes require explicit handling.
- Separating UI state from game logic improves predictability.
- LLMs are most effective when used as a helping tool to support understanding rather than replace problem-solving, which led to effective refinements in snippets of code in application development.
