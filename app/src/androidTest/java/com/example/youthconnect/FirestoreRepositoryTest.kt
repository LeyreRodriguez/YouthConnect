package com.example.youthconnect

import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.runBlocking


class FirestoreRepositoryTest {
    val child1 = Child(
        FullName = "Juan Pérez",
        ID = "00000000A",
        Course = "4th Grade",
        Password = "password123",
        BelongsToSchool = true,
        FaithGroups = false,
        GoOutAlone = true,
        Observations = "Needs extra help with math",
        ParentID = listOf("parent1ID", "parent2ID"),
        InstructorID = "instructor1ID",
        State = true,
        Score = 85,
        RollCall = listOf("2024-04-14", "2024-04-15")
    )

    val child2 = Child(
        FullName = "María García",
        ID = "00000000B",
        Course = "6th Grade",
        Password = "securepassword456",
        BelongsToSchool = true,
        FaithGroups = true,
        GoOutAlone = false,
        Observations = "",
        ParentID = listOf("parent3ID"),
        InstructorID = "instructor2ID",
        State = true,
        Score = null,
        RollCall = null
    )

    val child3 = Child(
        FullName = "Carlos López",
        ID = "00000000C",
        Course = "2nd Grade",
        Password = "childpassword789",
        BelongsToSchool = true,
        FaithGroups = false,
        GoOutAlone = true,
        Observations = "Allergic to peanuts",
        ParentID = listOf("parent4ID", "parent5ID"),
        InstructorID = "instructor3ID",
        State = true,
        Score = 90,
        RollCall = listOf("2024-04-14", "2024-04-15")
    )

    val instructor1 = Instructor(
        FullName = "Ana Martínez",
        ID = "00000001A",
        Password = "instructorpassword123",
        Score = 85
    )

    val instructor2 = Instructor(
        FullName = "Pedro Rodríguez",
        ID = "00000001B",
        Password = "secureinstructorpassword456",
        Score = 90
    )

    val instructor3 = Instructor(
        FullName = "María López",
        ID = "00000001C",
        Password = "teacherpassword789"
    )

    val parent1 = Parent(
        FullName = "María Sánchez",
        ID = "00000002A",
        PhoneNumber = "123-456-7890",
        Password = "parentpassword123",
        Score = 85
    )

    val parent2 = Parent(
        FullName = "Juan Rodríguez",
        ID = "00000002B",
        PhoneNumber = "987-654-3210",
        Password = "secureparentpassword456",
        Score = 90
    )

    val parent3 = Parent(
        FullName = "Ana Martínez",
        ID = "00000002C",
        PhoneNumber = "555-123-4567",
        Password = "mompassword789"
    )

    private lateinit var repository: FirestoreRepository


    @Before
    fun setUp() {
        repository = object : FirestoreRepository {
            override val dataBase: FirebaseFirestore?
                get() = FirebaseFirestore.getInstance()
            override val storageDataBase: FirebaseStorage?
                get() = FirebaseStorage.getInstance()

            override suspend fun getCurrentUser(): String? {
                return child1.ID
            }

            override suspend fun getChild(childId: String): Child? {
                return when (childId) {
                    "00000000A" -> child1
                    "00000000B" -> child2
                    "00000000C" -> child3
                    else -> null
                }
            }

            override suspend fun getAllChildren(): List<Child?> {
                return listOf(child1, child2, child3)
            }

            override suspend fun getChildByInstructorId(instructorID: String): List<Child?> {
                return when (instructorID) {
                    "00000001A" -> listOf(child1, child3)
                    "00000001B" -> listOf(child2)
                    else -> emptyList()
                }
            }

            override suspend fun getChildByInstructorIdThatIsInSchool(instructorId: String): List<Child?> {
                TODO("Not yet implemented")
            }

            override suspend fun getCurrentChildById(childId: String): Child? {
                return when (childId) {
                    "00000000A" -> child1
                    "00000000B" -> child2
                    "00000000C" -> child3
                    else -> null
                }
            }

            override suspend fun getChildByParentsId(parentID: String): List<Child?> {
                return when (parentID) {
                    "00000002A" -> listOf(child1)
                    "00000002B" -> listOf(child1)
                    "00000002C" -> listOf(child2)
                    else -> emptyList()
                }
            }

            override suspend fun getAllNews(): List<News?> {
                TODO("Not yet implemented")
            }

            override suspend fun getNewsById(newsId: String): News? {
                TODO("Not yet implemented")
            }

            override suspend fun addNews(news: News) {
                TODO("Not yet implemented")
            }

            override suspend fun findDocument(userId: String): String? {
                TODO("Not yet implemented")
            }

            override suspend fun getCurrentUserById(parentID: String): Parent? {
                return when (parentID) {
                    "00000002A" -> parent1
                    "00000002B" -> parent2
                    "00000002C" -> parent3
                    else -> null
                }
            }

            override suspend fun getParentsByParentsID(parentsID: List<String>): List<Parent?> {
                return parentsID.map { getCurrentUserById(it) }
            }

            override suspend fun getCurrentInstructorById(instructorID: String): Instructor? {
                return when (instructorID) {
                    "00000001A" -> instructor1
                    "00000001B" -> instructor2
                    "00000001C" -> instructor3
                    else -> null
                }
            }

            override suspend fun getInstructorByChildId(childId: String): Instructor? {
                return when (childId) {
                    "00000000A", "00000000C" -> instructor1
                    "00000000B" -> instructor2
                    else -> null
                }
            }

            override suspend fun getAllInstructors(): List<Instructor?> {
                return listOf(instructor1, instructor2, instructor3)
            }

            override suspend fun addChild(child: Child) {
                TODO("Not yet implemented")
            }

            override suspend fun addParent(parent: Parent) {
                TODO("Not yet implemented")
            }

            override suspend fun addInstructor(instructor: Instructor) {
                TODO("Not yet implemented")
            }

            override suspend fun changeState(childId: String) {
                TODO("Not yet implemented")
            }

            override suspend fun addInstructorToChild(child: Child, instructorID: String) {
                TODO("Not yet implemented")
            }

            override suspend fun rollCall(child: Child) {
                TODO("Not yet implemented")
            }

            override suspend fun notRollCall(child: Child) {
                TODO("Not yet implemented")
            }

            override suspend fun getRollCall(childId: String): List<String>? {
                TODO("Not yet implemented")
            }

            override suspend fun removeInstructorFromChild(child: Child, instructorID: String) {
                TODO("Not yet implemented")
            }

            override suspend fun getUser(): UserData? {
                TODO("Not yet implemented")
            }

            override suspend fun getUserById(Id: String): UserData? {
                TODO("Not yet implemented")
            }

            override suspend fun getAllUser(): List<UserData?> {
                TODO("Not yet implemented")
            }

            override suspend fun getAllUser(userType: String): List<UserData> {
                TODO("Not yet implemented")
            }

            override suspend fun getChatId(chatId: String): String? {
                TODO("Not yet implemented")
            }

            override suspend fun getQuestions(): List<Question?> {
                TODO("Not yet implemented")
            }

            override fun addNewQuestion(question: Question) {
                TODO("Not yet implemented")
            }

            override fun updateScore(collection: String, documentId: String) {
                TODO("Not yet implemented")
            }

            override fun resetScore(collection: String, documentId: String) {
                TODO("Not yet implemented")
            }

            override suspend fun findUserType(id: String): String {
                TODO("Not yet implemented")
            }

            override fun getScore(coleccion: String, idDocumento: String): String {
                TODO("Not yet implemented")
            }

            override fun updateUser(user: Any) {
                TODO("Not yet implemented")
            }
        }


    }

    @Test
    fun testGetChild() {
        runBlocking {
            var child = repository.getChild("00000000A")
            assertNotNull(child)
            assertEquals("Juan Pérez", child?.FullName)
            assertEquals("00000000A", child?.ID)
            assertEquals("4th Grade", child?.Course)
            assertEquals("password123", child?.Password)
            assertTrue(child?.BelongsToSchool!!)
            assertFalse(child?.FaithGroups!!)
            assertTrue(child?.GoOutAlone!!)
            assertEquals("Needs extra help with math", child?.Observations)
            assertEquals(listOf("parent1ID", "parent2ID"), child?.ParentID)
            assertEquals("instructor1ID", child?.InstructorID)
            assertTrue(child?.State!!)
            assertEquals(85, child?.Score)
            assertEquals(listOf("2024-04-14", "2024-04-15"), child?.RollCall)

            child = repository.getChild("00000000B")
            assertNotNull(child)
            assertEquals("María García", child?.FullName)
            assertEquals("00000000B", child?.ID)
            assertEquals("6th Grade", child?.Course)
            assertEquals("securepassword456", child?.Password)
            assertTrue(child?.BelongsToSchool!!)
            assertTrue(child?.FaithGroups!!)
            assertFalse(child?.GoOutAlone!!)
            assertEquals("", child?.Observations)
            assertEquals(listOf("parent3ID"), child?.ParentID)
            assertEquals("instructor2ID", child?.InstructorID)
            assertTrue(child?.State!!)
            assertEquals(null, child?.Score)
            assertEquals(null, child?.RollCall)

            // Aquí agregamos un mensaje de depuración
            if (child?.Score == null) {
                println("El Score es nulo para el ID ${child?.ID}")
            }

            if (child?.RollCall == null) {
                println("RollCall es nulo para el ID ${child?.ID}")
            }
        }
    }

    @Test
    fun testGetInstructorById() {
        runBlocking {
            var instructor = repository.getCurrentInstructorById("00000001A")
            assertNotNull(instructor)
            assertEquals("Ana Martínez", instructor?.FullName)
            assertEquals("00000001A", instructor?.ID)
            assertEquals("instructorpassword123", instructor?.Password)
            assertEquals(85, instructor?.Score)

            instructor = repository.getCurrentInstructorById("00000001B")
            assertNotNull(instructor)
            assertEquals("Pedro Rodríguez", instructor?.FullName)
            assertEquals("00000001B", instructor?.ID)
            assertEquals("secureinstructorpassword456", instructor?.Password)
            assertEquals(90, instructor?.Score)
        }
    }

    @Test
    fun testGetParentById() {
        runBlocking {
            var parent = repository.getCurrentUserById("00000002A")
            assertNotNull(parent)
            assertEquals("María Sánchez", parent?.FullName)
            assertEquals("00000002A", parent?.ID)
            assertEquals("123-456-7890", parent?.PhoneNumber)
            assertEquals("parentpassword123", parent?.Password)
            assertEquals(85, parent?.Score)

            parent = repository.getCurrentUserById("00000002B")
            assertNotNull(parent)
            assertEquals("Juan Rodríguez", parent?.FullName)
            assertEquals("00000002B", parent?.ID)
            assertEquals("987-654-3210", parent?.PhoneNumber)
            assertEquals("secureparentpassword456", parent?.Password)
            assertEquals(90, parent?.Score)
        }
    }

}