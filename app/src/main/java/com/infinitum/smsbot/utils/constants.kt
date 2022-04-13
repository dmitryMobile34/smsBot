package com.infinitum.smsbot.utils

import android.widget.ImageButton
import com.infinitum.smsbot.MainActivity
import com.infinitum.smsbot.database.DatabaseRepository

lateinit var APP_ACTIVITY: MainActivity
lateinit var REPOSITORY: DatabaseRepository
lateinit var LAST_CHAT_ID: String
lateinit var IMGBTN: ImageButton
var SMS_TOKEN: String = "FFFFFFFFFFFFFF"
const val SLACK_TOKEN = "xoxb-482323829088-2736504813875-y5pZ3VR9jLA8Ao3xyqL3dDKE"
const val TYPE_DATABASE = "type_database"
const val TYPE_ROOM = "type_room"
