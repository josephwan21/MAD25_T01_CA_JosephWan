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

1. “Why does Room throw a schema mismatch error and how do I fix it?”
2. “How can I store and retrieve a user’s personal best score using Room?”
3. “Why does my mole jump twice when using multiple LaunchedEffect blocks?”
4. “How should foreign keys and indices be defined in Room entities?”

---

## 3. Code Influenced by LLM

### 3.1 Personal Best Score Storage

**Before (SharedPreferences):**
```kotlin
val prefs = context.getSharedPreferences("wackamole_prefs", Context.MODE_PRIVATE)
val highScore = prefs.getInt("high_score", 0)
```

**After (Room database):**
```kotlin
val bestScore = db.scoreDao().getBestScoreForUser(currentUser.userId)?.score ?: 0
```

**Reason:** Room supports persistent, per-user data and scales better than SharedPreferences.

---

### 3.2 Database Schema Improvements

- Added foreign key relationship between User and Score
- Added index on foreign key column
- Introduced timestamps for score tracking

These changes improved data integrity and eliminated Room warnings.

---

### 3.3 Game Logic Timing Fix

- Identified multiple LaunchedEffect blocks updating the same state
- Refactored logic to ensure controlled mole movement
- Prevented unintended double jumps

---

## 4. Database Migration Strategy

Given the small scope of this CA, destructive migration was used during development:

```kotlin
.fallbackToDestructiveMigration(dropAllTables = true)
```

This ensured stability while iterating on the schema.

---

## 5. Key Takeaways

- Better understanding of Jetpack Compose state and side-effects
- Practical experience with Room entities, DAOs, and migrations
- Improved debugging and error interpretation skills and consistency
