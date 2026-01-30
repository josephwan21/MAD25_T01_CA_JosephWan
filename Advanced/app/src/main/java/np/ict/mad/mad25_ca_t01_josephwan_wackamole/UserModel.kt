package np.ict.mad.mad25_ca_t01_josephwan_wackamole

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserModel : ViewModel() {
    var currentUser by mutableStateOf<User?>(null)
        private set

    fun setUser(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}