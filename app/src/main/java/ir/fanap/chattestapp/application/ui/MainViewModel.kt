package ir.fanap.chattestapp.application.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.fanap.podchat.ProgressHandler
import com.fanap.podchat.chat.Chat
import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.mainmodel.Invitee
import com.fanap.podchat.mainmodel.ResultDeleteMessage
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import rx.subjects.PublishSubject
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var chat: Chat = Chat.init(application)
    private lateinit var testListener: TestListener
    var observable: PublishSubject<String> = PublishSubject.create()
    var observableLog: PublishSubject<String> = PublishSubject.create()

    init {
        chat.isLoggable(true)
        chat.addListener(object : ChatListener {
            override fun onChatState(state: String?) {
                super.onChatState(state)
                observable.onNext(state)
            }

            override fun onGetThreadParticipant(content: String?, response: ChatResponse<ResultParticipant>?) {
                super.onGetThreadParticipant(content, response)
                testListener.onGetThreadParticipant(response)
            }

            override fun onNewMessage(content: String?, response: ChatResponse<ResultNewMessage>?) {
                super.onNewMessage(content, response)
                testListener.onNewMessage(response)

            }

            override fun onError(content: String?, OutPutError: ErrorOutPut?) {
                super.onError(content, OutPutError)
                testListener.onError(OutPutError)
            }

            override fun onCreateThread(content: String?, response: ChatResponse<ResultThread>?) {
                super.onCreateThread(content, response)
                testListener.onCreateThread(response)
            }

            override fun onContactAdded(content: String?, response: ChatResponse<ResultAddContact>?) {
                super.onContactAdded(content, response)
                testListener.onAddContact(response)
            }

            override fun onRemoveContact(content: String?, response: ChatResponse<ResultRemoveContact>?) {
                super.onRemoveContact(content, response)
                testListener.onRemoveContact(response)
            }

            override fun onBlock(content: String?, response: ChatResponse<ResultBlock>?) {
                super.onBlock(content, response)
                testListener.onBlock(response)
            }

            override fun onGetContacts(content: String?, response: ChatResponse<ResultContact>?) {
                super.onGetContacts(content, response)
                testListener.onGetContact(response)
            }

            override fun onGetBlockList(content: String?, response: ChatResponse<ResultBlockList>?) {
                super.onGetBlockList(content, response)
                testListener.onBlockList(response)
            }

            override fun OnLogEvent(log: String) {
                super.OnLogEvent(log)
                testListener.onLogEvent(log)
            }

            override fun onUpdateContact(content: String?, response: ChatResponse<ResultUpdateContact>?) {
                super.onUpdateContact(content, response)
                testListener.onUpdateContact(response)
            }

            override fun onUnBlock(content: String?, response: ChatResponse<ResultBlock>?) {
                super.onUnBlock(content, response)
                testListener.onUnBlock(response)
            }

            override fun onSent(content: String?, response: ChatResponse<ResultMessage>?) {
                super.onSent(content, response)
                testListener.onSent(response)
            }

            override fun onGetThread(content: String?, thread: ChatResponse<ResultThreads>?) {
                super.onGetThread(content, thread)
                testListener.onGetThread(thread)
            }

            override fun onSeen(content: String?, response: ChatResponse<ResultMessage>?) {
                super.onSeen(content, response)
                testListener.onSeen(response)
            }

            override fun onDeliver(content: String?, response: ChatResponse<ResultMessage>?) {
                super.onDeliver(content, response)
                testListener.onDeliver(response)
            }

            override fun onThreadRemoveParticipant(content: String?, response: ChatResponse<ResultParticipant>?) {
                super.onThreadRemoveParticipant(content, response)
                testListener.onThreadRemoveParticipant(response)
            }

            override fun onThreadAddParticipant(content: String?, response: ChatResponse<ResultAddParticipant>?) {
                super.onThreadAddParticipant(content, response)
                testListener.onThreadAddParticipant(response)
            }

            override fun onThreadLeaveParticipant(content: String?, response: ChatResponse<ResultLeaveThread>?) {
                super.onThreadLeaveParticipant(content, response)
                testListener.onLeaveThread(response)
            }

            override fun onMuteThread(content: String?, response: ChatResponse<ResultMute>?) {
                super.onMuteThread(content, response)
                testListener.onMuteThread(response)
            }

            override fun onUnmuteThread(content: String?, response: ChatResponse<ResultMute>?) {
                super.onUnmuteThread(content, response)
                testListener.onUnmuteThread(response)
            }

            override fun onDeleteMessage(content: String?, response: ChatResponse<ResultDeleteMessage>?) {
                super.onDeleteMessage(content, response)
                testListener.onDeleteMessage(response)
            }

            override fun onEditedMessage(content: String?, response: ChatResponse<ResultNewMessage>?) {
                super.onEditedMessage(content, response)
                testListener.onEditedMessage(response)
            }

            override fun onGetHistory(content: String?, response: ChatResponse<ResultHistory>?) {
                super.onGetHistory(content, response)
                testListener.onGetHistory(response)
            }

        })
    }

    fun setTestListener(testListener: TestListener) {
        this.testListener = testListener
    }

    fun connect(
        socketAddress: String, appId: String, severName: String, token: String,
        ssoHost: String, platformHost: String, fileServer: String, typeCode: String?
    ) {
        chat.connect(socketAddress, appId, severName, token, ssoHost, platformHost, fileServer, typeCode)
        chat.addListener(object : ChatListener {
            override fun onUserInfo(content: String?, response: ChatResponse<ResultUserInfo>?) {
                super.onUserInfo(content, response)
            }
        })
    }

    fun uploadFile(requestUploadFile: RequestUploadFile): String {
        return chat.uploadFile(requestUploadFile)
    }

    fun uploadImage(activity: FragmentActivity?, uri: Uri?): String {
        return chat.uploadImage(activity, uri)
    }

    fun uploadImageProgress(
        context: Context,
        activity: FragmentActivity?,
        uri: Uri?,
        progress: ProgressHandler.onProgress
    ): String {
        return chat.uploadImageProgress(context, activity, uri, progress)
    }

    fun sendFileMessage(requestFileMessage: RequestFileMessage, objects: ProgressHandler.sendFileMessage): String {
        return chat.sendFileMessage(requestFileMessage, objects)
    }

    fun replyWithFile(requestReplyMessage: RequestReplyFileMessage, objects: ProgressHandler.sendFileMessage): String {
        return chat.replyFileMessage(requestReplyMessage, objects)
    }

    fun getHistory(requestGetHistory: RequestGetHistory): String {
        return chat.getHistory(requestGetHistory, null)
    }

    fun editMessage(requestEditMessage: RequestEditMessage): String {
        return chat.editMessage(requestEditMessage, null)
    }

    fun muteThread(requestMuteThread: RequestMuteThread): String {
        return chat.muteThread(requestMuteThread, null)
    }

    fun unMuteThread(requestMuteThread: RequestMuteThread): String {
        return chat.unMuteThread(requestMuteThread, null)
    }

    //    * createThreadTypes = {
//        *         NORMAL: 0,
//        *         OWNER_GROUP: 1,
//        *         PUBLIC_GROUP: 2,
//        *         CHANNEL_GROUP: 4,
//        *         CHANNEL: 8
//        *       }
//    */
//
//    int threadType, Invitee[] invitee, String threadTitle, String description, String image
//    , String metadata, ChatHandler handler
    fun createThread(
        threadType: Int,
        invitee: Array<Invitee>,
        threadTitle: String,
        description: String,
        image: String,
        metadata: String
    ): String {
        return chat.createThread(threadType, invitee, threadTitle, description, image, metadata, null)
    }

    fun createThreadWithMessage(requestCreateThread: RequestCreateThread): ArrayList<String>? {
        return chat.createThreadWithMessage(requestCreateThread)
    }

    fun updateContact(requestUpdateContact: RequestUpdateContact): String {
        return chat.updateContact(requestUpdateContact)
    }


    fun getContact(requestGetContact: RequestGetContact): String {
        return chat.getContacts(requestGetContact, null)
    }

    fun addContacts(requestAddContact: RequestAddContact): String {
        chat.addListener(object : ChatListener {
            override fun onContactAdded(content: String?, response: ChatResponse<ResultAddContact>?) {
                super.onContactAdded(content, response)
                testListener.onAddContact(response)
            }
        })
        return chat.addContact(requestAddContact)
    }

    fun removeContact(requestRemoveContact: RequestRemoveContact): String {
        return chat.removeContact(requestRemoveContact)
    }


    fun blockContact(requestBlock: RequestBlock): String {
        return chat.block(requestBlock, null)
    }

    fun unBlock(requestUnBlock: RequestUnBlock): String {
        return chat.unblock(requestUnBlock, null)
    }

    fun deleteMessage(requestDeleteMessage: RequestDeleteMessage): String {
        return chat.deleteMessage(requestDeleteMessage, null)
    }

    fun getThread(requestThread: RequestThread): String {
        return chat.getThreads(requestThread, null)
    }

    fun getBlockList(requestBlockList: RequestBlockList): String {
        return chat.getBlockList(requestBlockList, null)
    }

    fun sendTextMsg(requestMessage: RequestMessage): String {
        return chat.sendTextMessage(requestMessage, null)
    }

    fun forwardMessage(requestForwardMessage: RequestForwardMessage): List<String> {
        return chat.forwardMessage(requestForwardMessage)
    }

    fun replyMessage(replyMessage: RequestReplyMessage): String {
        return chat.replyMessage(replyMessage, null)
    }

    fun addParticipant(requestAddParticipants: RequestAddParticipants): String {
        return chat.addParticipants(requestAddParticipants, null)
    }

    fun removeParticipant(requestRemoveParticipants: RequestRemoveParticipants): String {
        return chat.removeParticipants(requestRemoveParticipants, null)
    }

    fun leaveThread(requestLeaveThread: RequestLeaveThread): String {
        return chat.leaveThread(requestLeaveThread, null)
    }

    fun getParticipant(requestThreadParticipant: RequestThreadParticipant): String {
        return chat.getThreadParticipants(requestThreadParticipant, null)
    }
}