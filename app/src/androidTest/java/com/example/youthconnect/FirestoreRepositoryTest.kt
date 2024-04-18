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
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class FirestoreRepositoryTest {

    val date1 = "2024-04-14"
    val date2 = "2024-04-15"
    val ana = "Ana Martínez"

    val notImplemented = "Not yet implemented"
    val child1 = Child(
        fullName = "Juan Pérez",
        id = "00000000A",
        course = "4th Grade",
        password = "password123",
        belongsToSchool = true,
        faithGroups = false,
        goOutAlone = true,
        observations = "Needs extra help with math",
        parentId = listOf("parent1ID", "parent2ID"),
        instructorId = "instructor1ID",
        state = true,
        score = 85,
        rollCall = listOf(date1 , date2)
    )

    val child2 = Child(
        fullName = "María García",
        id = "00000000B",
        course = "6th Grade",
        password = "securepassword456",
        belongsToSchool = true,
        faithGroups = true,
        goOutAlone = false,
        observations = "",
        parentId = listOf("parent3ID"),
        instructorId = "instructor2ID",
        state = true,
        score = null,
        rollCall = null
    )

    val child3 = Child(
        fullName = "Carlos López",
        id = "00000000C",
        course = "2nd Grade",
        password = "childpassword789",
        belongsToSchool = true,
        faithGroups = false,
        goOutAlone = true,
        observations = "Allergic to peanuts",
        parentId = listOf("parent4ID", "parent5ID"),
        instructorId = "instructor3ID",
        state = true,
        score = 90,
        rollCall = listOf(date1, date2 )
    )

    val instructor1 = Instructor(
        fullName = ana,
        id = "00000001A",
        password = "instructorpassword123",
        score = 85
    )

    val instructor2 = Instructor(
        fullName = "Pedro Rodríguez",
        id = "00000001B",
        password = "secureinstructorpassword456",
        score = 90
    )

    val instructor3 = Instructor(
        fullName = "María López",
        id = "00000001C",
        password = "teacherpassword789"
    )

    val parent1 = Parent(
        fullName = "María Sánchez",
        id = "00000002A",
        phoneNumber = "123-456-7890",
        password = "parentpassword123",
        score = 85
    )

    val parent2 = Parent(
        fullName = "Juan Rodríguez",
        id = "00000002B",
        phoneNumber = "987-654-3210",
        password = "secureparentpassword456",
        score = 90
    )

    val parent3 = Parent(
        fullName = ana,
        id = "00000002C",
        phoneNumber = "555-123-4567",
        password = "mompassword789"
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
                return child1.id
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
                TODO(notImplemented)
            }

            override suspend fun addParent(parent: Parent) {
                TODO(notImplemented)
            }

            override suspend fun addInstructor(instructor: Instructor) {
                TODO(notImplemented)
            }

            override suspend fun changeState(childId: String) {
                TODO(notImplemented)
            }

            override suspend fun addInstructorToChild(child: Child, instructorID: String) {
                TODO(notImplemented)
            }

            override suspend fun rollCall(child: Child) {
                TODO(notImplemented)
            }

            override suspend fun notRollCall(child: Child) {
                TODO(notImplemented)
            }

            override suspend fun getRollCall(childId: String): List<String>? {
                TODO(notImplemented)
            }

            override suspend fun removeInstructorFromChild(child: Child, instructorID: String) {
                TODO(notImplemented)
            }

            override suspend fun getUser(): UserData? {
                TODO(notImplemented)
            }

            override suspend fun getUserById(id: String): UserData? {
                TODO(notImplemented)
            }

            override suspend fun getAllUser(): List<UserData?> {
                TODO(notImplemented)
            }

            override suspend fun getAllUser(userType: String): List<UserData> {
                TODO(notImplemented)
            }

            override suspend fun getChatId(chatId: String): String? {
                TODO(notImplemented)
            }

            override suspend fun getQuestions(): List<Question?> {
                TODO(notImplemented)
            }

            override fun addNewQuestion(question: Question) {
                TODO(notImplemented)
            }

            override fun updateScore(collection: String, documentId: String) {
                TODO(notImplemented)
            }

            override fun resetScore(collection: String, documentId: String) {
                TODO(notImplemented)
            }

            override suspend fun findUserType(id: String): String {
                TODO(notImplemented)
            }

            override fun getScore(coleccion: String, idDocumento: String): String {
                TODO(notImplemented)
            }

            override fun updateUser(user: Any) {
                TODO(notImplemented)
            }
        }


    }

    @Test
    fun testGetChild() {
        runBlocking {
            var child = repository.getChild("00000000A")
            assertNotNull(child)
            assertEquals("Juan Pérez", child?.fullName)
            assertEquals("00000000A", child?.id)
            assertEquals("4th Grade", child?.course)
            assertEquals("password123", child?.password)
            assertTrue(child?.belongsToSchool!!)
            assertFalse(child?.faithGroups!!)
            assertTrue(child?.goOutAlone!!)
            assertEquals("Needs extra help with math", child?.observations)
            assertEquals(listOf("parent1ID", "parent2ID"), child?.parentId)
            assertEquals("instructor1ID", child?.instructorId)
            assertTrue(child?.state!!)
            assertEquals(85, child?.score)
            assertEquals(listOf(date1, date2), child?.rollCall)

            child = repository.getChild("00000000B")
            assertNotNull(child)
            assertEquals("María García", child?.fullName)
            assertEquals("00000000B", child?.id)
            assertEquals("6th Grade", child?.course)
            assertEquals("securepassword456", child?.password)
            assertTrue(child?.belongsToSchool!!)
            assertTrue(child?.faithGroups!!)
            assertFalse(child?.goOutAlone!!)
            assertEquals("", child?.observations)
            assertEquals(listOf("parent3ID"), child?.parentId)
            assertEquals("instructor2ID", child?.instructorId)
            assertTrue(child?.state!!)
            assertEquals(null, child?.score)
            assertEquals(null, child?.rollCall)

            // Aquí agregamos un mensaje de depuración
            if (child?.score == null) {
                println("El Score es nulo para el ID ${child?.id}")
            }

            if (child?.rollCall == null) {
                println("RollCall es nulo para el ID ${child?.id}")
            }
        }
    }

    @Test
    fun testGetInstructorById() {
        runBlocking {
            var instructor = repository.getCurrentInstructorById("00000001A")
            assertNotNull(instructor)
            assertEquals(ana, instructor?.fullName)
            assertEquals("00000001A", instructor?.id)
            assertEquals("instructorpassword123", instructor?.password)
            assertEquals(85, instructor?.score)

            instructor = repository.getCurrentInstructorById("00000001B")
            assertNotNull(instructor)
            assertEquals("Pedro Rodríguez", instructor?.fullName)
            assertEquals("00000001B", instructor?.id)
            assertEquals("secureinstructorpassword456", instructor?.password)
            assertEquals(90, instructor?.score)
        }
    }

    @Test
    fun testGetParentById() {
        runBlocking {
            var parent = repository.getCurrentUserById("00000002A")
            assertNotNull(parent)
            assertEquals("María Sánchez", parent?.fullName)
            assertEquals("00000002A", parent?.id)
            assertEquals("123-456-7890", parent?.phoneNumber)
            assertEquals("parentpassword123", parent?.password)
            assertEquals(85, parent?.score)

            parent = repository.getCurrentUserById("00000002B")
            assertNotNull(parent)
            assertEquals("Juan Rodríguez", parent?.fullName)
            assertEquals("00000002B", parent?.id)
            assertEquals("987-654-3210", parent?.phoneNumber)
            assertEquals("secureparentpassword456", parent?.password)
            assertEquals(90, parent?.score)
        }
    }

}