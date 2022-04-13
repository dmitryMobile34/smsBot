package com.infinitum.smsbot.screens.main

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.projectd.utils.showToast
import com.infinitum.smsbot.SlackBotHandler
import com.infinitum.smsbot.databinding.FragmentAddChannelBinding
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Senders
import com.infinitum.smsbot.utils.APP_ACTIVITY
import com.infinitum.smsbot.utils.REPOSITORY
import com.orhanobut.hawk.Hawk
import java.lang.Exception


class AddSenderFragment : Fragment() {

    private var _binding:FragmentAddChannelBinding?=null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: AddSenderFragmentViewModel
    private lateinit var mObserverChannelList: Observer<List<Channels>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddChannelBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
    }

    private fun initialization() {
        try{
            mViewModel = ViewModelProvider(this).get(AddSenderFragmentViewModel::class.java)

            mObserverChannelList = Observer {
                val catlist = it.asReversed()
                val adapter = ArrayAdapter(
                APP_ACTIVITY.applicationContext,
                R.layout.simple_spinner_item, catlist)
                mBinding.channelsSpinner.adapter = adapter
            }
            REPOSITORY.allChannels.observe(this, mObserverChannelList)

            mBinding.fabAddChannel.setOnClickListener {
                var channelId = (mBinding.channelsSpinner.selectedItem as Channels).id
                var number = mBinding.numberEditText.text.toString()
                if(number.isEmpty()){
                    showToast("Введите номер")
                }
                else {
                    mViewModel.insertSender(
                        Senders(
                            number = number.lowercase(),
                            channelId = channelId
                        )
                    ) {

                    }
                    Hawk.put(number.lowercase(), channelId as String)

                    APP_ACTIVITY.mNavController.navigate(com.infinitum.smsbot.R.id.action_addChannelFragment_to_mainFragment)
                }
            }

        }
        catch (e:Exception){
            Log.e("devx", e.localizedMessage)
        }
    }
}