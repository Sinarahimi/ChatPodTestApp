package ir.fanap.chattestapp.application.ui.function

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.fanap.podchat.mainmodel.Contact
import com.fanap.podchat.mainmodel.Invitee
import com.fanap.podchat.mainmodel.RequestThreadInnerMessage
import com.fanap.podchat.mainmodel.ResultDeleteMessage
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import com.fanap.podchat.util.ThreadType
import com.github.javafaker.Faker
import com.wang.avi.AVLoadingIndicatorView
import ir.fanap.chattestapp.BuildConfig
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.bussines.model.Method
import ir.fanap.chattestapp.application.ui.TestListener
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.GET_HISTORY
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncFour
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncOne
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncThree
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncTwo
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodNames
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

class FunctionFragment : Fragment(), FunctionAdapter.ViewHolderListener, TestListener {

    private lateinit var buttonCoonect: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var avLoadingIndicatorView: AVLoadingIndicatorView
    private lateinit var linearlayoutMangaer: LinearLayoutManager
    private lateinit var recyclerViewSmooth: RecyclerView.SmoothScroller
    private var methods: MutableList<Method> = mutableListOf()
    private var fucCallback: HashMap<String, String> = hashMapOf()
    private lateinit var textView_state: TextView
    private lateinit var functionAdapter: FunctionAdapter
    private var sandbox = false
    private val faker: Faker = Faker()
    private val TOKEN = "5fb88da4c6914d07a501a76d68a62363"
    private val SANDB_TOKEN = "8096f3e176a84aebb672a5e2f2107a02"

    //TODO change to CallBackMethod
    /**/
    companion object {

        fun newInstance(): FunctionFragment {
            return FunctionFragment()
        }
    }

    override fun onIconClicked(clickedViewHolder: FunctionAdapter.ViewHolder) {
        var position = clickedViewHolder.adapterPosition
        when (position) {
            0 -> {
                createThread()
            }
            1 -> {
                getContact()
            }
            2 -> {
                blockContact()
            }
            3 -> {
                addContact()
            }
            4 -> {
                getThread()
            }
            5 -> {
                getBlockList()
            }
            6 -> {
                unBlockContact()
            }
            7 -> {
                updateContact()
            }
            8 -> {
                sendTextMsg()
            }
            9 -> {
                removeContact()
            }
            10 -> {
                addParticipant()
            }
            11 -> {
                removeParticipant()
            }
            12 -> {
                forwardMessage()
            }
            13 -> {
                replyMessage()
            }
            14 -> {
                leaveThread()
            }
            15 -> {
                muteThread()
            }
            16 -> {
                unMuteThread()
            }
            17 -> {
                deleteMessage()
            }
            18 -> {
                editMessage()
            }
            19 -> {
                getHistory()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_function, container, false)
        buttonCoonect = view.findViewById(R.id.button_Connect)
        recyclerView = view.findViewById(R.id.recyclerV_funcFrag)
        textView_state = view.findViewById(R.id.textView_state)
        avLoadingIndicatorView = view.findViewById(R.id.AVLoadingIndicatorView)
        recyclerView.setHasFixedSize(true)
        linearlayoutMangaer = LinearLayoutManager(context)
        recyclerView.layoutManager = linearlayoutMangaer
        recyclerViewSmooth = object : LinearSmoothScroller(activity) {
            override fun getVerticalSnapPreference(): Int {
                return LinearSmoothScroller.SNAP_TO_START
            }
        }

        for (i in 0..19) {
            val method = Method()
            method.methodName = methodNames[i]
            method.funcOne = methodFuncOne[i]
            method.funcTwo = methodFuncTwo[i]
            method.funcThree = methodFuncThree[i]
            method.funcFour = methodFuncFour[i]
            methods.add(method)
        }

        functionAdapter = FunctionAdapter(methods, this)
        recyclerView.adapter = functionAdapter

        recyclerView.childCount
        buttonCoonect.setOnClickListener { connect() }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.setTestListener(this)
        mainViewModel.observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            textView_state.text = it

            if (it.equals("CHAT_READY")) {
                avLoadingIndicatorView.visibility = View.GONE
                textView_state.setTextColor(
                    ContextCompat.getColor(activity?.applicationContext!!, R.color.green_active)
                )
            }
        }
    }

    private fun getHistory() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.GET_HISTORY] = uniqueId
    }

    private fun editMessage() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.EDIT_MESSAGE] = uniqueId

    }

    private fun deleteMessage() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.DELETE_MESSAGE] = uniqueId

    }

    override fun onBlockList(response: ChatResponse<ResultBlockList>?) {
        super.onBlockList(response)
        var position = 5
        changeIconReceive(position)
    }

    override fun onError(chatResponse: ErrorOutPut?) {
        super.onError(chatResponse)
        activity?.runOnUiThread {
            Toast.makeText(activity, chatResponse?.errorMessage, Toast.LENGTH_LONG).show()
        }
        val uniqueId = chatResponse?.uniqueId
        if (uniqueId == fucCallback[ConstantMsgType.ADD_CONTACT]) {
            fucCallback[uniqueId]
            activity?.runOnUiThread {
                val viewHolder: RecyclerView.ViewHolder = recyclerView.getChildViewHolder(recyclerView.getChildAt(3))
                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.checkBox_test)
                    .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorAccent))
            }
        }
    }

    override fun onUnBlock(response: ChatResponse<ResultBlock>?) {
        super.onUnBlock(response)
        val position = 6
        if (fucCallback[ConstantMsgType.UNBLOCK_CONTACT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UNBLOCK_CONTACT)

            changeIconReceive(position)
        }

    }

    override fun onGetThread(chatResponse: ChatResponse<ResultThreads>?) {
        super.onGetThread(chatResponse)

        if (fucCallback[ConstantMsgType.GET_THREAD] == chatResponse?.uniqueId) {
            val position = 4
            fucCallback.remove(ConstantMsgType.GET_THREAD)
            changeIconReceive(position)
        }
        if (fucCallback[ConstantMsgType.SEND_MESSAGE] == chatResponse?.uniqueId) {
            if (chatResponse?.result?.threads?.size!! > 0) {
                fucCallback.remove(ConstantMsgType.SEND_MESSAGE)
                val threadId = chatResponse.result.threads[0].id
                val requestMessage = RequestMessage.Builder(faker.lorem().paragraph(), threadId).build()
                fucCallback[ConstantMsgType.SEND_MESSAGE] = mainViewModel.sendTextMsg(requestMessage)
            } else {
                val requestGetContact = RequestGetContact.Builder().build()
                fucCallback.remove(ConstantMsgType.SEND_MESSAGE)
                fucCallback[ConstantMsgType.SEND_MESSAGE] = mainViewModel.getContact(requestGetContact)
            }
        }
    }

    override fun onDeleteMessage(response: ChatResponse<ResultDeleteMessage>?) {
        super.onDeleteMessage(response)
        if (fucCallback[ConstantMsgType.DELETE_MESSAGE] == response?.uniqueId) {
            val position = 17
            changeIconReceive(position)
        }
    }

    override fun onEditedMessage(response: ChatResponse<ResultNewMessage>?) {
        super.onEditedMessage(response)

    }

    override fun onSent(response: ChatResponse<ResultMessage>?) {
        super.onSent(response)

        if (fucCallback[ConstantMsgType.EDIT_MESSAGE] == response?.uniqueId) {
            val requestEditMessage = RequestEditMessage.Builder("this is edit ", response!!.result.messageId).build()
            fucCallback[ConstantMsgType.EDIT_MESSAGE] = mainViewModel.editMessage(requestEditMessage)
        }

        if (fucCallback[ConstantMsgType.DELETE_MESSAGE_ID] == response?.uniqueId) {
            val requestDeleteMessage = RequestDeleteMessage.Builder(response!!.result.messageId).build()
            fucCallback[ConstantMsgType.DELETE_MESSAGE] = mainViewModel.deleteMessage(requestDeleteMessage)
        }

        if (fucCallback[ConstantMsgType.REPLY_MESSAGE] == response?.uniqueId) {
            val position = 13
            changeIconReceive(position)
        }

        if (fucCallback[ConstantMsgType.REPLY_MESSAGE_ID] == response?.uniqueId) {
            val messageId = response?.result?.messageId
            val threadId = fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID]
            val replyMessage =
                RequestReplyMessage.Builder("this is replyMessage", threadId?.toLong()!!, messageId!!).build()
            fucCallback[ConstantMsgType.REPLY_MESSAGE] = mainViewModel.replyMessage(replyMessage)
            val position = 13
            changeIconSend(position)
        }

        if (fucCallback[ConstantMsgType.FORWARD_MESSAGE] == response?.uniqueId) {
            val position = 12
            changeSecondIconReceive(position)
        }

        if (fucCallback[ConstantMsgType.FORWARD_MESSAGE_ID] == response?.uniqueId) {

            val messageIds = ArrayList<Long>()
            messageIds.add(response?.result?.messageId!!)

            val threadId = fucCallback[ConstantMsgType.FORWARD_MESSAGE_THREAD_ID]
            val requestForwardMessage = RequestForwardMessage.Builder(threadId!!.toLong(), messageIds).build()
            fucCallback[ConstantMsgType.FORWARD_MESSAGE] = mainViewModel.forwardMessage(requestForwardMessage)[0]
            val position = 12
            changeIconSend(position)

        }
        if (fucCallback[ConstantMsgType.SEND_MESSAGE] == response?.uniqueId) {
            val position = 8
            changeSecondIconReceive(position)
        }
    }

    override fun onSeen(response: ChatResponse<ResultMessage>?) {
        super.onSeen(response)
        val position = 8
        changeFourthIconReceive(position)
    }

    override fun onDeliver(response: ChatResponse<ResultMessage>?) {
        super.onDeliver(response)
        val position = 8
        changeThirdIconReceive(position)
    }

    override fun onLeaveThread(response: ChatResponse<ResultLeaveThread>?) {
        super.onLeaveThread(response)
        val position = 14
        changeIconReceive(position)
    }

    override fun onUpdateContact(response: ChatResponse<ResultUpdateContact>?) {
        super.onUpdateContact(response)
        if (fucCallback[ConstantMsgType.UPDATE_CONTACT] == response?.uniqueId) {
            val position = 7
            changeIconReceive(position)
        }
    }

    /**
     * OnBlock receive message
     * */
    override fun onBlock(chatResponse: ChatResponse<ResultBlock>?) {
        super.onBlock(chatResponse)
        if (fucCallback[ConstantMsgType.BLOCK_CONTACT] == chatResponse?.uniqueId) {
            val position = 2

            changeIconReceive(position)

            val id = chatResponse?.result?.contact?.id
            if (id != null) {
                val requestUnBlock = RequestUnBlock.Builder(id).build()
                mainViewModel.unBlock(requestUnBlock)
            }
        }
        if (fucCallback[ConstantMsgType.UNBLOCK_CONTACT] == chatResponse?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UNBLOCK_CONTACT)
            val contactId = chatResponse?.result?.contact?.id
            if (contactId != null) {
                val requestUnBlock = RequestUnBlock.Builder(contactId).build()
                fucCallback[ConstantMsgType.UNBLOCK_CONTACT] = mainViewModel.unBlock(requestUnBlock)
            }
        }
    }

    override fun onAddContact(response: ChatResponse<ResultAddContact>?) {
        super.onAddContact(response)
        if (fucCallback[ConstantMsgType.ADD_CONTACT] == response?.uniqueId) {
            val position = 3
            changeIconReceive(position)

            var id = response?.result?.contact?.id
            if (id != null) {
                val requestRemoveContact = RequestRemoveContact.Builder(id).build()
                mainViewModel.removeContact(requestRemoveContact)
            }
        }

        if (fucCallback[ConstantMsgType.REMOVE_CONTACT] == response?.uniqueId) {
            var id = response?.result?.contact?.id
            if (id != null) {
                fucCallback.remove(ConstantMsgType.REMOVE_CONTACT)
                val requestRemoveContact = RequestRemoveContact.Builder(id).build()
                fucCallback[ConstantMsgType.REMOVE_CONTACT] = mainViewModel.removeContact(requestRemoveContact)
                val position = 9
                changeIconSend(position)
            }
        }
    }

    override fun onMuteThread(response: ChatResponse<ResultMute>?) {
        super.onMuteThread(response)
        if (fucCallback[ConstantMsgType.MUTE_THREAD] == response?.uniqueId) {
            val position = 15
            changeIconReceive(position)
        }

    }

    override fun onUnmuteThread(response: ChatResponse<ResultMute>?) {
        super.onUnmuteThread(response)
        if (fucCallback[ConstantMsgType.UNMUTE_THREAD] == response?.uniqueId) {
            val position = 16
            changeIconReceive(position)
        }

    }

    override fun onGetHistory(response: ChatResponse<ResultHistory>?) {
        super.onGetHistory(response)
        if (fucCallback[ConstantMsgType.GET_HISTORY] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.GET_HISTORY)
            val position = 19
            changeIconReceive(position)
        }
    }

    override fun onCreateThread(response: ChatResponse<ResultThread>?) {
        super.onCreateThread(response)

        if (fucCallback[ConstantMsgType.GET_HISTORY] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.GET_HISTORY)
            val threadId = response!!.result.thread.id
            val requestGetHistory = RequestGetHistory.Builder(threadId).build()
            fucCallback[ConstantMsgType.GET_HISTORY] = mainViewModel.getHistory(requestGetHistory)
        }

        if (fucCallback[ConstantMsgType.UNMUTE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UNMUTE_THREAD)
            val threadId = response!!.result.thread.id
            val requestMuteThread = RequestMuteThread.Builder(threadId).build()
            fucCallback[ConstantMsgType.UNMUTE_THREAD] = mainViewModel.unMuteThread(requestMuteThread)
        }

        if (fucCallback[ConstantMsgType.MUTE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.MUTE_THREAD)
            val threadId = response!!.result.thread.id
            val requestMuteThread = RequestMuteThread.Builder(threadId).build()
            fucCallback[ConstantMsgType.MUTE_THREAD] = mainViewModel.muteThread(requestMuteThread)
        }

        if (fucCallback[ConstantMsgType.CREATE_THREAD] == response?.uniqueId) {
            val position = 0
            changeIconReceive(position)

        }

        if (fucCallback[ConstantMsgType.CREATE_THREAD_CHANNEL] == response?.uniqueId) {
            val position = 0
            changeSecondIconReceive(position)
        }

        if (fucCallback[ConstantMsgType.CREATE_THREAD_CHANNEL_GROUP] == response?.uniqueId) {
            val position = 0
            changeThirdIconReceive(position)
        }

        if (fucCallback[ConstantMsgType.CREATE_THREAD_PUBLIC_GROUP] == response?.uniqueId) {
            val position = 0
            changeFourthIconReceive(position)
        }

        if (fucCallback[ConstantMsgType.LEAVE_THREAD] == response?.uniqueId) {
            val threadId = response?.result?.thread?.id
            val requeLeaveThread = RequestLeaveThread.Builder(threadId!!.toLong()).build()
            fucCallback[ConstantMsgType.LEAVE_THREAD] = mainViewModel.leaveThread(requeLeaveThread)
            val position = 14
            changeIconSend(position)
        }

        if (fucCallback[ConstantMsgType.FORWARD_MESSAGE] == response?.uniqueId) {
            val threadId = response?.result?.thread?.id
            fucCallback[ConstantMsgType.FORWARD_MESSAGE_THREAD_ID] = threadId.toString()
        }
        if (fucCallback[ConstantMsgType.REPLY_MESSAGE] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.REPLY_MESSAGE)
            val threadId = response?.result?.thread?.id
            fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID] = threadId.toString()

        }

        if (fucCallback[ConstantMsgType.ADD_PARTICIPANT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.ADD_PARTICIPANT)
            var participantId = fucCallback["ADD_PARTICIPANT_ID"]
            val partId = participantId?.toLong()
            val threadId = response?.result?.thread?.id
            if (partId != null && threadId != null) {
                val contactIdList: MutableList<Long> = mutableListOf()
                contactIdList.add(partId)
                val requestAddParticipants = RequestAddParticipants.Builder(threadId, contactIdList).build()
                mainViewModel.addParticipant(requestAddParticipants)
                fucCallback.remove("ADD_PARTICIPANT_ID")
            }

        }
    }

    private fun connect() {
        avLoadingIndicatorView.visibility = View.VISIBLE
        if (sandbox) {
            //sandBox

            mainViewModel.connect(
                BuildConfig.SANDB_SOCKET_ADDRESS,
                BuildConfig.APP_ID,
                BuildConfig.SERVER_NAME
                ,
                SANDB_TOKEN,
                BuildConfig.SANDB_SSO_HOST,
                BuildConfig.SANDB_PLATFORM_HOST,
                BuildConfig.SANDB_FILE_SERVER,
                null
            )
        } else {
            //Local

            mainViewModel.connect(
                BuildConfig.SOCKET_ADDRESS, BuildConfig.APP_ID, BuildConfig.SERVER_NAME
                , TOKEN, BuildConfig.SSO_HOST, BuildConfig.PLATFORM_HOST, BuildConfig.FILE_SERVER, null
            )
//            mainViewModel.connect(
//                "ws://172.16.110.131:8003/ws", BuildConfig.APP_ID, "chat-server2"
//                , TOKEN, BuildConfig.SSO_HOST, BuildConfig.PLATFORM_HOST, BuildConfig.FILE_SERVER, null
//            )
        }


    }

    private fun changeFourthIconReceive(position: Int) {
        activity?.runOnUiThread {
            if (view != null) {
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFourth)
                    .setImageResource(R.drawable.ic_round_done_all_24px)

                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFourth)
                    .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            }
        }
    }

    private fun changeThirdIconReceive(position: Int) {
        activity?.runOnUiThread {
            if (view != null) {
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickThird)
                    .setImageResource(R.drawable.ic_round_done_all_24px)

                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickThird)
                    .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            }
        }
    }


    private fun changeSecondIconReceive(position: Int) {
        activity?.runOnUiThread {
            if (view != null) {
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFirst)
                    .setImageResource(R.drawable.ic_round_done_all_24px)

                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFirst)
                    .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            }
        }

    }

    override fun onRemoveContact(response: ChatResponse<ResultRemoveContact>?) {
        super.onRemoveContact(response)
        if (fucCallback[ConstantMsgType.REMOVE_CONTACT] == response?.uniqueId) {
            val position = 9
            changeIconReceive(position)
        }
    }

    override fun onGetContact(response: ChatResponse<ResultContact>?) {
        super.onGetContact(response)
        val contactList = response?.result?.contacts

        if (fucCallback[GET_HISTORY] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.GET_HISTORY)
            handleGetHistory(contactList)
        }

        if (fucCallback[ConstantMsgType.EDIT_MESSAGE] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.DELETE_MESSAGE)
            handleEditMessage(contactList)
        }

        if (fucCallback[ConstantMsgType.DELETE_MESSAGE] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.DELETE_MESSAGE)
            handleDeleteMessage(contactList)
        }

        if (fucCallback[ConstantMsgType.UNMUTE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UNMUTE_THREAD)
            handleUnmuteThread(contactList)
        }

        if (fucCallback[ConstantMsgType.MUTE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.MUTE_THREAD)
            handleMuteThread(contactList)
        }

        if (fucCallback[ConstantMsgType.LEAVE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.LEAVE_THREAD)
            handleLeaveThread(contactList)
        }

        if (fucCallback[ConstantMsgType.REPLY_MESSAGE] == response?.uniqueId) {
            handleReplyMessage(contactList)
        }

        if (fucCallback[ConstantMsgType.SEND_MESSAGE] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.SEND_MESSAGE)
            handleSendMessageResponse(contactList)
        }

        if (fucCallback[ConstantMsgType.CREATE_THREAD] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.CREATE_THREAD)
            handleGetThreadResponse(contactList)
        }
        if (fucCallback[ConstantMsgType.GET_CONTACT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.GET_CONTACT)
            val position = 1
            changeIconReceive(position)

        }
        if (fucCallback[ConstantMsgType.BLOCK_CONTACT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.BLOCK_CONTACT)
            handleBlockContact(contactList)
        }

        if (fucCallback[ConstantMsgType.UPDATE_CONTACT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UPDATE_CONTACT)
            handleUpdateContact(contactList)
        }

        if (fucCallback[ConstantMsgType.UNBLOCK_CONTACT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.UNBLOCK_CONTACT)
            handleUnBlockContact(contactList)
        }
        if (fucCallback[ConstantMsgType.ADD_PARTICIPANT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.ADD_PARTICIPANT)
            handleAddParticipant(contactList)
        }
        if (fucCallback[ConstantMsgType.REMOVE_PARTICIPANT] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.REMOVE_PARTICIPANT)
            handleRemoveParticipant(contactList)
        }
        if (fucCallback[ConstantMsgType.FORWARD_MESSAGE] == response?.uniqueId) {
            fucCallback.remove(ConstantMsgType.FORWARD_MESSAGE)
            handleForward(contactList)
            // CREATE WITH MSSAGE CONTACT
            // STORE THE MESSAGE ID
            //
        }
    }

    private fun handleGetHistory(contactList: ArrayList<Contact>?) {

        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 1) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))
//                        val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
//                        val requestCreateThread: RequestCreateThread =
//                            RequestCreateThread.Builder(0, inviteList)
//                                .message(requestThreadInnerMessage)
//                                .build()
                        val list = Array<Invitee>(1) { Invitee(inviteList[0].id, 2) }

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, list, "nothing", ""
                            , "", ""
                        )

                        fucCallback[ConstantMsgType.GET_HISTORY] = uniqueId
                    }
                    break
                }
            }
        }

    }

    private fun handleEditMessage(contactList: ArrayList<Contact>?) {

        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 1) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))
                        val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                        val requestCreateThread: RequestCreateThread =
                            RequestCreateThread.Builder(0, inviteList)
                                .message(requestThreadInnerMessage)
                                .build()
                        val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                        fucCallback[ConstantMsgType.DELETE_MESSAGE_ID] = uniqueId!![1]
                    }
                    break
                }
            }
        }
    }

    private fun handleDeleteMessage(contactList: ArrayList<Contact>?) {

        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 1) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))
                        val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                        val requestCreateThread: RequestCreateThread =
                            RequestCreateThread.Builder(0, inviteList)
                                .message(requestThreadInnerMessage)
                                .build()
                        val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                        fucCallback[ConstantMsgType.DELETE_MESSAGE_ID] = uniqueId!![1]
                    }
                    break
                }
            }
        }
    }

    private fun handleUnmuteThread(contactList: ArrayList<Contact>?) {

    }

    private fun handleMuteThread(contactList: ArrayList<Contact>?) {


    }

    private fun handleLeaveThread(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    if (uniqueId?.get(0) != null) {
                        fucCallback[ConstantMsgType.LEAVE_THREAD] = uniqueId[0]
                    }
                    break
                }
            }
        }
    }

    private fun handleReplyMessage(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID] = uniqueId!![0]
                    fucCallback[ConstantMsgType.REPLY_MESSAGE_ID] = uniqueId[1]
                    break
                }
            }
        }

    }

    override fun onThreadAddParticipant(response: ChatResponse<ResultAddParticipant>?) {
        super.onThreadAddParticipant(response)

        val position = 10
        changeIconReceive(position)
    }

    private fun handleForward(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    fucCallback[ConstantMsgType.FORWARD_MESSAGE] = uniqueId!![0]
                    fucCallback[ConstantMsgType.FORWARD_MESSAGE_ID] = uniqueId[1]
                    break
                }
            }
        }
    }

    private fun handleAddParticipant(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = Array<Invitee>(1, { i -> Invitee(contactId, 2) })
                    inviteList[0].id = contactId

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )
                    fucCallback[ConstantMsgType.ADD_PARTICIPANT] = uniqueId
                    choose++
                    if (choose == 2) {
                        fucCallback["ADD_PARTICIPANT_ID"] = contactId.toString()
                        break
                    }
                }
            }
        }
    }

    private fun removeContact() {
//        val
        val requestAddContact = RequestAddContact.Builder()
            .firstName(faker.name().firstName())
            .lastName(faker.name().lastName())
            .email(faker.lordOfTheRings().character() + "@Gmail.com")
            .cellphoneNumber(faker.phoneNumber().cellPhone())
            .build()
        fucCallback[ConstantMsgType.REMOVE_CONTACT] = mainViewModel.addContacts(requestAddContact)

    }

    private fun handleSendMessageResponse(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    fucCallback[ConstantMsgType.SEND_MESSAGE] = uniqueId!![0]
                    break
                }
            }
        }
    }

    //Response from getContact
    /**
     *
     * */
    private fun handleUnBlockContact(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val requestBlock = RequestBlock.Builder(contactId).build()
                    val uniqueId = mainViewModel.blockContact(requestBlock)
                    fucCallback[ConstantMsgType.UNBLOCK_CONTACT] = uniqueId

                    break
                }
            }
        }
    }

    private fun handleUpdateContact(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactid = contact.id
                    val cellPhoneNumber = contact.cellphoneNumber
                    val firstName = contact.firstName
                    val lastName = contact.lastName
                    val email = contact.email

                    val requestUpdateContact = RequestUpdateContact.Builder(contactid)
                        .cellphoneNumber(cellPhoneNumber)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .build()

                    val uniqueId = mainViewModel.updateContact(requestUpdateContact)
                    fucCallback[ConstantMsgType.UPDATE_CONTACT] = uniqueId
                    changeIconSend(7)
                    break
                }
            }
        }
    }


    private fun handleBlockContact(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val requestBlock = RequestBlock.Builder(contactId).build()
                    val uniqueId = mainViewModel.blockContact(requestBlock)
                    fucCallback[ConstantMsgType.BLOCK_CONTACT] = uniqueId
                    changeIconSend(2)
                    break
                }
            }
        }
    }


    //Get Thread
    // If there is no Thread
    // Its create Thread with someone that has userId
    // Then send Message to that thread
    private fun sendTextMsg() {
        val requestThread = RequestThread.Builder().build()
        fucCallback[ConstantMsgType.SEND_MESSAGE] = mainViewModel.getThread(requestThread)

//        val requestMessage = RequestMessage.Builder()
//        mainViewModel.sendTextMsg()ConstantMsgType.UNBLOCK_CONTACT
    }


    private fun unMuteThread() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.UNMUTE_THREAD] = mainViewModel.getContact(requestGetContact)
        val position = 16
        changeIconSend(position)
//        val requestMuteThread = RequestMuteThread.Builder(threadId).build()
//        mainViewModel.unMuteThread(requestMuteThread)
    }

    private fun muteThread() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.MUTE_THREAD] = mainViewModel.getContact(requestGetContact)
        val position = 15
        changeIconSend(position)
    }

    private fun leaveThread() {
        val requestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.LEAVE_THREAD] = mainViewModel.getContact(requestGetContact)
    }

    private fun removeParticipant() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.REMOVE_PARTICIPANT] = mainViewModel.getContact(requestGetContact)
        val position = 11
        changeIconSend(position)
    }

    private fun blockContact() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.BLOCK_CONTACT] = uniqueId
    }

    private fun addContact() {

        val requestAddContact = RequestAddContact.Builder()
            .cellphoneNumber(faker.phoneNumber()?.phoneNumber())
            .firstName(faker.name()?.firstName())
            .lastName(faker.name()?.lastName())
            .build()
        val uniqueId = mainViewModel.addContacts(requestAddContact)
        fucCallback[ConstantMsgType.ADD_CONTACT] = uniqueId
        val position = 3
        changeIconSend(position)
    }

    private fun getContact() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.GET_CONTACT] = uniqueId
    }

    private fun createThread() {
        //get contact
        // search for evey one that has user
        // create thread with that

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.CREATE_THREAD] = uniqueId
        changeIconSend(0)
    }

    private fun creatThreadWithMessage() {

    }

    private fun createThreadOwnerGroup(inviteList: ArrayList<Invitee>) {

        val list = Array<Invitee>(1) { Invitee(inviteList[0].id, 2) }

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.OWNER_GROUP, list, "nothing", ""
            , "", ""
        )
        fucCallback[ConstantMsgType.CREATE_THREAD_OWNER_GROUP] = uniqueId
    }

    private fun createThreadPublicGroup(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1) { Invitee(inviteList[0].id, 2) }

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.PUBLIC_GROUP, list, "nothing", ""
            , "", ""
        )
        fucCallback[ConstantMsgType.CREATE_THREAD_PUBLIC_GROUP] = uniqueId
    }

    private fun createThreadChannelGroup(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1, { i -> Invitee(inviteList[0].id, 2) })

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.CHANNEL_GROUP, list, "nothing", ""
            , "", ""
        )
        fucCallback[ConstantMsgType.CREATE_THREAD_CHANNEL_GROUP] = uniqueId

    }

    private fun createThreadChannel(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1, { i -> Invitee(inviteList[0].id, 2) })

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.CHANNEL, list, "nothing", ""
            , "", ""
        )
        fucCallback[ConstantMsgType.CREATE_THREAD_CHANNEL] = uniqueId
    }


    private fun handleRemoveParticipant(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id
                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
//                    val uniqueId = mainViewModel.createThread(requestCreateThread)
//                    fucCallback[ConstantMsgType.CREATE_THREAD] = uniqueId
                    break
                }
            }
        }
    }

    //handle getContact response to create thread
    //
    /**
     * Create the thread to p to p/channel/group. The list below is showing all of the threads type
     * int NORMAL = 0;
     * int OWNER_GROUP = 1;
     * int PUBLIC_GROUP = 2;
     * int CHANNEL_GROUP = 4;
     * int TO_BE_USER_ID = 5;
     * <p>
     * int CHANNEL = 8;
     */
    private fun handleGetThreadResponse(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id
                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage = RequestThreadInnerMessage.Builder(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    fucCallback[ConstantMsgType.CREATE_THREAD] = uniqueId!![0]

                    createThreadChannel(inviteList)
                    createThreadChannelGroup(inviteList)
                    createThreadOwnerGroup(inviteList)
                    createThreadPublicGroup(inviteList)
                    break
                }
            }
        }
    }

    private fun unBlockContact() {
        // get Contact
        // block contact with 3 params
        //
        val requestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.UNBLOCK_CONTACT] = mainViewModel.getContact(requestGetContact)

    }

    private fun updateContact() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.UPDATE_CONTACT] = mainViewModel.getContact(requestGetContact)
        val position = 7
        changeIconSend(position)
    }

    private fun getBlockList() {
        val requestBlockList = RequestBlockList.Builder().build()
        mainViewModel.getBlockList(requestBlockList)
    }

    private fun getThread() {
        val requestThread = RequestThread.Builder().build()
        fucCallback[ConstantMsgType.GET_THREAD] = mainViewModel.getThread(requestThread)
        val position = 4
        changeIconSend(position)
    }

    private fun scroll(position: Int) {
        recyclerViewSmooth.targetPosition = position
        linearlayoutMangaer.startSmoothScroll(recyclerViewSmooth)
    }

    private fun changeIconReceive(position: Int) {

        activity?.runOnUiThread {
            val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            viewHolder.itemView.findViewById<AppCompatImageView>(R.id.checkBox_test)
                .setImageResource(R.drawable.ic_round_done_all_24px)
            viewHolder.itemView.findViewById<AppCompatImageView>(R.id.checkBox_test)
                .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }
    }

    private fun changeIconSend(position: Int) {
        activity?.runOnUiThread {
            val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            viewHolder.itemView.findViewById<AppCompatImageView>(R.id.checkBox_test)
                .setImageResource(R.drawable.ic_round_done_all_24px)
        }
    }
/*
* getContact
*choose one of the contact and create thread with that TYPE_PUBLIC_GROUP
 *choose another to add as a participant
* */

    private fun addParticipant() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.ADD_PARTICIPANT] = mainViewModel.getContact(requestGetContact)
        val position = 10
        changeIconSend(position)
    }

    private fun forwardMessage() {

        val requestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.FORWARD_MESSAGE] = mainViewModel.getContact(requestGetContact)


        //get contact
        //createThread with message with that first contact
        //createThread with second contact

        //forward the message from first thread to second thread

        // if the sent type come then its sent

    }

    private fun replyMessage() {
        val requestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.REPLY_MESSAGE] = mainViewModel.getContact(requestGetContact)

        //getContact
        //CreateThread with message
        //get that message id and thread id and call reply Message
    }

}

