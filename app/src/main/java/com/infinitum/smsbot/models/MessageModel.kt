package com.infinitum.smsbot.models

import androidx.room.*
import com.google.gson.Gson
import java.io.Serializable

@Entity(tableName = "telegram_channels_table")
data class Channels(
    @ColumnInfo val name: String = "",
    @PrimaryKey val id: String = ""
) : Serializable

@Entity(tableName = "messages_table") @TypeConverters(Converters::class)
data class Messages (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val sender: Int = 0,
    @ColumnInfo val text: String = ""
) : Serializable

@Entity(tableName = "senders_table") @TypeConverters(Converters::class)
data class Senders (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val tag: String = "",
    @ColumnInfo val number: String = "",
    @ColumnInfo val channelId: String = ""
) : Serializable

class Converters {
    @TypeConverter
    fun listToJson(value: List<String>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<String>::class.java).toList()
}