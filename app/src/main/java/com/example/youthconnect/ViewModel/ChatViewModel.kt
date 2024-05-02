package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository): ViewModel() {

    private var _recipientUserId = MutableLiveData<String>()
    val recipientUserId: LiveData<String> = _recipientUserId


    init {
        recipientUserId.observeForever { userId ->
            Log.e("recipientUserId", userId)
            val currentUserId = Firebase.auth.currentUser?.email?.substringBefore('@')?.uppercase()
                ?: ""
            val chatId = generateChatId(currentUserId, userId)
            loadMessages(chatId)
        }
    }

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages


    private var _isMessageSeen = MutableLiveData<Boolean>(false)
    val isMessageSeen: LiveData<Boolean> = _isMessageSeen


    /**
     * Generar un ID único para una conversación entre dos usuarios
     */
     fun generateChatId(user1Id: String, user2Id: String): String {
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
            val currentUserId = Firebase.auth.currentUser?.email?.substringBefore('@')?.uppercase() ?: ""
            val chatId = generateChatId(currentUserId, recipientUserId)

            // Establecer seen como false para el mensaje enviado
            val messageData = hashMapOf(
                Constants.MESSAGE to message,
                Constants.SENT_BY to currentUserId,
                Constants.SENT_ON to System.currentTimeMillis(),
                "chatId" to chatId,
                "seen" to false
            )

            Firebase.firestore.collection(Constants.MESSAGES).document().set(messageData)
                .addOnSuccessListener {
                    _message.value = ""
                }


        }
    }



    /**
     * Cargar mensajes para un chat específico
     */
    private fun loadMessages(chatId: String) {
        val currentUserId = Firebase.auth.currentUser?.email?.substringBefore('@')?.uppercase() ?: ""

        Firebase.firestore.collection(Constants.MESSAGES)
            .whereEqualTo("chatId", chatId)
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = mutableListOf<Map<String, Any>>()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        val sentBy = data[Constants.SENT_BY].toString()
                        val isCurrentUser = currentUserId == sentBy
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.email?.substringBefore('@')?.uppercase() ?: "" == sentBy

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }


    /**
     * Marcar un mensaje como visto
     */
    fun markMessagesAsSeen(chatId: String) {
        // Obtener una referencia a la colección de mensajes para la conversación específica
        val messagesCollection = Firebase.firestore.collection(Constants.MESSAGES)

        // Marcar todos los mensajes en la conversación como vistos
        messagesCollection.whereEqualTo("chatId", chatId).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Actualizar el estado "seen" del mensaje a true
                    messagesCollection.document(document.id).update("seen", true)
                }
            }
            .addOnFailureListener { e ->
                Log.e(Constants.TAG, "Error al marcar mensajes como vistos", e)
            }
    }




    /**
     * Update the list after getting the details from firestore
     */
    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }


    suspend fun getAllUsers(type : String) : List<UserData?>? {
        try {
            return firestoreRepository.getAllUser(type)
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUser", e)
            return null
        }
    }

    fun initRecipientUserId(userId: String) {
        _recipientUserId.value = userId
    }


    fun getUnseenMessages(): MutableLiveData<List<String>> {
        val unseenSentByLiveData = MutableLiveData<List<String>>()

        Firebase.firestore.collection(Constants.MESSAGES)
            .whereEqualTo("seen", false)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(Constants.TAG, "Error al obtener mensajes no vistos", error)
                    unseenSentByLiveData.value = emptyList()
                    return@addSnapshotListener
                }

                val unseenSentBy = mutableListOf<String>()
                value?.documents?.forEach { document ->
                    val messageData = document.data
                    if (messageData != null) {
                        val sentBy = messageData["sent_by"] as? String ?: ""
                        unseenSentBy.add(sentBy)
                    }
                }

                unseenSentByLiveData.value = unseenSentBy.distinct() // Para eliminar duplicados si es necesario
            }

        return unseenSentByLiveData
    }


}