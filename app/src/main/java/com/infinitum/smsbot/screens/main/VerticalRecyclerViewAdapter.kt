package com.infinitum.smsbot.screens.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.infinitum.smsbot.R
import com.infinitum.smsbot.models.Channels
import com.infinitum.smsbot.models.Senders
import com.infinitum.smsbot.utils.APP_ACTIVITY

class VerticalRecyclerViewAdapter: RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalRVViewHolder>(){
    private var mListChannels = mutableListOf<Channels>()
    private var mListSenders = emptyList<Senders>()

    private lateinit var mViewModel: MainFragmentViewModel


    fun setList(listChannels: List<Channels>){
        mListChannels = listChannels.toMutableList()
        notifyDataSetChanged()
    }
    fun setDocs(listSenders: List<Senders>, vm: MainFragmentViewModel) {
        mListSenders = listSenders
        mViewModel = vm
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerticalRecyclerViewAdapter.VerticalRVViewHolder {

        return VerticalRVViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.channel_item, parent, false))

    }

    override fun onBindViewHolder(holder: VerticalRecyclerViewAdapter.VerticalRVViewHolder, position: Int) {
        var model = mListChannels[position]
        var sendersList = mutableListOf<Senders>()
        for(c in mListSenders) {
            if(c.channelId == model.id){
                sendersList.add(c)
            }
        }


//
//        if(APP_ACTIVITY.mNavController.currentDestination?.id == 2131362089 && categoryCards.size < 1){
//            showToast("В категории ${model.name} не осталось документов, поэтому она была скрыта")
//
//        }
//        else {


        holder.title.text = mListChannels[position].name
        var sendersRvAdapter = SendersRvAdapter(
            sendersList,
            mViewModel
        )
        holder.recyclerView.setHasFixedSize(true)
        holder.recyclerView.layoutManager = LinearLayoutManager(
            APP_ACTIVITY.applicationContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.recyclerView.adapter = sendersRvAdapter
    }
//    }

    override fun getItemCount(): Int {
        return mListChannels.size
    }

    class VerticalRVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var recyclerView: RecyclerView
        var title: TextView
        init {
            recyclerView = itemView.findViewById(R.id.recycler_view_horizontal)
            title = itemView.findViewById(R.id.channel_name)
        }

    }
}

class SendersRvAdapter(var senders: List<Senders>, var vm: MainFragmentViewModel) : RecyclerView.Adapter<SendersRvAdapter.RVViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sender_item, parent, false)
        return RVViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        var card = senders[position]
        holder.name?.text = card.number

    }

    override fun getItemCount(): Int {
        return senders.size
    }

    override fun onViewAttachedToWindow(holder: RVViewHolder) {
//        holder.itemView.setOnClickListener{
//            MainFragment.click(message[holder.adapterPosition], channel)
//        }
//        holder.itemView.setOnLongClickListener {
//            MainFragment.click(message[holder.adapterPosition], vm)
//            return@setOnLongClickListener true
//        }

    }

    override fun onViewDetachedFromWindow(holder: RVViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

    inner class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView? = null

        init {
            name = itemView?.findViewById(R.id.sender_number)
        }
    }

}