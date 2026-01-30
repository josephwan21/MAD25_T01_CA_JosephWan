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

### 3.1 Mole Movement Logic

With the intention to get the mole to immediately move to a different position upon getting clicked, the mole was sometimes jumping twice or appearing to skip positions due to overlapping timers and state changes.

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
- Clicking the mole immediately moves the mole to a new position and resets the delay timer cleanly.
- Prevents overlapping coroutine execution.

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

## 4. Room Scheme Error Resolution

Given the small scope of this CA, destructive migration was used during development.

### App crashed with:
```Room cannot verify the data integrity. Looks like you've changed schema but forgot to update the version number.
```

### Resolution
```kotlin
.fallbackToDestructiveMigration(dropAllTables = true)
```

This ensured stability while iterating on the schema.

---

## 5. Key Takeaways
- Jetpack Compose requires careful handling of side effects (LaunchedEffect) to avoid unintended recompositions.
- Room databases must be versioned consistently; schema changes require explicit handling.
- Separating UI state from game logic improves predictability.
