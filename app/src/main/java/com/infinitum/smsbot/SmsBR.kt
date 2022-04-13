package com.infinitum.smsbot

import android.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.example.projectd.utils.showToast
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import androidx.core.app.NotificationCompat
import android.graphics.Color
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import android.os.Build
import android.app.PendingIntent
import android.app.NotificationManager

import android.app.NotificationChannel











class SmsBR : BroadcastReceiver() {
    val serviceClass = SmsService::class.java
    lateinit var  intentService: Intent

    companion object {
        private const val TAG = "devx"
        const val PERMISSIONS_REQUEST_READ_SMS = 100
        const val PERMISSIONS_REQUEST_SERVICE = 100
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intentService = Intent(context,serviceClass)

        Log.e("devx","receiver start!!!")
        var body = ""
        val bundle = intent?.extras
        val pdusArr = bundle!!.get("pdus") as Array<Any>
        var messages: Array<SmsMessage?>  = arrayOfNulls(pdusArr.size)

        // if SMSis Long and contain more than 1 Message we'll read all of them
        for (i in pdusArr.indices) {
            messages[i] = SmsMessage.createFromPdu(pdusArr[i] as ByteArray)
        }
        var MobileNumber: String? = messages[0]?.originatingAddress
        Log.e(TAG, "MobileNumber =$MobileNumber")

        val bodyText = StringBuilder()
        for (i in messages.indices) {
            bodyText.append(messages[i]?.messageBody)
        }
        body = bodyText.toString()
        if (body.isNotEmpty()){
            // Do something, save SMS in DB or variable , static object or ....
            Log.i("devx" , "body =$body")
            Toast.makeText(context,"body =$body", Toast.LENGTH_SHORT).show()

            intentService.putExtra("message",body)
            intentService.putExtra("number",MobileNumber.toString())




            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context?.applicationContext?.startForegroundService(intentService)
            }
            else{
                context?.applicationContext?.startService(intentService)

            }
        } else{
            Log.e("Inside Receiver :" , "body =empty")
        }
    }

}

class SmsService : Service() {
    private var result: String = "null"
    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }
    var mNotifyManager: NotificationManager? = null
    var mBuilder: NotificationCompat.Builder? = null
    var notification: Notification? = null
    var notificationChannel: NotificationChannel? = null
    override fun onCreate() {
        super.onCreate()

    }
    val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        Log.e("devx","service starting")
        val number = intent.getStringExtra("number").toString()
        val message = intent.getStringExtra("message").toString()


        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Infinitum SmsBot Service")
            .setContentText("Service is running")
            .setSmallIcon(com.infinitum.smsbot.R.drawable.logo)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)


        runBlocking {
            sendSms(number,message)
        }
        //stopSelf()



        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }


    private suspend fun sendSms(number: String, message: String) {
        withContext(Dispatchers.IO){
            sendingToSlack(number, message)
            result = "sendingToSlack(number,message)"
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("devx", "service destroyed")
        Toast.makeText(applicationContext,"Service destroyed.",Toast.LENGTH_SHORT).show()
    }
    fun getEmoji(unicode: Int): String {
        return String(Character.toChars(unicode))
    }
    // Custom method to do a task
    private fun sendingToSlack(num: String, mes: String){
        //try {
            Log.d("devxz", "message received")

            var channel = Hawk.get<String>(num.lowercase())
            //Log.d("devxz", "first channel. res: $channel")

            val emoji = 0x1F4E9
            if (!channel.isNullOrEmpty() && channel != "null") {
                SlackBotHandler().sendMessage("${getEmoji(emoji)} SMS от: $num\n\n$mes", channel)
            }
            else{
                Log.d("devxz", "tuta")
                if(!mes.isNullOrEmpty() && mes != "null") {
                    Log.d("devxz", "tuta тож")

                    val ms = stringToWords(mes)
                    Log.d("devxz", "if false. ms: $ms | mes $mes")
                    if(!ms.isNullOrEmpty() && ms.size>0) {
                        for (i in 0 until ms.size) {
                            var ch: String? = Hawk.get(ms[i].lowercase())
                            Log.d("devxz", "i:: $i | hawk:: $ch")
                            if (!ch.isNullOrEmpty() && ch != "null") {
                                val emoji = 0x1F4E9
                                SlackBotHandler().sendMessage(
                                    "${getEmoji(emoji)} SMS от: $num\n\n$mes",
                                    ch!!
                                )

                            }
                        }
                    }
                }
            }

        //}
        //catch (e: Exception){
        //    Log.e("devxz", "exception: ${e.localizedMessage}")
        //}

    }
    fun stringToWords(s : String) = s.trim().splitToSequence(' ')
        .filter { it.isNotEmpty() } // or: .filter { it.isNotBlank() }
        .toList()
}
