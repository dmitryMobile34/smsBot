package com.infinitum.smsbot.database.room

import androidx.lifecycle.LiveData
import com.infinitum.smsbot.database.DatabaseRepository
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.models.Senders

class AppRoomRepository(private val appRoomDao: AppRoomDao): DatabaseRepository {
    override val allMessages: LiveData<List<Messages>>
        get() = appRoomDao.getAllMessages()
    override val allChannels: LiveData<List<Channels>>
        get() = appRoomDao.getAllChannels()
    override val allSenders: LiveData<List<Senders>>
        get() = appRoomDao.getAllSenders()

    override suspend fun insertMessage(message: Messages, onSuccess: () -> Unit) {
        appRoomDao.insertMessage(message)
        onSuccess()
    }

    override suspend fun insertChannel(channel: Channels, onSuccess: () -> Unit) {
        appRoomDao.insertChannel(channel)
        onSuccess()
    }

    override suspend fun insertSender(sender: Senders, onSuccess: () -> Unit) {
        appRoomDao.insertSender(sender)
        onSuccess()
    }


    override suspend fun removeMessage(message: Messages, onSuccess: () -> Unit) {
        appRoomDao.removeMessage(message)
        onSuccess()    }

    override suspend fun removeChannel(channel: Channels, onSuccess: () -> Unit) {
        appRoomDao.insertChannel(channel)
        onSuccess()
    }

    override suspend fun removeSender(sender: Senders, onSuccess: () -> Unit) {
        appRoomDao.removeSender(sender)
        onSuccess()
    }


}