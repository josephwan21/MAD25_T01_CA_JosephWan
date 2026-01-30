package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE username = :username LIMIT 1")
    fun getUserByCredentials(username: String, password: String): User?
}
