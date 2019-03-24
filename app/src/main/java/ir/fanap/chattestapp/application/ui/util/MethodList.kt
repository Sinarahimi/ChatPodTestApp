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
                "8 Send Message",
                "9 Remove Contact",
                "10 Add participant",
                "11 remove participant",
                "12 Forward Message",
                "13 Reply Message",
                "14 Leave Thread"
            )


        val methodFuncOne: MutableList<String> =
            arrayListOf(
                "CHANNEL",//0
                "",//1
                "",//2
                "",//3
                "",//4
                "",//5
                "",//6
                "",//7
                "8 sent",//8
                "",//9
                "",//10
                "",//11
                "onSent",//12
                "",//13
                ""
            )

        val methodFuncTwo: MutableList<String> =
            arrayListOf(
                "CHANNEL_GROUP",//0
                "",//1
                "",//2
                "",//3
                "",//4
                "",//5
                "",//6
                "",//7
                "8 deliver",
                "",//9
                "",//10
                "",//11
                "",//12
                "",//13
                ""
            )

        val methodFuncThree: MutableList<String> =
            arrayListOf(
                "PUBLIC_GROUP",//0
                "",//1
                "",//2
                "",//3
                "",//4
                "",//5
                "",//6
                "",//7
                "8 seen",
                "",//9
                "",//10
                "",//11
                "",//12
                "",//13
                ""
            )

        val methodFuncFour: MutableList<String> =
            arrayListOf(
                "OWNER_GROUP",//0
                "",//1
                "",//2
                "",//3
                "",//4
                "",//5
                "",//6
                "",//7
                "",//8
                "",//9
                "",//10
                "",//11
                "",//12
                "",//13
                ""
            )
    }
}