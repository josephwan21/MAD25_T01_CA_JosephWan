package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [User::class, Score::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun scoreDao(): ScoreDao
}
