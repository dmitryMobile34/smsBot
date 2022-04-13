package com.infinitum.smsbot.screens.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Senders
import com.infinitum.smsbot.utils.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddSenderFragmentViewModel(application: Application): AndroidViewModel(application) {

    fun insertSender(sender: Senders, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.insertSender(sender) {
                onSuccess()
            }
        }
}