package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val username: String,
    val password: String
)

@Entity
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val score: Int,
    val timestamp: Long
)

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    fun getUserByUsername(username: String): User?
}

@Dao
interface ScoreDao {
    @Insert
    fun insertScore(score: Score)

    @Query("SELECT * FROM Score WHERE userId = :userId ORDER BY score DESC LIMIT 1")
    fun getBestScoreForUser(userId: Int): Score?

    @Query("""
        SELECT u.username, MAX(s.score) as score
        FROM Score s
        INNER JOIN User u ON s.userId = u.userId
        GROUP BY u.username
        ORDER BY score DESC
    """)
    fun getAllUsersBestScores(): List<UserScore>
}

data class UserScore(
    val username: String,
    val score: Int
)

@Database(entities = [User::class, Score::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scoreDao(): ScoreDao
}
