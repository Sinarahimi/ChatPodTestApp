package ir.fanap.chattestapp.application.ui

import com.fanap.podchat.mainmodel.ResultDeleteMessage
import com.fanap.podchat.model.*

interface TestListener {

    fun onGetContact(response: ChatResponse<ResultContact>?) {}
    fun onAddContact(response: ChatResponse<ResultAddContact>?) {}
    fun onCreateThread(response: ChatResponse<ResultThread>?) {}
    fun onBlock(chatResponse: ChatResponse<ResultBlock>?) {}
    fun onUnBlock(response: ChatResponse<ResultBlock>?) {}
    fun onGetThread(chatResponse: ChatResponse<ResultThreads>?) {}
    fun onError(chatResponse: ErrorOutPut?) {}
    fun onRemoveContact(response: ChatResponse<ResultRemoveContact>?) {}
    fun onBlockList(response: ChatResponse<ResultBlockList>?) {}
    fun onLogEvent(log: String) {}
    fun onUpdateContact(response: ChatResponse<ResultUpdateContact>?) {}
    fun onSent(response: ChatResponse<ResultMessage>?) {}
    fun onSeen(response: ChatResponse<ResultMessage>?) {}
    fun onDeliver(response: ChatResponse<ResultMessage>?) {}
    fun onThreadRemoveParticipant(response: ChatResponse<ResultParticipant>?) {
    }

    fun onThreadAddParticipant(response: ChatResponse<ResultAddParticipant>?) {

    }

    fun onLeaveThread(response: ChatResponse<ResultLeaveThread>?) {


    }

    fun onUnmuteThread(response: ChatResponse<ResultMute>?) {


    }

    fun onMuteThread(response: ChatResponse<ResultMute>?) {


    }

    fun onDeleteMessage(response: ChatResponse<ResultDeleteMessage>?) {


    }

    fun onEditedMessage(response: ChatResponse<ResultNewMessage>?) {


    }

    fun onGetHistory(response: ChatResponse<ResultHistory>?) {


    }

    fun onNewMessage(response: ChatResponse<ResultNewMessage>?) {


    }

    fun onGetThreadParticipant(response: ChatResponse<ResultParticipant>?) {


    }
}