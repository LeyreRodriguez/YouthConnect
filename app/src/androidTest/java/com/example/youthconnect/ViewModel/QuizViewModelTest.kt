import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.ViewModel.QuizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class QuizViewModelTest {


    companion object {
        const val optionA = "optionA"
        const val optionB = "optionB"

        const val optionC = "optionC"

        const val optionD = "optionD"

    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var firestoreRepository: FirestoreRepository

    @Mock
    private lateinit var storageRepository: FirebaseStorageRepository

    private lateinit var viewModel: QuizViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = QuizViewModel(firestoreRepository, storageRepository)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getAllQuestionsTest() = runBlocking {
        val mockQuestions = listOf<Question>(
            Question("1", "A", optionA, optionB, optionC, optionD, "Question"),
            Question("2", "B",optionA, optionB, optionC,optionD , "Question")
        )

        // Launching a coroutine
            // Inside the coroutine scope, call the suspend function getQuestions
            Mockito.`when`(firestoreRepository.getQuestions()).thenReturn(mockQuestions)

            // Call the function under test
            viewModel.getAllQuestions()

            // Now, you can assert the result directly since it's executed inside the coroutine
            Assert.assertEquals(mockQuestions, viewModel.getAllQuestions())

    }


    @Test
    fun testAddNewQuestion() = runBlocking {
        val question = Question("1", "A", "Option A", "Option B", "Option C", "Option D", "Question")

        // Mocking repository method
        Mockito.doNothing().`when`(firestoreRepository).addNewQuestion(question)

        // Call the function
        viewModel.addNewQuestion(question)

        // Verify that the repository method was called
        Mockito.verify(firestoreRepository).addNewQuestion(question)
    }

    @Test
    fun testUpdateQuestion() = runBlocking {
        val question = Question("1", "A", "Option A", "Option B", "Option C", "Option D", "Question")

        // Stubbing repository method
        Mockito.doNothing().`when`(firestoreRepository).updateQuestion(question)

        // Call the function
        viewModel.updateQuestion(question)

        // Verify that the repository method was called
        Mockito.verify(firestoreRepository).updateQuestion(question)
    }

    @Test
    fun testDeleteQuestion() = runBlocking {
        val questionId = "1"

        // Mocking repository method to do nothing
        Mockito.doNothing().`when`(firestoreRepository).deleteQuestion(questionId)

        // Call the function
        viewModel.deleteQuestion(questionId)

        // Verify that the repository method was called
        Mockito.verify(firestoreRepository).deleteQuestion(questionId)
    }






}
