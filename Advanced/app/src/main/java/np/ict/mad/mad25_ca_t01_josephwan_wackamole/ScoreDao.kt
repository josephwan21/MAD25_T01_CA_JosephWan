package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
    fun getLeaderboard(): List<UserScore>
}

data class UserScore(
    val username: String,
    val score: Int
)