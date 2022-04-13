package com.infinitum.smsbot

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.infinitum.smsbot.SmsBR.Companion.PERMISSIONS_REQUEST_READ_SMS
import com.infinitum.smsbot.SmsBR.Companion.PERMISSIONS_REQUEST_SERVICE
import com.infinitum.smsbot.databinding.ActivityMainBinding
import com.infinitum.smsbot.screens.main.MainFragment
import com.infinitum.smsbot.utils.APP_ACTIVITY
import com.infinitum.smsbot.utils.IMGBTN
import com.infinitum.smsbot.utils.SMS_TOKEN
import com.orhanobut.hawk.Hawk

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    lateinit var mNavController: NavController
    private var _binding:ActivityMainBinding? = null
    val mBinding get() = _binding!!


    suspend fun getAdvertisingId(context: Context): String =
        withContext(Dispatchers.IO) {
            val adInfo = AdvertisingIdClient(context)
            adInfo.start()
            val adIdInfo = adInfo.info
            Log.d("getAdvertisingId = ",adIdInfo.id.toString())
            adInfo.finish()
            adIdInfo.id
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(mBinding.root)

        APP_ACTIVITY = this


        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)

        Hawk.init(APP_ACTIVITY.applicationContext).build()
        try {

            GlobalScope.launch(Dispatchers.IO) {
                SMS_TOKEN = getAdvertisingId(applicationContext).substring(0, 4)
                Log.d("devx", SMS_TOKEN)
                runOnUiThread {
                    mBinding.idtext.text = SMS_TOKEN;
                }

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(
                    applicationContext!!,
                    Manifest.permission.RECEIVE_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) { // Needs permission
                requestPermissions(
                    arrayOf(Manifest.permission.RECEIVE_SMS),
                    PERMISSIONS_REQUEST_READ_SMS
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    requestPermissions(
                        arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                        PERMISSIONS_REQUEST_SERVICE
                    )
                }

            } else { // Permission has already been granted

            }
            intentService = Intent(this.applicationContext, serviceClass)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.applicationContext.startForegroundService(intentService)
            } else {
                this.applicationContext.startService(intentService)

            }
        }
        catch (e: Exception){

        }
    }
    lateinit var  intentService: Intent
    val serviceClass = SmsService::class.java


    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?): Intent? {
        return super.registerReceiver(receiver, filter)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            SmsBR.PERMISSIONS_REQUEST_READ_SMS -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("devx", "PERMISSIONS_REQUEST_READ_SMS Granted")
                } else {
                    //  toast("Permission must be granted  ")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}