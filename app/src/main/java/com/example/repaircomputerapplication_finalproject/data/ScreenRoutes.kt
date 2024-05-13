package com.example.repaircomputerapplication_finalproject.data

sealed class ScreenRoutes(val route:String){

        //Screen Route
        object Home : ScreenRoutes("หน้าเมนูหลัก")
        object Notification : ScreenRoutes("หน้าแจ้งเตือน")
        object RequestRepairList : ScreenRoutes("รายการแจ้งซ่อม")
        object LoginScreens : ScreenRoutes("หน้าล๊อคอิน")
        object Menu : ScreenRoutes("Menu")
        //Graph Route
        object AuthNav : ScreenRoutes("AUTH_NAV_GRAPH")
        object HomeNav : ScreenRoutes("HOME_NAV_GRAPH")
        object MenuNav : ScreenRoutes("MENU_NAV_GRAPH")
        object ListRequestNav : ScreenRoutes("LISTREQUEST_NAV_GRAPH")
        object NotiNav:ScreenRoutes("NOTI_NAV_GRAPH")
        object RequestFormNav : ScreenRoutes("RR_FORM_NAV_GRAPH")
        //Form Route
        object FormRequestForRepair : ScreenRoutes("หน้าแจ้งซ่อม")

        //Report
        object Report : ScreenRoutes("report_screen")

}
