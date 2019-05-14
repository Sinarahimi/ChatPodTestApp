package ir.fanap.chattestapp.application.ui.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.fanap.chattestapp.R
import android.animation.Animator
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.design.circularreveal.CircularRevealCompat
import android.support.design.circularreveal.cardview.CircularRevealCardView
import android.support.v4.content.ContextCompat
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.fanap.podchat.ProgressHandler
import com.fanap.podchat.mainmodel.Contact
import com.fanap.podchat.mainmodel.Invitee
import com.fanap.podchat.mainmodel.RequestThreadInnerMessage
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import com.fanap.podchat.util.ThreadType
import com.github.javafaker.Faker
import ir.fanap.chattestapp.application.ui.MainViewModel
import ir.fanap.chattestapp.application.ui.TestListener
import ir.fanap.chattestapp.application.ui.util.ConstantMsgType
import java.util.ArrayList

class ChatFragment : Fragment(), TestListener {
    private lateinit var atach_file: AppCompatImageView
    private lateinit var mainViewModel: MainViewModel
    private var fucCallback: HashMap<String, String> = hashMapOf()
    private val REQUEST_TAKE_PHOTO = 0
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    private lateinit var contextFrag: Context
    private var imageUrl: Uri? = null
    private lateinit var txtViewFileMsg: TextView
    private lateinit var txtViewUploadFile: TextView
    private lateinit var txtViewUploadImage: TextView
    private lateinit var txtViewReplyFileMsg: TextView
    private lateinit var imageView_tickOne: AppCompatImageView
    private lateinit var imageView_tickTwo: AppCompatImageView
    private lateinit var imageView_tickThree: AppCompatImageView
    private lateinit var imageView_tickFour: AppCompatImageView
    private lateinit var prgressbarUploadImg: ProgressBar
    private lateinit var buttonUploadImage: AppCompatImageView


    private val faker: Faker = Faker()

    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        contextFrag = context!!
    }

    /*private <T extends View & CircularRevealWidget> void circularRevealFromMiddle(@NonNull final T circularRevealWidget) {
    circularRevealWidget.post(new Runnable() {
        @Override
        public void run() {
            int viewWidth = circularRevealWidget.getWidth();
            int viewHeight = circularRevealWidget.getHeight();

            int viewDiagonal = (int) Math.sqrt(viewWidth * viewWidth + viewHeight * viewHeight);

            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(
                    CircularRevealCompat.createCircularReveal(circularRevealWidget, viewWidth / 2, viewHeight / 2, 10, viewDiagonal / 2),
                    ObjectAnimator.ofArgb(circularRevealWidget, CircularRevealWidget.CircularRevealScrimColorProperty.CIRCULAR_REVEAL_SCRIM_COLOR, Color.RED, Color.TRANSPARENT));

            animatorSet.setDuration(5000);
            animatorSet.start();
        }
    });
}*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val cicuralCard: CircularRevealCardView = view.findViewById(R.id.ccv_attachment_reveal)
        val appCompatImageView_gallery: AppCompatImageView = view.findViewById(R.id.appCompatImageView_gallery)
        imageView_tickOne = view.findViewById(R.id.checkBox_Send_File_Msg)
        imageView_tickTwo = view.findViewById(R.id.checkBox_Upload_File)
        imageView_tickThree = view.findViewById(R.id.checkBox_Reply_File_Msg)
        imageView_tickFour = view.findViewById(R.id.checkBox_ufil)

        buttonUploadImage = view.findViewById(R.id.buttonUploadImage)

        txtViewFileMsg = view.findViewById(R.id.TxtViewFileMsg)
        txtViewUploadFile = view.findViewById(R.id.TxtViewUploadFile)
        txtViewUploadImage = view.findViewById(R.id.TxtViewUploadImage)
        txtViewReplyFileMsg = view.findViewById(R.id.TxtViewReplyFileMsg)

        prgressbarUploadImg = view.findViewById(R.id.progress_UploadImage)

        txtViewFileMsg.setOnClickListener { fileMsg() }
        txtViewUploadFile.setOnClickListener { uploadFile() }
        buttonUploadImage.setOnClickListener {
            //            uploadImage()
            uploadImageProgress()
        }
        txtViewReplyFileMsg.setOnClickListener { replyFileMsg() }


        val appCmpImgViewFolder: AppCompatImageView = view.findViewById(R.id.appCompatImageView_folder)

        appCompatImageView_gallery.setOnClickListener {
            selectImageInAlbum()
        }
        var isOpen = false
        atach_file = view.findViewById(R.id.atach_file)
        atach_file.setOnClickListener {

            if (!isOpen) {

                val x = cicuralCard.left
                val y = cicuralCard.bottom

                val startRadius = 0
                val endRadius = Math.hypot(cicuralCard.width.toDouble(), cicuralCard.height.toDouble())

                val anim: Animator =
                    CircularRevealCompat.createCircularReveal(
                        cicuralCard,
                        x.toFloat(),
                        y.toFloat(),
                        startRadius.toFloat(),
                        endRadius.toFloat()
                    )
                anim.interpolator = AccelerateDecelerateInterpolator()
//                cicuralCard.visibility = View.VISIBLE
                anim.duration = 50000
                anim.start()

                isOpen = true

            } else {

                val x = cicuralCard.left
                val y = cicuralCard.bottom

                val endRadius = Math.max(cicuralCard.width, cicuralCard.height)
                val startRadius = 0


                val anim: Animator =
                    CircularRevealCompat.createCircularReveal(
                        cicuralCard,
                        x.toFloat(),
                        y.toFloat(),
                        startRadius.toFloat(),
                        endRadius.toFloat()
                    )
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator?) {

                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
//                        cicuralCard.visibility = View.GONE
                    }
                })
                anim.interpolator = AccelerateDecelerateInterpolator()
                anim.duration = 50000
                anim.start()
                isOpen = false
            }
        }
        return view
    }

    override fun onSent(response: ChatResponse<ResultMessage>?) {
        super.onSent(response)
        if (fucCallback[ConstantMsgType.REPLY_MESSAGE_ID] == response?.uniqueId) {
            val messageId = response?.result?.messageId
            val threadId = fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID]
            val replyFileMessage = RequestReplyFileMessage
                .Builder("this is replyMessage", threadId?.toLong()!!, messageId!!, imageUrl, activity)
                .build()
            fucCallback[ConstantMsgType.REPLY_FILE_MESSAGE] = mainViewModel
                .replyWithFile(replyFileMessage, object : ProgressHandler.sendFileMessage {
                    override fun onFinishImage(json: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                        super.onFinishImage(json, chatResponse)
                        imageView_tickFour.setImageResource(R.drawable.ic_round_done_all_24px)
                        imageView_tickFour.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
                    }

                    override fun onProgressUpdate(
                        uniqueId: String?,
                        bytesSent: Int,
                        totalBytesSent: Int,
                        totalBytesToSend: Int
                    ) {
                        super.onProgressUpdate(uniqueId, bytesSent, totalBytesSent, totalBytesToSend)

                    }
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(MainViewModel::class.java)

        mainViewModel.setTestListener(this)
    }

    override fun onGetContact(response: ChatResponse<ResultContact>?) {
        super.onGetContact(response)
        if (fucCallback[ConstantMsgType.SEND_FILE_MESSAGE] == response?.uniqueId) {
            handleSendFileMsg(response!!.result.contacts)
        }

        if (fucCallback[ConstantMsgType.REPLY_FILE_MESSAGE] == response?.uniqueId) {
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
        if (fucCallback[ConstantMsgType.SEND_FILE_MESSAGE] == response?.uniqueId) {
            val requestFileMessage = RequestFileMessage.Builder(activity, response!!.result.thread.id, imageUrl).build()
            mainViewModel.sendFileMessage(requestFileMessage, object : ProgressHandler.sendFileMessage {
                override fun onFinishImage(json: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                    super.onFinishImage(json, chatResponse)
                    imageView_tickOne.setImageResource(R.drawable.ic_round_done_all_24px)
                    imageView_tickOne.setColorFilter(ContextCompat.getColor(activity!!, R.color.colorPrimary))
                }
            })
        }

        if ((fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID] == response?.uniqueId)) {
            val threadId = response?.result?.thread?.id
            fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID] = threadId.toString()
        }
    }

    private fun handleReplyFileMessage(contactList: ArrayList<Contact>) {
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
                        fucCallback[ConstantMsgType.REPLY_MESSAGE_THREAD_ID] = uniqueId!![0]
                        fucCallback[ConstantMsgType.REPLY_MESSAGE_ID] = uniqueId[1]
                    }
                    break
                }
            }
        }
    }

    private fun handleSendFileMsg(contactList: ArrayList<Contact>) {

        if (contactList != null) {
            var choose = 0
            for (contact: Contact in contactList) {
                if (contact.isHasUser) {
                    choose++
                    if (choose == 1) {
                        val contactId = contact.id

                        val inviteList = ArrayList<Invitee>()
                        inviteList.add(Invitee(contactId, 1))

                        val list = Array(1) { Invitee(inviteList[0].id, 2) }

                        val uniqueId = mainViewModel.createThread(
                            ThreadType.Constants.NORMAL, list, "nothing", ""
                            , "", ""
                        )
                        fucCallback[ConstantMsgType.SEND_FILE_MESSAGE] = uniqueId
                    }
                    break
                }
            }
        }
    }

    private fun fileMsg() {

        val requestGetContact: RequestGetContact = RequestGetContact.Builder().build()
        val uniqueId = mainViewModel.getContact(requestGetContact)
        fucCallback[ConstantMsgType.SEND_FILE_MESSAGE] = uniqueId
//            mainViewModel.sendFileMessage()

    }

    fun uploadFile() {
        if (imageUrl != null) {

            val requestUploadFile = RequestUploadFile.Builder(activity, imageUrl).build()
            mainViewModel.uploadFile(requestUploadFile)
        }
    }

    //Chat needs update
    fun uploadImage() {
        if (!imageUrl.toString().isEmpty()) {
            mainViewModel.uploadImage(activity, imageUrl!!)
        }
    }

    fun uploadImageProgress() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            prgressbarUploadImg.setProgress(100, true)
//        }


        if (!imageUrl.toString().isEmpty()) {
//            prgressbarUploadImg.max = 100
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                prgressbarUploadImg.setProgress(10, true)
//            }
            prgressbarUploadImg.incrementProgressBy(10)
            mainViewModel.uploadImageProgress(contextFrag, activity, imageUrl, object : ProgressHandler.onProgress {
                override fun onProgressUpdate(
                    uniqueId: String?,
                    bytesSent: Int,
                    totalBytesSent: Int,
                    totalBytesToSend: Int
                ) {
                    super.onProgressUpdate(uniqueId, bytesSent, totalBytesSent, totalBytesToSend)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        prgressbarUploadImg.setProgress(bytesSent, true)
//                    }
                    prgressbarUploadImg.incrementProgressBy(bytesSent)
                }

                override fun onFinish(imageJson: String?, chatResponse: ChatResponse<ResultImageFile>?) {
                    super.onFinish(imageJson, chatResponse)
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        prgressbarUploadImg.setProgress(50, true)
//                    }
                    prgressbarUploadImg.incrementProgressBy(100)

                }
            })
        }
    }

    fun replyFileMsg() {
        val requestGetContact = RequestGetContact.Builder().build()
        fucCallback[ConstantMsgType.REPLY_FILE_MESSAGE] = mainViewModel.getContact(requestGetContact)
    }

    fun selectImageInAlbum() {
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

