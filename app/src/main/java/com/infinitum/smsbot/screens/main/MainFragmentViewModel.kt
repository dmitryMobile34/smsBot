package com.infinitum.smsbot.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.infinitum.smsbot.database.room.AppRoomDatabase
import com.infinitum.smsbot.database.room.AppRoomRepository
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.utils.REPOSITORY
import com.infinitum.smsbot.utils.TYPE_ROOM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(application: Application): AndroidViewModel(application) {
    private val mContext = application

    fun initDatabase(type:String, onSuccess:() -> Unit){
        when(type){
            TYPE_ROOM->{
                val dao = AppRoomDatabase.getInstance(mContext).getAppRoomDao()
                REPOSITORY = AppRoomRepository(dao)
                onSuccess()
                initViewModel()
            }
        }
    }

    var locker: Boolean = false

    fun initViewModel(){
        val allChannels = REPOSITORY.allChannels
    }

    fun insertChannel(channel: Channels, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertChannel(channel) {
                onSuccess()
            }
        }


    fun removeDoc(message: Messages, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.removeMessage(message) {
                onSuccess()
            }
        }
    fun removeCat(channel: Channels, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.removeChannel(channel) {
                onSuccess()
            }
        }
}