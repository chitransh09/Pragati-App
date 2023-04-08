import android.content.Context
import com.myprojects.pragati.utils.Constants.EMAIL_KEY
import com.myprojects.pragati.utils.Constants.NAME_KEY
import com.myprojects.pragati.utils.Constants.PREFS_TOKEN_FILE
import com.myprojects.pragati.utils.Constants.USER_TOKEN

class SharedPrefManager(context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    fun saveName(name: String) {
        editor.putString(NAME_KEY, name).apply()
    }

    fun getName(): String? {
        return prefs.getString(NAME_KEY, null)
    }

    fun saveEmail(email: String) {
        editor.putString(EMAIL_KEY, email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString(EMAIL_KEY, null)
    }

}
