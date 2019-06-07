package ir.fanap.chattestapp.application.ui.upload

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.fanap.chattestapp.R
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.fanap.podchat.ProgressHandler
import com.fanap.podchat.mainmodel.Contact
import com.fanap.podchat.mainmodel.Invitee
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import com.fanap.podchat.util.ThreadType
import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.application.ui.function.ResponseAdapter
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.CREATE_THREAD
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.EMPTY_ERROR_LOG
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.GET_CONTACT
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REPLY_FILE_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.REPLY_FILE_MESSAGE_UPLOADED
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.SEND_FILE_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.SEND_MESSAGE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.SUCCESSFUL
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.UPLOAD_FILE
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType.Companion.UPLOAD_IMAGE
import ir.fanap.chattestapp.application.ui.util.UploadList
import ir.fanap.chattestapp.bussines.model.CallBackMethod
import ir.fanap.chattestapp.bussines.model.FunctionStatus
import ir.fanap.chattestapp.bussines.model.Method
import kotlinx.android.synthetic.main.bottom_sheet_log.*
import java.util.ArrayList

class UploadFragment : Fragment(), UploadListener, UploadAdapter.UploadViewHListener {

    private lateinit var atach_file: AppCompatImageView
    private lateinit var mainViewModel: MainViewModel
    private val REQUEST_TAKE_PHOTO = 0
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    private lateinit var contextFrag: Context
    private var imageUrl: Uri? = null
    private var methods: MutableList<Method> = mutableListOf()
    private lateinit var subMethods: MutableList<FunctionStatus>
    private lateinit var bottom_sheet_log: ConstraintLayout
    private lateinit var bottomSheetLog: BottomSheetBehavior<ConstraintLayout>
    private lateinit var textView_log: TextView
    private var funcCallback: HashMap<String, CallBackMethod> = hashMapOf()
    private var chatStates: Boolean? = false
    private var gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private lateinit var txtView_noResponse: TextView

    private lateinit var recyclerViewResponse: RecyclerView
    private lateinit var recyclerViewUpload: RecyclerView
    private val faker: Faker = Faker()

    companion object {
        fun newInstance(): UploadFragment {
            return UploadFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contextFrag = context!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = activity.run { ViewModelProviders.of(this!!).get(MainViewModel::class.java) }
        mainViewModel.setUploadListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        val appCompatImageView_gallery: AppCompatImageView = view.findViewById(R.id.appCompatImageView_gallery)

//        mainViewModel.chatStateObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                if (it.equals("CHAT_READY")) {
//                    chatStates = true
//                }
//            }
        initView(view)
        val buttonClose = view.findViewById(R.id.button_close) as Button

        bottomSheetSetup(buttonClose)

        setupUploadRecyclerView(view)
        setupSubMethodRecyclerView(view)

        val appCmpImgViewFolder: AppCompatImageView = view.findViewById(R.id.appCompatImageView_folder)
        appCompatImageView_gallery.setOnClickListener {
            selectImageInAlbum()
        }
        return view
    }

    private fun setupSubMethodRecyclerView(view: View) {
        recyclerViewResponse = view.findViewById(R.id.recyclerView_response)

        recyclerViewResponse.setHasFixedSize(true)
        val linearLayoutMngResponse = LinearLayoutManager(context)
        recyclerViewResponse.layoutManager = linearLayoutMngResponse
    }

    private fun setupUploadRecyclerView(view: View) {

        for (i in 0..3) {
            val method = Method()
            val mutableList = arrayListOf<FunctionStatus>()
            method.methodName = UploadList.methodNames[i]
            method.funcOne = UploadList.methodFuncOne[i]
            method.funcTwo = UploadList.methodFuncTwo[i]
            method.funcThree = UploadList.methodFuncThree[i]
            method.funcFour = UploadList.methodFuncFour[i]
            val functionStatus = FunctionStatus()
            functionStatus.errorLog = ""
            functionStatus.methodName = ""
            functionStatus.status = ""
            method.funcStatusList = mutableList
            methods.add(method)
        }

        val uploadAdapter = UploadAdapter(this.activity!!, methods, this)
        recyclerViewUpload.adapter = uploadAdapter
        recyclerViewUpload.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerViewUpload.layoutManager = linearLayoutManager
    }

    override fun onUploadFile(response: ChatResponse<ResultFile>?) {
        super.onUploadFile(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.UPLOAD_FILE) {
            funcCallback.remove(response?.uniqueId)
            val position = 2
            changeIconReceive(position, response!!)
        }
    }

    override fun onUploadImageFile(response: ChatResponse<ResultImageFile>?) {
        super.onUploadImageFile(response)

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_FILE_MESSAGE) {

        }

        if (funcCallback[response?.uniqueId]?.method == UPLOAD_IMAGE) {
            funcCallback.remove(response?.uniqueId)
            val position = 3
            changeIconReceive(position, response!!)
        }
    }

    override fun onIconClicked(clickedViewHolder: UploadAdapter.ViewHolderUpload) {
        val position = clickedViewHolder.adapterPosition
        when (position) {
            0 -> {
                sendFileMessage()
            }
            1 -> {
                sendReplyFileMessage()
            }
            2 -> {
                uploadFile()
            }
            3 -> {
                uploadImage()
            }
        }
    }

    override fun onLogClicked(clickedViewHolder: UploadAdapter.ViewHolderUpload) {
        val position = clickedViewHolder.adapterPosition

        bottomSheetLog.state = BottomSheetBehavior.STATE_EXPANDED

        subMethods = methods[position].funcStatusList!!
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
///
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

    private fun initView(view: View) {
        bottom_sheet_log = view.findViewById(R.id.bottom_sheet_log)
        textView_log = view.findViewById(R.id.textView_log)
        recyclerViewUpload = view.findViewById(R.id.recyclerV_upload)
        txtView_noResponse = view.findViewById(R.id.TxtView_noResponse)
    }

    override fun onSent(response: ChatResponse<ResultMessage>?) {
        super.onSent(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_FILE_MESSAGE) {
            funcCallback.remove(response?.uniqueId)
            val position = 0
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG,SEND_FILE_MESSAGE)
            changeIconReceive(position, response!!)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_FILE_MESSAGE_UPLOADED) {
            funcCallback.remove(response?.uniqueId)
            val position = 1
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, REPLY_FILE_MESSAGE)
            changeIconReceive(position, response!!)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_FILE_MESSAGE) {
            val position = 1
            funcCallback.remove(response?.uniqueId)
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, SEND_MESSAGE)
            val requestFileMessage = RequestReplyFileMessage
                .Builder(
                    "This is reply", response?.result?.conversationId!!
                    , response.result.messageId
                    , imageUrl, activity
                )
                .build()
            val uniqueId = mainViewModel.replyFileMessage(requestFileMessage, object : ProgressHandler.sendFileMessage {
                override fun onFinishImage(json: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                    super.onFinishImage(json, chatResponse)

                }
            })
            addToCallBack(REPLY_FILE_MESSAGE_UPLOADED, uniqueId, position)
        }
    }

    override fun onGetContact(response: ChatResponse<ResultContact>?) {
        super.onGetContact(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_FILE_MESSAGE) {
            val position = 0
            updateMethodList(
                position,
                ConstantMsgType.SUCCESSFUL,
                ConstantMsgType.EMPTY_ERROR_LOG,
                ConstantMsgType.GET_CONTACT
            )
            handleSendFileMsg(response!!.result.contacts)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_FILE_MESSAGE) {
            val position = 1
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, GET_CONTACT)
            handleReplyFileMessage(response!!.result.contacts)
        }
    }

    override fun onError(chatResponse: ErrorOutPut?) {
        super.onError(chatResponse)
        activity?.runOnUiThread {
            Toast.makeText(activity, chatResponse?.errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateThread(response: ChatResponse<ResultThread>?) {
        super.onCreateThread(response)
        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.SEND_FILE_MESSAGE) {
            val position = 0
            updateMethodList(
                position,
                ConstantMsgType.SUCCESSFUL,
                ConstantMsgType.EMPTY_ERROR_LOG,
                ConstantMsgType.CREATE_THREAD
            )
            val requestFileMessage =
                RequestFileMessage.Builder(activity, response!!.result.thread.id, imageUrl).build()
            val uniqueId = mainViewModel.sendFileMessage(requestFileMessage, object : ProgressHandler.sendFileMessage {
                override fun onFinishImage(json: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                    super.onFinishImage(json, chatResponse)
                    changeIconReceive(position, chatResponse!!)
                    updateMethodList(
                        position,
                        ConstantMsgType.SUCCESSFUL,
                        ConstantMsgType.EMPTY_ERROR_LOG,
                        ConstantMsgType.UPLOAD_IMAGE
                    )
                }
            })
            addToCallBack(ConstantMsgType.SEND_FILE_MESSAGE, uniqueId, position)
        }

        if (funcCallback[response?.uniqueId]?.method == ConstantMsgType.REPLY_FILE_MESSAGE) {
            val position = 1
            updateMethodList(position, SUCCESSFUL, EMPTY_ERROR_LOG, CREATE_THREAD)
            funcCallback.remove(response?.uniqueId)
            val threadId = response?.result?.thread?.id
            val requestMessage =
                RequestMessage.Builder("this is message for test reply file message", threadId!!)
                    .build()
            val uniqueId = mainViewModel.sendTextMsg(requestMessage)
            addToCallBack(ConstantMsgType.REPLY_FILE_MESSAGE, uniqueId, position)
        }
    }

    override fun onGetThread(chatResponse: ChatResponse<ResultThreads>?) {
        super.onGetThread(chatResponse)
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

    /* visibility of progress bar*/
    private fun changeIconSend(position: Int, methodName: String, uniqueId: String) {
        val callBackMethod = CallBackMethod(methodName, position)
        funcCallback[uniqueId] = callBackMethod
        methods[position].pending = true

        activity?.runOnUiThread {

            val viewHolder: RecyclerView.ViewHolder =
                recyclerViewUpload.findViewHolderForAdapterPosition(position)!!
            viewHolder.itemView.findViewById<ProgressBar>(R.id.progress_method).visibility = View.VISIBLE
        }
    }

    private fun <T> changeIconReceive(position: Int, log: ChatResponse<T>) {
        methods[position].methodNameFlag = true
        methods[position].pending = false
        val jsonString = gson.toJson(log)
        methods[position].log = jsonString

        activity?.runOnUiThread {
            val viewHolder: RecyclerView.ViewHolder? = recyclerViewUpload.findViewHolderForAdapterPosition(position)
            viewHolder?.itemView?.findViewById<ProgressBar>(R.id.progress_method)?.visibility = View.GONE
            viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.imgView_log)
                ?.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))

        }
    }

    private fun addToCallBack(methodName: String, uniqueId: String, position: Int) {

        val callBackMethod = CallBackMethod(methodName, position)
        funcCallback[uniqueId] = callBackMethod
    }

    //Get Contact
    private fun handleReplyFileMessage(contactList: ArrayList<Contact>) {
        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {

                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))

                    val list = Array(1) { Invitee(inviteList[0].id, 2) }

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.NORMAL, list, "nothing", ""
                        , "", ""
                    )
                    val position = 1
                    addToCallBack(REPLY_FILE_MESSAGE, uniqueId, position)
                    break
                }
            }
        }
    }

    private fun handleSendFileMsg(contactList: ArrayList<Contact>) {

        if (contactList != null) {
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {

                    val contactId = contact.id

                    val inviteList = ArrayList<Invitee>()
                    inviteList.add(Invitee(contactId, 1))

                    val list = Array(1) { Invitee(inviteList[0].id, 2) }

                    val uniqueId = mainViewModel.createThread(
                        ThreadType.Constants.NORMAL, list, "nothing", ""
                        , "", ""
                    )
                    val position = 0
                    addToCallBack(ConstantMsgType.SEND_FILE_MESSAGE, uniqueId, position)
                    break
                }
            }
        }
    }

    private fun sendFileMessage() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 0
        changeIconSend(position, ConstantMsgType.SEND_FILE_MESSAGE, uniqueId)
    }

    private fun uploadFile() {
        if (imageUrl != null) {

            val requestUploadFile = RequestUploadFile.Builder(activity, imageUrl).build()
            val uniqueId = mainViewModel.uploadFile(requestUploadFile)
            val position = 2
            changeIconSend(position, UPLOAD_FILE, uniqueId)
        }
    }

    //Chat needs update
    private fun uploadImage() {
        if (!imageUrl.toString().isEmpty()) {
            val uniqueId = mainViewModel.uploadImage(activity, imageUrl!!)
            val position = 3
            changeIconSend(position, UPLOAD_IMAGE, uniqueId)
        }
    }

    private fun uploadImageProgress() {

        if (!imageUrl.toString().isEmpty()) {

            mainViewModel.uploadImageProgress(contextFrag, activity, imageUrl, object : ProgressHandler.onProgress {
                override fun onProgressUpdate(
                    uniqueId: String?,
                    bytesSent: Int,
                    totalBytesSent: Int,
                    totalBytesToSend: Int
                ) {
                    super.onProgressUpdate(uniqueId, bytesSent, totalBytesSent, totalBytesToSend)
                    activity?.runOnUiThread {
                        //                            textView_progress_UploadImage.text = bytesSent.toString()
                    }
                }

                override fun onFinish(imageJson: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                    super.onFinish(imageJson, chatResponse)
                    activity?.runOnUiThread {
                        //                            textView_progress_UploadImage.text = "100 % "
//                            textView_progress_UploadImage.setTextColor(
                        ContextCompat.getColor(
                            activity!!,
                            R.color.green_active
                        )

                    }
                }
            })
        }
    }

    private fun sendReplyFileMessage() {
        val requestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        val position = 1
        changeIconSend(position, ConstantMsgType.REPLY_FILE_MESSAGE, uniqueId)
    }

    private fun selectImageInAlbum() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (REQUEST_SELECT_IMAGE_IN_ALBUM == requestCode) {
                imageUrl = data.data
            }
        }
    }
}

