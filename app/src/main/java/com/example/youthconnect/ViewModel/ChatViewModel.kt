package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val repo: FirebaseStorageRepository,
): ViewModel() {
    init {
        val recipientUserId =
        getMessages()
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    private val _conversations = mutableMapOf<String, MutableLiveData<List<Map<String, Any>>>>()


    /**
     * Generar un ID único para una conversación entre dos usuarios
     */
    private fun generateChatId(user1Id: String, user2Id: String): String {
        return if (user1Id < user2Id) {
            "$user1Id-$user2Id"
        } else {
            "$user2Id-$user1Id"
        }
    }
    /**
     * Update the message value as user types
     */


    fun updateMessage(message: String) {
        _message.value = message
    }

    /**
     * Send message
     */
    fun addMessage(recipientUserId: String) {
        val message: String = _message.value ?: throw IllegalArgumentException("message empty")
        if (message.isNotEmpty()) {
            val currentUserId = Firebase.auth.currentUser?.email?.substringBefore('@') ?: return

            val chatId = generateChatId(currentUserId.uppercase(), recipientUserId.uppercase())

            Firebase.firestore.collection(Constants.MESSAGES).document().set(
                hashMapOf(
                    Constants.MESSAGE to message,
                    Constants.SENT_BY to currentUserId.uppercase(),
                    Constants.SENT_ON to System.currentTimeMillis(),
                    "chatId" to chatId
                )
            ).addOnSuccessListener {
                _message.value = ""
            }
        }
    }

    /**
     * Get the messages
     */

    /**
     * Quiero que a esta funcion se le pase el valor del id del destinatario y con eso deberia de funcionar
     */
    fun getMessages() {
        val currentUserId = Firebase.auth.currentUser?.email?.substringBefore('@') ?: return

        //val chatId = generateChatId(currentUserId.uppercase(), recipientUserId.uppercase())
        Firebase.firestore.collection(Constants.MESSAGES)
            .whereEqualTo("chatId", chatId)
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[Constants.SENT_BY].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }

    /**
     * Update the list after getting the details from firestore
     */
    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }


    suspend fun getAllUsers() : List<UserData?>? {
        try {
            return firestoreRepository.getAllUser()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUser", e)
            return null
        }
    }
}