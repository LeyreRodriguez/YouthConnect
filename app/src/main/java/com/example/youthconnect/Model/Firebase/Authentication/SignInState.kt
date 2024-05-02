import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.Model.Sealed.AuthError

sealed class SignInState {
    object Loading : SignInState()
    data class Success(val data: UserData) : SignInState()
    data class Error(val error: AuthError) : SignInState()
}

