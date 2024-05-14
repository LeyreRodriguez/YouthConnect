import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.ViewModel.UserViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var firestoreRepository: FirestoreRepository

    @Mock
    private lateinit var firebaseStorageRepository: FirebaseStorageRepository

    private lateinit var userViewModel: UserViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        userViewModel = UserViewModel(firestoreRepository, firebaseStorageRepository)
    }

    @Test
    fun getCurrentUserTest()  = runBlocking{
        val expectedDocument = "document"
        `when`(firestoreRepository.getCurrentUser()).thenReturn(expectedDocument)

         val result =  userViewModel.getCurrentUser()

        assertEquals(expectedDocument, result)
    }


    @Test
    fun getUserByIdTest() = runBlocking {
        val userId = "someUserId"
        val expectedUser = UserData(userId, "name")
        `when`(firestoreRepository.getUserById(userId)).thenReturn(expectedUser)

        val result = userViewModel.getUserById(userId)

        assertEquals(expectedUser, result)
    }


    @Test
    fun findDocumentTest() = runBlocking {
        // Arrange
        val userId = "someUserId"
        val expectedDocument = "someDocument"
        `when`(firestoreRepository.findDocument(userId)).thenReturn(expectedDocument)

        val result = userViewModel.findDocument(userId)

        assertEquals(expectedDocument, result)
    }

    @Test
    fun getUserTypeTest() = runBlocking {
        val userId = "someUserId"
        val expectedUserType = "someUserType"
        `when`(firestoreRepository.findUserType(userId)).thenReturn(expectedUserType)

        val result = userViewModel.getUserType(userId)

        assertEquals(expectedUserType, result)
    }


    @Test
    fun uploadProfileImageTest() = runBlocking {
        val imageUri = mock(Uri::class.java)
        val onSuccess: (String) -> Unit = mock()
        val onFailure: (Exception) -> Unit = mock()

        userViewModel.uploadProfileImage(imageUri, onSuccess, onFailure)

        verify(firebaseStorageRepository).uploadImageToFirebase(imageUri, onSuccess, onFailure)
    }


}

