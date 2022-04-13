package com.infinitum.smsbot.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.models.Senders

@Dao
interface AppRoomDao {
    @Query("SELECT * from messages_table")
    fun getAllMessages():LiveData<List<Messages>>

    @Query("SELECT * from telegram_channels_table")
    fun getAllChannels():LiveData<List<Channels>>

    @Query("SELECT * from senders_table")
    fun getAllSenders():LiveData<List<Senders>>



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessage(doc: Messages)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChannel(doc: Channels)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSender(doc: Senders)

    @Delete
    suspend fun removeMessage(doc: Messages)
    @Delete
    suspend fun removeChannel(doc: Channels)
    @Delete
    suspend fun removeSender(doc: Senders)

}