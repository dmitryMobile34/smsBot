package com.infinitum.smsbot.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.models.Senders

@Database(entities = [Messages::class, Channels::class, Senders::class], version = 8)
abstract class AppRoomDatabase:RoomDatabase() {

    abstract fun getAppRoomDao(): AppRoomDao

    companion object{
        @Volatile
        private var database: AppRoomDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppRoomDatabase {
            if(database == null){
                database = Room.databaseBuilder(
                    context,
                    AppRoomDatabase::class.java,
                    "database"
                ).fallbackToDestructiveMigration().build()
                return database as AppRoomDatabase
            } else return database as AppRoomDatabase
        }
    }

}