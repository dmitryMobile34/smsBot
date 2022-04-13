package com.infinitum.smsbot

import android.os.StrictMode
import android.util.Log
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import com.infinitum.smsbot.utils.LAST_CHAT_ID
import com.infinitum.smsbot.utils.SLACK_TOKEN
import com.slack.api.RequestConfigurator
import com.slack.api.Slack
import com.slack.api.methods.request.chat.ChatPostMessageRequest.ChatPostMessageRequestBuilder
import com.slack.api.methods.request.conversations.ConversationsListRequest
import com.slack.api.methods.response.chat.ChatPostMessageResponse
import com.slack.api.methods.response.conversations.ConversationsListResponse
import com.slack.api.model.ConversationType

class SlackBotHandler {
    constructor(){
        val SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        Log.d("devx", "Thread policy is set.")
    }
    var slack = Slack().methods()




//    fun send(channelID: String, text: String) {
//        val response =
//            slack.methods(SLACK_TOKEN).chatPostMessage { req: ChatPostMessageRequestBuilder ->
//                req
//                    .channel(channelID) // Channel ID
//                    .text(text)
//            }
//    }

    fun getChannels():ConversationsListResponse {
        return slack.conversationsList { r ->
            r
                .token(SLACK_TOKEN)
                .types(listOf(ConversationType.PRIVATE_CHANNEL))

        }
    }

    fun sendMessage(text: String, channel: String){
        slack.chatPostMessage { req: ChatPostMessageRequestBuilder ->
                req
                    .token(SLACK_TOKEN)
                    .channel(channel) // Channel ID
                    .text(text)

            }
    }
}