package ir.fanap.chattestapp.application.ui.util

import android.support.annotation.StringDef

class ConstantMsgType {

    companion object {
        const val UPDATE_CONTACT = "UPDATE_CONTACT"
        const val SEND_MESSAGE = "SEND_MESSAGE"
        const val GET_THREAD = "GET_THREAD"
        const val ADD_CONTACT = "ADD_CONTACT"
        const val REMOVE_CONTACT = "REMOVE_CONTACT"
        const val UNBLOCK_CONTACT = "UNBLOCK_CONTACT"
        const val BLOCK_CONTACT = "BLOCK_CONTACT"
        const val GET_CONTACT = "GET_CONTACT"
        const val CREATE_THREAD = "CREATE_THREAD"
        const val CREATE_THREAD_CHANNEL = "CREATE_THREAD_CHANNEL"
        const val CREATE_THREAD_CHANNEL_GROUP = "CREATE_THREAD_CHANNEL_GROUP"
        const val CREATE_THREAD_PUBLIC_GROUP = "CREATE_THREAD_PUBLIC_GROUP"
        const val CREATE_THREAD_OWNER_GROUP = "CREATE_THREAD_OWNER_GROUP"
        const val REMOVE_PARTICIPANT = "REMOVE_PARTICIPANT"
        const val ADD_PARTICIPANT = "ADD_PARTICIPANT"
    }

    @StringDef(UPDATE_CONTACT,
        GET_THREAD
        ,ADD_CONTACT
        ,UNBLOCK_CONTACT
        ,BLOCK_CONTACT
        ,GET_CONTACT
        ,CREATE_THREAD
        ,CREATE_THREAD_CHANNEL,
        CREATE_THREAD_CHANNEL_GROUP,
        CREATE_THREAD_PUBLIC_GROUP,
        CREATE_THREAD_OWNER_GROUP,
        REMOVE_PARTICIPANT,
        ADD_PARTICIPANT,
        REMOVE_CONTACT
        )

    @Retention(AnnotationRetention.SOURCE)
    annotation class CONSTANT
}