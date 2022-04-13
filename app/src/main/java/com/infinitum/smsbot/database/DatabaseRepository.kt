package com.infinitum.smsbot.database

import androidx.lifecycle.LiveData
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.models.Senders

interface DatabaseRepository {
    val allMessages: LiveData<List<Messages>>
    val allChannels: LiveData<List<Channels>>
    val allSenders: LiveData<List<Senders>>
    suspend fun insertMessage(message: Messages, onSuccess:() -> Unit)
    suspend fun insertChannel(channel: Channels, onSuccess:() -> Unit)
    suspend fun insertSender(sender: Senders, onSuccess:() -> Unit)
    suspend fun removeMessage(message: Messages, onSuccess:() -> Unit)
    suspend fun removeChannel(channel: Channels, onSuccess:() -> Unit)
    suspend fun removeSender(sender: Senders, onSuccess:() -> Unit)

}