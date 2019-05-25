package ir.fanap.chattestapp.application.ui.function

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.fanap.podchat.mainmodel.Contact
import com.fanap.podchat.mainmodel.Invitee
import com.fanap.podchat.mainmodel.RequestThreadInnerMessage
import com.fanap.podchat.mainmodel.ResultDeleteMessage
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import com.fanap.podchat.util.ThreadType
import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wang.avi.AVLoadingIndicatorView
import ir.fanap.chattestapp.BuildConfig
import ir.fanap.chattestapp.R
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.bussines.model.Method
import ir.fanap.chattestapp.application.ui.TestListener
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.ADD_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.ADD_PARTICIPANT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.ADD_PARTICIPANT_ID
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.BLOCK_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.BLOCK_LIST
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.CREATE_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.CREATE_THREAD_WITH_FORW_MSG
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.EMPTY_ERROR_LOG
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.ERROR
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.FORWARD_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.GET_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.GET_HISTORY
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.GET_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.LEAVE_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.MUTE_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REMOVE_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REMOVE_PARTICIPANT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REMOVE_PARTICIPANT_ID
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REPLY_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.SEND_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.SUCCESSFUL
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.UNBLOCK_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.UNMUTE_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.UPDATE_CONTACT
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncFour
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncOne
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncThree
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodFuncTwo
import ir.fanap.chattestapp.application.ui.util.MethodList.Companion.methodNames
import ir.fanap.chattestapp.bussines.model.CallBackMethod
import ir.fanap.chattestapp.bussines.model.FunctionStatus
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class FunctionFragment : Fragment(), FunctionAdapter.ViewHolderListener, TestListener {

    private lateinit var buttonConect: Button
    private lateinit var switchCompat_sandBox: SwitchCompat
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView_response: RecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var appCompatImageView_noResponse: AppCompatImageView
    private lateinit var txtView_noResponse: TextView
    private lateinit var scrollView_log: ScrollView

    private lateinit var avLoadingIndicatorView: AVLoadingIndicatorView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerViewSmooth: RecyclerView.SmoothScroller
    private lateinit var bottom_sheet_log: ConstraintLayout
    private lateinit var bottomSheetLog: BottomSheetBehavior<ConstraintLayout>
    private var gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private var methods: MutableList<Method> = mutableListOf()
    private var funcCallback: HashMap<String, CallBackMethod> = hashMapOf()
    private lateinit var textView_state: TextView
    private lateinit var textView_log: TextView
    private lateinit var functionAdapter: FunctionAdapter
    private var sandbox = false
    private val faker: Faker = Faker()
    private val TOKEN = "5fb88da4c6914d07a501a76d68a62363"
    private val SANDB_TOKEN = "8c4617792bf54aad92f6c6467e3a31f7"

    companion object {

        fun newInstance(): FunctionFragment {
            return FunctionFragment()
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

    override fun onLogClicked(clickedViewHolder: FunctionAdapter.ViewHolder) {
        val position = clickedViewHolder.adapterPosition


        bottomSheetLog.state = BottomSheetBehavior.STATE_EXPANDED

        val subMethods = methods[position].funcStatusList!!
        val responseAdapter = ResponseAdapter(subMethods)
        recyclerView_response.adapter = responseAdapter

        if (methods[position].log != null && !methods[position].log?.isEmpty()!!) {
            textView_log.text = methods[position].log
//            when (position) {
//                0 -> {
//
//                }
//                1 -> {
//                    textView_log.text = methods[position].log
//                }
//                2 -> {
//                    textView_log.text = methods[position].log
//                }
//
//
//            }
        }

        if (subMethods.size >= 1) {

            appCompatImageView_noResponse.visibility = View.GONE
            txtView_noResponse.visibility = View.GONE

            recyclerView_response.visibility = View.VISIBLE
        } else {
            recyclerView_response.visibility = View.GONE
            appCompatImageView_noResponse.visibility = View.VISIBLE
            txtView_noResponse.visibility = View.VISIBLE
        }

        if (textView_log.text.isEmpty()) {
            appCompatImageView_noResponse.visibility = View.VISIBLE
            txtView_noResponse.visibility = View.VISIBLE

            scrollView_log.visibility = View.GONE

        } else {
            appCompatImageView_noResponse.visibility = View.GONE
            txtView_noResponse.visibility = View.GONE

            scrollView_log.visibility = View.VISIBLE
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
            20 -> {
                createThreadWithForwMessage()
            }
            21 -> {
                getPartitipant()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_function, container, false)
        initView(view)

        val buttonClose = view.findViewById(R.id.button_close) as Button

        bottomSheetSetup(buttonClose)

        setupMethodRecyclerSetup()
        setupFunctionRecyclerView()


        buttonConect.setOnClickListener { connect() }
        switchCompat_sandBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            sandbox = true
        })

        return view
    }

    private fun setupMethodRecyclerSetup() {
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerViewSmooth = object : LinearSmoothScroller(activity) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        for (i in 0..21) {
            val method = Method()
            val mutableList = arrayListOf<FunctionStatus>()
            method.methodName = methodNames[i]
            method.funcOne = methodFuncOne[i]
            method.funcTwo = methodFuncTwo[i]
            method.funcThree = methodFuncThree[i]
            method.funcFour = methodFuncFour[i]
            val functionStatus = FunctionStatus()
            functionStatus.errorLog = ""
            functionStatus.methodName = ""
            functionStatus.status = ""
            method.funcStatusList = mutableList
            methods.add(method)
        }

        functionAdapter = FunctionAdapter(this.activity!!, methods, this)
        recyclerView.adapter = functionAdapter
    }

    private fun bottomSheetSetup(button_close: Button) {
        bottomSheetLog = BottomSheetBehavior.from<ConstraintLayout>(bottom_sheet_log)
        bottomSheetLog.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(view: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        textView_log.text = ""
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //                        textView_state.text = "close"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        textView_log.text = ""
                    }
                }
            }

        })

        button_close.setOnClickListener {
            if (bottomSheetLog.state != BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetLog.state = BottomSheetBehavior.STATE_COLLAPSED
                textView_log.text = ""
            }
        }
    }

    private fun setupFunctionRecyclerView() {

        recyclerView_response.setHasFixedSize(true)
        val linearLayoutMngResponse = LinearLayoutManager(context)
        recyclerView_response.layoutManager = linearLayoutMngResponse


    }

    private fun initView(view: View) {
        buttonConect = view.findViewById(R.id.button_Connect)
        recyclerView = view.findViewById(R.id.recyclerV_funcFrag)
        recyclerView_response = view.findViewById(R.id.recyclerView_response)
        textView_state = view.findViewById(R.id.textView_state)
        switchCompat_sandBox = view.findViewById(R.id.switchCompat_sandBox)
        avLoadingIndicatorView = view.findViewById(R.id.AVLoadingIndicatorView)
        bottom_sheet_log = view.findViewById(R.id.bottom_sheet_log)
        textView_log = view.findViewById(R.id.textView_log)
        appCompatImageView_noResponse = view.findViewById(R.id.appCompatImageView_noResponse)
        txtView_noResponse = view.findViewById(R.id.TxtView_noResponse)
        scrollView_log = view.findViewById(R.id.scrollView_log)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(MainViewModel::class.java)
//            .of(this).get(MainViewModel::class.java)
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
        val position = 19

        changeIconSend(position, ConstantMsgType.GET_HISTORY, uniqueId)
    }

    private fun editMessage() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)

        var callBackMethod: CallBackMethod? = null
        callBackMethod?.method = ConstantMsgType.EDIT_MESSAGE
        funcCallback[uniqueId] = callBackMethod!!


    }

    //TODO ITs not check yet
    private fun deleteMessage() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 17
        changeIconSend(position,ConstantMsgType.DELETE_MESSAGE,uniqueId)
    }

    override fun onBlockList(response: ChatResponse<ResultBlockList>?) {
        super.onBlockList(response)
        val position = 5
        updateMethodList(position, SUCCESSFUL, "", BLOCK_LIST)
        changeIconReceive(position, response!!)
    }

    override fun onError(chatResponse: ErrorOutPut?) {
        super.onError(chatResponse)
        activity?.runOnUiThread {
            Toast.makeText(activity, chatResponse?.errorMessage, Toast.LENGTH_LONG).show()
        }
        val uniqueId = chatResponse?.uniqueId

        if (funcCallback.containsKey(uniqueId)) {
            val position = funcCallback[uniqueId]?.position
            val methodName = funcCallback[uniqueId]?.method
            val errorLog = gson.toJson(chatResponse)

            if (methodName != null && position != null) {
                updateMethodList(position, ERROR, errorLog, methodName)
            }

            activity?.runOnUiThread {
                val viewHolder: RecyclerView.ViewHolder? = recyclerView.findViewHolderForAdapterPosition(position!!)
                viewHolder?.itemView?.findViewById<ProgressBar>(R.id.progress_method)?.visibility = View.GONE

                viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.imgView_log)
                    ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorAccent))
            }
        }

    }

    override fun onUnBlock(response: ChatResponse<ResultBlock>?) {
        super.onUnBlock(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UNBLOCK_CONTACT) {
            val position = 6
            funcCallback.remove(response?.uniqueId)
            updateMethodList(position, SUCCESSFUL, "", UNBLOCK_CONTACT)
            changeIconReceive(position, response!!)
        }
    }

    override fun onGetThread(chatResponse: ChatResponse<ResultThreads>?) {
        super.onGetThread(chatResponse)
        ConstantMsgType.GET_THREAD
        if (funcCallback[chatResponse?.uniqueId]?.method == ConstantMsgType.GET_THREAD) {
            funcCallback.remove(ConstantMsgType.GET_THREAD)
            val position = 4
            changeIconReceive(position, chatResponse!!)
            updateMethodList(position, SUCCESSFUL, "", GET_THREAD)
        }

        if (funcCallback[chatResponse?.uniqueId]?.method == ConstantMsgType.SEND_MESSAGE) {
            val position = 8
            updateMethodList(position, SUCCESSFUL, "", GET_THREAD)

            if (chatResponse?.result?.threads?.size!! > 0) {
                funcCallback.remove(ConstantMsgType.SEND_MESSAGE)
                val threadId = chatResponse.result.threads[0].id
                val requestMessage = RequestMessage.Builder(faker.lorem().paragraph(), threadId).build()

                val uniqueId = mainViewModel.sendTextMsg(requestMessage)
                addToCallBack(ConstantMsgType.SEND_MESSAGE, uniqueId, 8)

            } else {
                val requestGetContact = RequestGetContact.Builder().build()
                funcCallback.remove(ConstantMsgType.SEND_MESSAGE)
                val uniqueId = mainViewModel.getContact(requestGetContact)
                funcCallback[uniqueId]?.method = ConstantMsgType.SEND_MESSAGE
            }
        }
    }

    override fun onDeleteMessage(response: ChatResponse<ResultDeleteMessage>?) {
        super.onDeleteMessage(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.DELETE_MESSAGE) {
            val position = 17
            changeIconReceive(position)
            methods[position].methodNameFlag = true
        }
    }

    override fun onEditedMessage(response: ChatResponse<ResultNewMessage>?) {
        super.onEditedMessage(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.EDIT_MESSAGE) {
            val position = 18
            changeIconReceive(position)
            methods[position].methodNameFlag = true
        }
    }

    override fun onSent(response: ChatResponse<ResultMessage>?) {
        super.onSent(response)

        //TODO create Thread with forward message should changed
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG_ID) {
            val forwardMsgId = response?.result?.messageId
            val contactId = funcCallback[ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG_CONTCT_ID]?.method

            val inviteList = ArrayList<Invitee>()
            inviteList.add(Invitee(contactId!!.toLong(), 1))
            val forwList: MutableList<Long> = mutableListOf()
            forwList.add(forwardMsgId!!)
            val requestThreadInnerMessage = RequestThreadInnerMessage.Builder().message(faker.music().genre())
                .forwardedMessageIds(forwList).build()
            val requestCreateThread: RequestCreateThread =
                RequestCreateThread.Builder(0, inviteList)
                    .message(requestThreadInnerMessage)
                    .build()
            val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)

            funcCallback[uniqueId!![1]]?.method = ConstantMsgType.DELETE_MESSAGE_ID

        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.EDIT_MESSAGE_ID) {
            val requestEditMessage = RequestEditMessage.Builder("this is edit ", response!!.result.messageId).build()
            funcCallback[mainViewModel.editMessage(requestEditMessage)]?.method = ConstantMsgType.EDIT_MESSAGE
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.DELETE_MESSAGE_ID) {
//            val requestDeleteMessage = RequestDeleteMessage.Builder(response!!.result.messageId).build()
//            funcCallback[ConstantMsgType.DELETE_MESSAGE] = mainViewModel.deleteMessage(requestDeleteMessage)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_MESSAGE) {
            val position = 13
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, SEND_MESSAGE)

            val messageId = response?.result?.messageId
            val threadId = response?.result?.conversationId
            val replyMessage =
                RequestReplyMessage.Builder("this is replyMessage", threadId!!, messageId!!).build()

            val uniqueId = mainViewModel.replyMessage(replyMessage)
            addToCallBack(ConstantMsgType.REPLY_MESSAGE_ID, uniqueId, position)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_MESSAGE_ID) {
            val position = 13
            if (response != null) {
                changeIconReceive(position, response)
            }
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, REPLY_MESSAGE)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.FORWARD_MESSAGE) {
            val position = 12
            funcCallback.remove(response?.uniqueId)
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, SEND_MESSAGE)

            val messageIds = ArrayList<Long>()
            messageIds.add(response?.result?.messageId!!)

            val threadId = response.result.conversationId
            val requestForwardMessage = RequestForwardMessage.Builder(threadId, messageIds).build()
            val uniqueId = mainViewModel.forwardMessage(requestForwardMessage)[0]

            addToCallBack(ConstantMsgType.FORWARD_MESSAGE_ID, uniqueId, position)
        }

        //onSent
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_MESSAGE) {
            val position = 8
            changeIconReceive(position, response!!)
            updateMethodList(position, SUCCESSFUL, "", SEND_MESSAGE)
        }
    }

    override fun onSeen(response: ChatResponse<ResultMessage>?) {
        super.onSeen(response)
        val position = 8
        changeFourthIconReceive(position)
        methods[position].funcFourFlag = true
    }

    override fun onDeliver(response: ChatResponse<ResultMessage>?) {
        super.onDeliver(response)
        val position = 8
        changeThirdIconReceive(position)
        methods[position].funcThreeFlag = true
    }

    override fun onNewMessage(response: ChatResponse<ResultNewMessage>?) {
        super.onNewMessage(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.FORWARD_MESSAGE_ID) {

            val position = 12
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, FORWARD_MESSAGE)
            changeIconReceive(position, response!!)
        }
    }

    override fun onLeaveThread(response: ChatResponse<ResultLeaveThread>?) {
        super.onLeaveThread(response)
        val position = 14
        if (response != null) {
            changeIconReceive(position,response)
        }
        updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, LEAVE_THREAD)
    }

    override fun onUpdateContact(response: ChatResponse<ResultUpdateContact>?) {
        super.onUpdateContact(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UPDATE_CONTACT) {
            val position = 7
            changeIconReceive(position, response!!)
        }
    }

    /**
     * OnBlock receive message
     * */
    override fun onBlock(chatResponse: ChatResponse<ResultBlock>?) {
        super.onBlock(chatResponse)
        if (funcCallback[chatResponse?.uniqueId]?.method == ConstantMsgType.BLOCK_CONTACT) {
            val position = 2
            changeIconReceive(position, chatResponse!!)
            methods[position].methodNameFlag = true
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, BLOCK_CONTACT)


            //Un block the contact that has been blocked
            val id = chatResponse?.result?.contact?.id
            if (id != null) {
                val requestUnBlock = RequestUnBlock.Builder(id).build()
                mainViewModel.unBlock(requestUnBlock)
            }
        }
        if (funcCallback[chatResponse?.uniqueId]?.method == ConstantMsgType.UNBLOCK_CONTACT) {
            funcCallback.remove(ConstantMsgType.UNBLOCK_CONTACT)
            val contactId = chatResponse?.result?.contact?.id
            updateMethodList(6, SUCCESSFUL, "", BLOCK_CONTACT)

            if (contactId != null) {
                val requestUnBlock = RequestUnBlock.Builder(contactId).build()
                val uniqueId = mainViewModel.unBlock(requestUnBlock)

                addToCallBack(UNBLOCK_CONTACT, uniqueId, 6)
            }
        }
    }

    override fun onAddContact(response: ChatResponse<ResultAddContact>?) {
        super.onAddContact(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.ADD_CONTACT) {
            val position = 3
            changeIconReceive(position, response!!)
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, ADD_CONTACT)

            val id = response.result?.contact?.id
            if (id != null) {
                val requestRemoveContact = RequestRemoveContact.Builder(id).build()
                mainViewModel.removeContact(requestRemoveContact)
            }
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_CONTACT) {
            val position = 9

            updateMethodList(position, SUCCESSFUL, "", ADD_CONTACT)

            val id = response?.result?.contact?.id
            if (id != null) {
                funcCallback.remove(ConstantMsgType.REMOVE_CONTACT)
                val requestRemoveContact = RequestRemoveContact.Builder(id).build()
                val uniqueId = mainViewModel.removeContact(requestRemoveContact)
                addToCallBack(REMOVE_CONTACT, uniqueId, position)
            }
        }
    }

    private fun updateResponse() {}

    override fun onMuteThread(response: ChatResponse<ResultMute>?) {
        super.onMuteThread(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.MUTE_THREAD) {
            val position = 15
            changeIconReceive(position,response!!)
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG,ConstantMsgType.MUTE_THREAD)
        }
    }

    override fun onUnmuteThread(response: ChatResponse<ResultMute>?) {
        super.onUnmuteThread(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UNMUTE_THREAD) {
            val position = 16
            changeIconReceive(position,response!!)
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG,ConstantMsgType.UNMUTE_THREAD)
        }
    }

    override fun onGetHistory(response: ChatResponse<ResultHistory>?) {
        super.onGetHistory(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.GET_HISTORY) {
            funcCallback.remove(ConstantMsgType.GET_HISTORY)
            val position = 19
            changeIconReceive(position)
        }
    }

    override fun onGetThreadParticipant(response: ChatResponse<ResultParticipant>?) {
        super.onGetThreadParticipant(response)
        val position = 21
        changeIconReceive(position)
    }

    override fun onCreateThread(response: ChatResponse<ResultThread>?) {
        super.onCreateThread(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.GET_PARTICIPANT) {
            val threadId = response!!.result.thread.id
            val requestThreadParticipant = RequestThreadParticipant.Builder(threadId).build()
            mainViewModel.getParticipant(requestThreadParticipant)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG) {
            val threadId = response!!.result.thread.id
            val requestMessage =
                RequestMessage.Builder("this is message for create thread with forward message", threadId)
                    .build()
            val uniqueId = mainViewModel.sendTextMsg(requestMessage)
            funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG_ID
        }

        //onCreateThread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.GET_HISTORY) {
            funcCallback.remove(ConstantMsgType.GET_HISTORY)
            val threadId = response!!.result.thread.id
            val requestGetHistory = RequestGetHistory.Builder(threadId).build()
            val uniqueId = mainViewModel.getHistory(requestGetHistory)
            funcCallback[uniqueId]?.method = ConstantMsgType.GET_HISTORY
        }

        //onCreateThread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UNMUTE_THREAD) {
            funcCallback.remove(ConstantMsgType.UNMUTE_THREAD)
            val threadId = response!!.result.thread.id
            val requestMuteThread = RequestMuteThread.Builder(threadId).build()
            val uniqueId = mainViewModel.unMuteThread(requestMuteThread)
            val position = 16
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG,ConstantMsgType.UNMUTE_THREAD)
            addToCallBack(ConstantMsgType.UNMUTE_THREAD,uniqueId,position)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.MUTE_THREAD) {
            funcCallback.remove(ConstantMsgType.MUTE_THREAD)
            val threadId = response!!.result.thread.id
            val requestMuteThread = RequestMuteThread.Builder(threadId).build()
            val uniqueId = mainViewModel.muteThread(requestMuteThread)
            val position = 15
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG,ConstantMsgType.CREATE_THREAD)
            addToCallBack(ConstantMsgType.MUTE_THREAD,uniqueId,position)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD) {
            val position = 0
            changeIconReceive(position)
            methods[position].methodNameFlag = true
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD_CHANNEL) {
            val position = 0
            changeSecondIconReceive(position)
            methods[position].funcOneFlag = true
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD_CHANNEL_GROUP) {
            val position = 0
            changeThirdIconReceive(position)
            methods[position].funcTwoFlag = true
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD_PUBLIC_GROUP) {
            val position = 0
            changeFourthIconReceive(position)
            methods[position].funcThreeFlag = true
        }

        //Create Thread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.LEAVE_THREAD) {
            val threadId = response?.result?.thread?.id
            val position = 14
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, CREATE_THREAD)
            funcCallback.remove(response?.uniqueId)
            val requeLeaveThread = RequestLeaveThread.Builder(threadId!!.toLong()).build()
            val uniqueId = mainViewModel.leaveThread(requeLeaveThread)
            addToCallBack(ConstantMsgType.LEAVE_THREAD,uniqueId,position)
        }

        //Create Thread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.FORWARD_MESSAGE) {
            val threadId = response?.result?.thread?.id
            val position = 12
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, CREATE_THREAD)

            val requestMessage = RequestMessage.Builder("this is msg for forward", threadId!!).build()
            val uniqueId = mainViewModel.sendTextMsg(requestMessage)
            funcCallback.remove(response?.uniqueId)
            addToCallBack(ConstantMsgType.FORWARD_MESSAGE, uniqueId, position)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_MESSAGE) {
            funcCallback.remove(response?.uniqueId)
            val threadId = response?.result?.thread?.id
            val position = 13
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, CREATE_THREAD)

            val requestMessage = RequestMessage.Builder("send msg for reply", threadId!!).build()
            val uniqueId = mainViewModel.sendTextMsg(requestMessage)
            addToCallBack(ConstantMsgType.REPLY_MESSAGE, uniqueId, position)
        }

        //Create Thread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.ADD_PARTICIPANT) {
            val position = 10
            updateMethodList(position, SUCCESSFUL, "", CREATE_THREAD)
            funcCallback.remove(ConstantMsgType.ADD_PARTICIPANT)
            val participantId = funcCallback[ADD_PARTICIPANT_ID]?.method
            val partId = participantId?.toLong()
            val threadId = response?.result?.thread?.id
            if (partId != null && threadId != null) {
                val contactIdList: MutableList<Long> = mutableListOf()
                contactIdList.add(partId)
                val requestAddParticipants = RequestAddParticipants.Builder(threadId, contactIdList).build()
                val uniqueId = mainViewModel.addParticipant(requestAddParticipants)
                addToCallBack(ADD_PARTICIPANT, uniqueId, position)
                funcCallback.remove("ADD_PARTICIPANT_ID")
            }
        }

        //Create Thread
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_PARTICIPANT) {
            val position = 11
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, CREATE_THREAD)

            funcCallback.remove(ConstantMsgType.REMOVE_PARTICIPANT)
            val participantId = funcCallback[REMOVE_PARTICIPANT_ID]?.method
            val partId = participantId?.toLong()
            val threadId = response?.result?.thread?.id

            if (partId != null && threadId != null) {
                val contactIdList: MutableList<Long> = mutableListOf()
                contactIdList.add(partId)
                val requestAddParticipants = RequestAddParticipants.Builder(threadId, contactIdList).build()
                val uniqueId = mainViewModel.addParticipant(requestAddParticipants)
                addToCallBack(REMOVE_PARTICIPANT, uniqueId, position)
            }
        }
    }


    private fun changeFourthIconReceive(position: Int) {
        activity?.runOnUiThread {
            if (view != null) {
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)!!
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
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)!!
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
                val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)!!
                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFirst)
                    .setImageResource(R.drawable.ic_round_done_all_24px)

                viewHolder.itemView.findViewById<AppCompatImageView>(R.id.imageView_tickFirst)
                    .setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            }
        }
    }

    override fun onRemoveContact(response: ChatResponse<ResultRemoveContact>?) {
        super.onRemoveContact(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_CONTACT) {
            val position = 9
            changeIconReceive(position, response!!)
            updateMethodList(position, SUCCESSFUL, "", REMOVE_CONTACT)
        }
    }

    override fun onGetContact(response: ChatResponse<ResultContact>?) {
        super.onGetContact(response)
        val contactList = response?.result?.contacts

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.GET_PARTICIPANT) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.GET_PARTICIPANT)
            handleGetParticipant(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == CREATE_THREAD_WITH_FORW_MSG) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.GET_HISTORY)
            handleCrtThreadForwMsg(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == GET_HISTORY) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.GET_HISTORY)
            handleGetHistory(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.EDIT_MESSAGE) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.DELETE_MESSAGE)
            handleEditMessage(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.DELETE_MESSAGE) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.DELETE_MESSAGE)
            handleDeleteMessage(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UNMUTE_THREAD) {
            val position = 16
            funcCallback.remove(ConstantMsgType.UNMUTE_THREAD)
            handleUnmuteThread(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.MUTE_THREAD) {
            val position = 15
            funcCallback.remove(ConstantMsgType.MUTE_THREAD)
            handleMuteThread(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.LEAVE_THREAD) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.LEAVE_THREAD)
            handleLeaveThread(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_MESSAGE) {
            val position = 13
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(response?.uniqueId)
            handleReplyMessage(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_MESSAGE) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.SEND_MESSAGE)
            handleSendMessageResponse(contactList)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.CREATE_THREAD) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.CREATE_THREAD)
            handleGetThreadResponse(contactList)
        }

        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.GET_CONTACT) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            methods[position].methodNameFlag = true
            var json = gson.toJson(response?.result)
            methods[position].log = json
            changeIconReceive(position)
        }

        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.BLOCK_CONTACT) {

            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            handleBlockContact(contactList)
        }

        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UPDATE_CONTACT) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.UPDATE_CONTACT)
            handleUpdateContact(contactList)
        }

        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UNBLOCK_CONTACT) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.UNBLOCK_CONTACT)
            handleUnBlockContact(contactList)
        }

        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.ADD_PARTICIPANT) {
            val position = funcCallback[response?.uniqueId]?.position
            updateMethodList(position!!, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.ADD_PARTICIPANT)
            handleAddParticipant(contactList)
        }
        //onGetContact
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_PARTICIPANT) {
            val position = 11
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.REMOVE_PARTICIPANT)
            handleRemoveParticipant(contactList)
        }
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.FORWARD_MESSAGE) {
            val position = 12
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            funcCallback.remove(ConstantMsgType.FORWARD_MESSAGE)
            handleForward(contactList)

        }
    }

    private fun updateMethodList(
        position: Int,
        status: String,
        errorLog: String,
        methodName: String
    ) {

        val functionStatus = FunctionStatus()
        functionStatus.status = status
        functionStatus.methodName = methodName
        functionStatus.errorLog = errorLog
        methods[position].funcStatusList?.add(functionStatus)

    }

    private fun handleGetParticipant(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    if (choose == 2) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))

                        val list = Array(1) { Invitee(inviteList[0].id, 2) }

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, list, "nothing", ""
                            , "", ""
                        )

                        funcCallback[uniqueId]?.method = ConstantMsgType.GET_PARTICIPANT
                        break
                    }
                    choose++
                }
            }
        }
    }

    /**
     * Its created thread and stored another contact id (because its needed tha contact id in order to
     * create another thread) Its sent message(its used as forward message id)/
     * */
    private fun handleCrtThreadForwMsg(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    if (choose == 2) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))

                        val list = Array(1) { Invitee(inviteList[0].id, 2) }

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, list, "nothing", ""
                            , "", ""
                        )

                        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG
                        break
                    }
                    if (choose == 1) {
                        funcCallback[contact.id.toString()]?.method =
                            ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG_CONTCT_ID
                    }
                    choose++
                }
            }
        }
    }

    private fun handleGetHistory(contactList: ArrayList<Contact>?) {

        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    if (choose == 1) {
                        val contactId = contact.id
                        val userId = contact.userId
                        val inviteList = Array<Invitee>(1) { Invitee(contactId, 2) }
                        inviteList[0].id = contactId

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, inviteList, "", ""
                            , "", ""
                        )
                        funcCallback[uniqueId]?.method = ConstantMsgType.GET_HISTORY
                        break
                    }
                    choose++
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
                        val requestThreadInnerMessage =
                            RequestThreadInnerMessage.Builder().message(faker.music().genre()).build()
                        val requestCreateThread: RequestCreateThread =
                            RequestCreateThread.Builder(0, inviteList)
                                .message(requestThreadInnerMessage)
                                .build()
                        val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)

                        val firstUniqueId = uniqueId!![1]
                        funcCallback[firstUniqueId]?.method = ConstantMsgType.DELETE_MESSAGE_ID
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
                        val requestThreadInnerMessage =
                            RequestThreadInnerMessage.Builder().message(faker.music().genre()).build()
                        val requestCreateThread: RequestCreateThread =
                            RequestCreateThread.Builder(0, inviteList)
                                .message(requestThreadInnerMessage)
                                .build()
                        val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                        val firstUniqueId = uniqueId!![1]
                        funcCallback[firstUniqueId]?.method = ConstantMsgType.DELETE_MESSAGE_ID
                    }
                    break
                }
            }
        }
    }

    private fun handleUnmuteThread(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 2) {
                        val contactId = contact.id
                        val userId = contact.userId
                        val inviteList = Array<Invitee>(1, { i -> Invitee(contactId, 5) })
                        inviteList[0].id = contactId
                        val position = 16
                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, inviteList, "", ""
                            , "", ""
                        )
                        addToCallBack(UNMUTE_THREAD,uniqueId,position)
                        break
                    }
                }
            }
        }
    }

    private fun handleMuteThread(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 2) {
                        val contactId = contact.id
                        val position = 15
                        val inviteList = Array<Invitee>(1, { i -> Invitee(contactId, 2) })
                        inviteList[0].id = contactId

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, inviteList, "", ""
                            , "", ""
                        )
                        updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
                        addToCallBack(MUTE_THREAD,uniqueId,position)
                        break
                    }
                }
            }
        }
    }

    private fun handleLeaveThread(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val position = 14


                    val inviteList = Array(1) { Invitee(contactId, 2) }
                    inviteList[0].id = contactId
                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )

                    addToCallBack(ConstantMsgType.LEAVE_THREAD,uniqueId,position)

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

//                    val inviteList = ArrayList<Invitee>()
//                    inviteList.add(Invitee(contactId, 1))
//                    val requestThreadInnerMessage =
//                        RequestThreadInnerMessage.Builder().message(faker.music().genre()).build()
//                    val requestCreateThread: RequestCreateThread =
//                        RequestCreateThread.Builder(0, inviteList)
//                            .message(requestThreadInnerMessage)
//                            .build()
//                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    val inviteList = Array(1) { Invitee(contactId, 2) }
                    inviteList[0].id = contactId
                    val position = 13
                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )
                    addToCallBack(ConstantMsgType.REPLY_MESSAGE, uniqueId, position)
                    break
                }
            }
        }
    }

    override fun onThreadAddParticipant(response: ChatResponse<ResultAddParticipant>?) {
        super.onThreadAddParticipant(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.ADD_PARTICIPANT) {
            val position = 10
            updateMethodList(position, SUCCESSFUL, "", ADD_PARTICIPANT)
            changeIconReceive(position, response!!)
        }
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_PARTICIPANT) {
            val position = 11
            updateMethodList(position, SUCCESSFUL, "", ADD_PARTICIPANT)

            val threadId = response?.result?.thread?.id
            val participantId = funcCallback[REMOVE_PARTICIPANT_ID]?.method
            val mutableList = mutableListOf<Long>()
            mutableList.add(participantId!!.toLong())
            val requestRemoveParticipants =
                RequestRemoveParticipants.Builder(threadId!!, mutableList).build()

            val uniqueId = mainViewModel.removeParticipant(requestRemoveParticipants)
            funcCallback.remove(response.uniqueId)
            addToCallBack(REMOVE_PARTICIPANT, uniqueId, position)
        }
    }

    override fun onThreadRemoveParticipant(response: ChatResponse<ResultParticipant>?) {
        super.onThreadRemoveParticipant(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REMOVE_PARTICIPANT) {
            val position = 11
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, REMOVE_PARTICIPANT)
            if (response != null) {
                changeIconReceive(position, response)
            }
        }

    }

    private fun handleForward(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id
                    val position = 12

                    val inviteList = Array(1) { Invitee(contactId, 2) }
                    inviteList[0].id = contactId

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )
                    addToCallBack(ConstantMsgType.FORWARD_MESSAGE, uniqueId, position)

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

                    val inviteList = Array(1) { Invitee(contactId, 2) }
                    inviteList[0].id = contactId

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )
                    val position = 10
                    addToCallBack(ADD_PARTICIPANT, uniqueId, position)

                    choose++
                    if (choose == 2) {
                        addToCallBack(contactId.toString(), ADD_PARTICIPANT_ID, position)
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
        val uniqueId = mainViewModel.addContacts(requestAddContact)
        val position = 9

        changeIconSend(position, REMOVE_CONTACT, uniqueId)
    }

    private fun handleSendMessageResponse(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))
                    val requestThreadInnerMessage =
                        RequestThreadInnerMessage.Builder().message(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)

                    funcCallback[uniqueId!![0]]?.method = ConstantMsgType.SEND_MESSAGE
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
                    addToCallBack(ConstantMsgType.UNBLOCK_CONTACT, uniqueId, 6)
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
                    addToCallBack(ConstantMsgType.UPDATE_CONTACT, uniqueId, 7)
//                    funcCallback[uniqueId]?.method = ConstantMsgType.UPDATE_CONTACT
//                    changeIconSend(7, ConstantMsgType.UPDATE_CONTACT, uniqueId)
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

//                    funcCallback[uniqueId]?.method = ConstantMsgType.BLOCK_CONTACT
                    addToCallBack(ConstantMsgType.BLOCK_CONTACT, uniqueId, 2)
//                    changeIconSend(2, ConstantMsgType.BLOCK_CONTACT, uniqueId)
                    break
                }
            }
        }
    }

    private fun addToCallBack(methodName: String, uniqueId: String, position: Int) {

        val callBackMethod = CallBackMethod(methodName, position)
        funcCallback[uniqueId] = callBackMethod
    }

    private fun getPartitipant() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()

        funcCallback[mainViewModel.getContact(requestGetContact)]?.method = ConstantMsgType.GET_PARTICIPANT
    }

    //Get Thread
    // If there is no Thread
    // Its create Thread with someone that has userId
    // Then send Message to that thread
    private fun sendTextMsg() {
        val requestThread = RequestThread.Builder().build()

        val uniqueId = mainViewModel.getThread(requestThread)
        changeIconSend(8, ConstantMsgType.SEND_MESSAGE, uniqueId)
    }


    private fun unMuteThread() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 16
        changeIconSend(position, ConstantMsgType.UNMUTE_THREAD, uniqueId)
    }

    private fun muteThread() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 15
        changeIconSend(position, ConstantMsgType.MUTE_THREAD, uniqueId)
    }

    private fun leaveThread() {
        val requestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)

        val position = 14
        changeIconSend(position, ConstantMsgType.LEAVE_THREAD, uniqueId)
    }

    private fun removeParticipant() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        funcCallback[uniqueId]?.method = ConstantMsgType.REMOVE_PARTICIPANT
        val position = 11
        changeIconSend(position, ConstantMsgType.REMOVE_PARTICIPANT, uniqueId)
    }

    private fun blockContact() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 2

        changeIconSend(position, BLOCK_CONTACT, uniqueId)
    }

    private fun addContact() {

        val requestAddContact = RequestAddContact.Builder()
            .cellphoneNumber(faker.phoneNumber()?.phoneNumber())
            .firstName(faker.name()?.firstName())
            .lastName(faker.name()?.lastName())
            .build()
        val uniqueId = mainViewModel.addContacts(requestAddContact)

        val position = 3
        changeIconSend(position, ConstantMsgType.ADD_CONTACT, uniqueId)
    }

    private fun getContact() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        var position = 1

        changeIconSend(position, GET_CONTACT, uniqueId)
    }

    private fun createThread() {
        //get contact
        // search for evey one that has user
        // create thread with that

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD
        changeIconSend(0, CREATE_THREAD, uniqueId)
    }


    private fun createThreadWithForwMessage() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_WITH_FORW_MSG
    }

    private fun createThreadOwnerGroup(inviteList: ArrayList<Invitee>) {

        val list = Array<Invitee>(1) { Invitee(inviteList[0].id, 2) }

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.OWNER_GROUP, list, "nothing", ""
            , "", ""
        )
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_OWNER_GROUP
    }

    private fun createThreadPublicGroup(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1) { Invitee(inviteList[0].id, 2) }

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.PUBLIC_GROUP, list, "nothing", ""
            , "", ""
        )
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_PUBLIC_GROUP
    }

    private fun createThreadChannelGroup(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1, { i -> Invitee(inviteList[0].id, 2) })

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.CHANNEL_GROUP, list, "nothing", ""
            , "", ""
        )
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_CHANNEL_GROUP

    }

    private fun createThreadChannel(inviteList: ArrayList<Invitee>) {
        val list = Array<Invitee>(1, { i -> Invitee(inviteList[0].id, 2) })

        val uniqueId = mainViewModel.createThread(
            ThreadType.Constants.CHANNEL, list, "nothing", ""
            , "", ""
        )
        funcCallback[uniqueId]?.method = ConstantMsgType.CREATE_THREAD_CHANNEL
    }

    private fun handleRemoveParticipant(contactList: ArrayList<Contact>?) {
        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    val position = 11
                    val contactId = contact.id

                    val inviteList = Array(1) { Invitee(contactId, 2) }
                    inviteList[0].id = contactId

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.PUBLIC_GROUP, inviteList, "nothing", ""
                        , "", ""
                    )

                    addToCallBack(REMOVE_PARTICIPANT, uniqueId, position)
                    choose++
                    if (choose == 2) {
                        addToCallBack(contactId.toString(), ConstantMsgType.REMOVE_PARTICIPANT_ID, position)
                        break
                    }
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
                    val requestThreadInnerMessage =
                        RequestThreadInnerMessage.Builder().message(faker.music().genre()).build()
                    val requestCreateThread: RequestCreateThread =
                        RequestCreateThread.Builder(0, inviteList)
                            .message(requestThreadInnerMessage)
                            .build()
                    val uniqueId = mainViewModel.createThreadWithMessage(requestCreateThread)
                    funcCallback[uniqueId!![0]]?.method = ConstantMsgType.CREATE_THREAD

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

        val position = 6
        val requestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        changeIconSend(position, ConstantMsgType.UNBLOCK_CONTACT, uniqueId)
    }

    private fun updateContact() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
//        funcCallback[mainViewModel.getContact(requestGetContact)]?.method = ConstantMsgType.UPDATE_CONTACT
        val position = 7
        changeIconSend(position, UPDATE_CONTACT, uniqueId)
    }

    private fun getBlockList() {
        val requestBlockList = RequestBlockList.Builder().build()
        val uniqueId = mainViewModel.getBlockList(requestBlockList)
        val position = 5
        changeIconSend(position, ConstantMsgType.BLOCK_LIST, uniqueId)
    }

    private fun getThread() {
        val requestThread = RequestThread.Builder().build()
        val uniqueId = mainViewModel.getThread(requestThread)
        val position = 4
        changeIconSend(position, ConstantMsgType.GET_THREAD, uniqueId)
    }

    private fun scroll(position: Int) {
        recyclerViewSmooth.targetPosition = position
        linearLayoutManager.startSmoothScroll(recyclerViewSmooth)
    }

    private fun changeIconReceive(position: Int) {


        activity?.runOnUiThread {
            val viewHolder: RecyclerView.ViewHolder? = recyclerView.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.findViewById<ProgressBar>(R.id.progress_method)?.visibility = View.GONE
            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.imgView_log)
                ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))

//            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.checkBox_ufil)
//                ?.setImageResource(R.drawable.ic_round_done_all_24px)
//            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.checkBox_ufil)
//                ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }
    }

    private fun <T> changeIconReceive(position: Int, log: ChatResponse<T>) {
        methods[position].methodNameFlag = true
        val jsonString = gson.toJson(log)
        methods[position].log = jsonString

        activity?.runOnUiThread {
            val viewHolder: RecyclerView.ViewHolder? = recyclerView.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.findViewById<ProgressBar>(R.id.progress_method)?.visibility = View.GONE
            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.imgView_log)
                ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))

//            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.checkBox_ufil)
//                ?.setImageResource(R.drawable.ic_round_done_all_24px)
//            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.checkBox_ufil)
//                ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
        }
    }

    /* visibility of progress bar*/
    private fun changeIconSend(position: Int, methodName: String, uniqueId: String) {
        val callBackMethod = CallBackMethod(methodName, position)
        funcCallback[uniqueId] = callBackMethod

        activity?.runOnUiThread {

            val viewHolder: RecyclerView.ViewHolder = recyclerView.findViewHolderForAdapterPosition(position)!!
            viewHolder.itemView.findViewById<ProgressBar>(R.id.progress_method).visibility = View.VISIBLE

        }
    }
/*
* getContact
*choose one of the contact and create thread with that TYPE_PUBLIC_GROUP
 *choose another to add as a participant
* */

    private fun addParticipant() {
        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 10
        changeIconSend(position, ConstantMsgType.ADD_PARTICIPANT, uniqueId)
    }

    //TODO its not working
    private fun forwardMessage() {

        val requestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 12
        changeIconSend(position, ConstantMsgType.FORWARD_MESSAGE, uniqueId)
    }

    private fun replyMessage() {
        val requestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 13
        changeIconSend(position, ConstantMsgType.REPLY_MESSAGE, uniqueId)
    }

}

