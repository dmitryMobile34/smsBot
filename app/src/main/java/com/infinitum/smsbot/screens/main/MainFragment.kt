package com.infinitum.smsbot.screens.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectd.utils.showToast
import com.infinitum.smsbot.R
import com.infinitum.smsbot.SlackBotHandler
import com.infinitum.smsbot.databinding.FragmentMainBinding
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Messages
import com.infinitum.smsbot.models.Senders
import com.infinitum.smsbot.utils.APP_ACTIVITY
import com.infinitum.smsbot.utils.REPOSITORY
import com.infinitum.smsbot.utils.TYPE_ROOM

import java.lang.Exception
import androidx.core.content.ContextCompat.getSystemService

import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import com.infinitum.smsbot.utils.SMS_TOKEN


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: MainFragmentViewModel
    private lateinit var mVerticalRecyclerView: RecyclerView
    private lateinit var mVerticalAdapter: VerticalRecyclerViewAdapter
    private lateinit var mObserverChannelList: Observer<List<Channels>>
    private lateinit var mObserverSendersList: Observer<List<Senders>>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        mBinding.refreshlayout.setOnRefreshListener {
            initialization()
        }
        initialization()
    }

    private fun initialization() {
        try {

            mViewModel = ViewModelProvider(this).get(MainFragmentViewModel::class.java)
            mViewModel.initDatabase(TYPE_ROOM) {
            }


            mVerticalRecyclerView = mBinding.verticalRecyclerView
            mVerticalRecyclerView.layoutManager = LinearLayoutManager(this.context)
            mVerticalAdapter = VerticalRecyclerViewAdapter()



            mObserverChannelList = Observer {
                val catlist = it.asReversed()

                mVerticalAdapter.setList(catlist)
            }
            mObserverSendersList = Observer {
                val doclist = it.asReversed()
                mVerticalAdapter.setDocs(doclist, mViewModel)
            }

            REPOSITORY.allChannels.observe(this, mObserverChannelList)
            REPOSITORY.allSenders.observe(this, mObserverSendersList)
            mVerticalRecyclerView.adapter = mVerticalAdapter
            mBinding.fab.setOnClickListener {
                APP_ACTIVITY.mNavController.navigate(R.id.action_mainFragment_to_addChannelFragment)
            }

            val channelsList = SlackBotHandler().getChannels().channels

            if(channelsList != null && channelsList.size > 0) {
                for (i in 0 until channelsList.size) {
                    Log.d("devx", "topic is : " + channelsList[i].topic.value)
                    if (channelsList[i].topic.value.contains(SMS_TOKEN)) {
                        mViewModel.insertChannel(
                            Channels(
                                name = channelsList[i].name,
                                id = channelsList[i].id
                            )
                        ) {
                        }

                    }
                }
            }
        }
        catch (e: Exception){

            Log.e("devx", e.localizedMessage)
        }
        mBinding.refreshlayout.isRefreshing = false
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        REPOSITORY.allChannels.removeObserver(mObserverChannelList)
        REPOSITORY.allSenders.removeObserver(mObserverSendersList)
        mVerticalRecyclerView.adapter = null
    }
    companion object{
//        fun click(document: AppDocs, category: AppCats){
//            val bundle = Bundle()
//            bundle.putSerializable("document", document)
//            bundle.putSerializable("category", category)
//            APP_ACTIVITY.mNavController.navigate(R.id.action_mainFragment_to_cardFragment2, bundle)
//        }
//        fun click(document: AppDocs, vm: MainFragmentViewModel){
//            vm.removeDoc(document){
//
//            }
//        }

    }

}