package ir.fanap.chattestapp.application.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.Handler
import com.fanap.podchat.chat.Chat
import com.fanap.podchat.chat.ChatListener
import com.fanap.podchat.model.*
import com.fanap.podchat.requestobject.*
import rx.subjects.PublishSubject
import java.io.BufferedReader
import java.io.InputStreamReader

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

//    * createThreadTypes = {
//        *         NORMAL: 0,
//        *         OWNER_GROUP: 1,
//        *         PUBLIC_GROUP: 2,
//        *         CHANNEL_GROUP: 4,
//        *         CHANNEL: 8
//        *       }
//    */
//
    fun createThread(requestCreateThread: RequestCreateThread): String {
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

    fun showLog() {
//        var handler: Handler? = null
//
//        handler?.post {
//            val process: Process = Runtime.getRuntime().exec("logcat -d")
//            val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
//            var log: StringBuilder = java.lang.StringBuilder()
//            var line = bufferedReader.readLine()
//            while ((line) != null) {
//                log.append(line)
//                observableLog.onNext(log.toString())
//            }
//        }
    }

    fun blockContact(requestBlock: RequestBlock): String {
        return chat.block(requestBlock, null)
    }

    fun unBlock(requestUnBlock: RequestUnBlock): String {
        return chat.unblock(requestUnBlock, null)
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
}