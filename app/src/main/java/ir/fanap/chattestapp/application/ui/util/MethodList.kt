package ir.fanap.chattestapp.application.ui.util

class MethodList {

    companion object {
        val methodNames: MutableList<String> =
            arrayListOf(
                "0 Create Thread with message(NORMAL)",
                "1 get Contact",
                "2 Block Contact",
                "3 Add Contact",
                "4 Get Thread",
                "5 Get BlockList",
                "6 unBlock Contact",
                "7 Update Contact",
                "8 Send Message"
            )


        val methodFuncOne: MutableList<String> =
            arrayListOf(
                "CHANNEL",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "8 sent"
            )

        val methodFuncTwo: MutableList<String> =
            arrayListOf(
                "CHANNEL_GROUP",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "8 deliver"
            )

        val methodFuncThree: MutableList<String> =
            arrayListOf(
                "PUBLIC_GROUP",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "8 seen"
            )

        val methodFuncFour: MutableList<String> =
            arrayListOf(
                "OWNER_GROUP",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )
    }
}