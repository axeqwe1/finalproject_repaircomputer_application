package com.example.repaircomputerapplication_finalproject.data

sealed class ScreenRoutes(val route:String){

        //---------------------------Screen Route
        object Home : ScreenRoutes("Home")
        object Notification : ScreenRoutes("Notification")
        object RequestRepairList : ScreenRoutes("RequestRepairList")
        object LoginScreens : ScreenRoutes("Login")
        object Menu : ScreenRoutes("Menu")

        object checkConnection : ScreenRoutes("CheckConnection")

        //--------------------------- Manage Screen
        object ManageMenu : ScreenRoutes("ManageMenu")
        object UserManageMenu : ScreenRoutes("UserManageMenu")
        object DataManageMenu : ScreenRoutes("DataManageMenu")
        object ManageUserScreen :ScreenRoutes("UserManage?User={UserType}"){
                fun passUserType(
                        UserType: String
                ):String{
                        return "UserManage?User=$UserType"
                }
        }
        object ManageDataScreen :ScreenRoutes("DataManage?Data={DataType}"){
                fun passDataType(
                        DataType: String
                ):String{
                        return "DataManage?Data=$DataType"
                }
        }
        //---------------------------Form Route
        object FormRequestForRepair : ScreenRoutes("หน้าแจ้งซ่อม")


        //---------------------------Form ManageMentData
        object AddUserForm : ScreenRoutes("AddUserForm?User={UserType}&isEdit={isEdit}&UserId={UserId}"){
                fun passUserTypeAndIsEditAndUserId(
                        UserType:String = "",
                        UserId:String? = null,
                        isEdit:Boolean = false,

                ):String{
                        return "AddUserForm?User=$UserType&isEdit=$isEdit&UserId=$UserId"
                }
        }
        object DataForm:ScreenRoutes("DataForm?DataType={DataType}&isEdit={isEdit}&DataID={DataID}"){
                fun passIsEditAndId(
                        isEdit:Boolean = false,
                        DataID:String = "",
                        DataType:String = ""
                ):String{
                        return "DataForm?DataType=$DataType&isEdit=$isEdit&DataID=$DataID"
                }
        }

        //---------------------------Report
        object Report : ScreenRoutes("report_screen")
        object Dashboard : ScreenRoutes("dashboard_screen")



        //---------------------------DetailRequest
        object detailRepair:ScreenRoutes("detail_screen?rrid={rrid}&userType={userType}"){
                fun passRridAndUserType(rrid:String,userType:String):String{
                    return "detail_screen?rrid=$rrid&userType=$userType"
                }
        }
        object addDetailRepair:ScreenRoutes("add_detail_screen?rrid={rrid}&rd_id={rd_id}&isUpdate={isUpdate}"){
                fun passRrceIdAndIsUpdate(rrid:String,rd_id:String,isUpdate:Boolean):String{
                        return "add_detail_screen?rrid=$rrid&rd_id=$rd_id&isUpdate=$isUpdate"
                }
        }

        object statusDetail:ScreenRoutes("status_detail?rrid={rrid}"){
                fun passRrid(rrid: String):String{
                        return "status_detail?rrid=$rrid"
                }
        }
        //---------------------------DisplayAssignWork
        object AssignWork : ScreenRoutes("displayList_Assignwork")
        object TechnicianListBacklog : ScreenRoutes("techBacklogList?admin_id={admin_id}&rrid={rrid}"){
                fun passAdminIdAndRrid(admin_id:String,rrid:String):String{
                        return "techBacklogList?admin_id=$admin_id&rrid=$rrid"
                }
        }

        //---------------------------Graph Route
        object AuthNav : ScreenRoutes("AUTH_NAV_GRAPH")
        object HomeNav : ScreenRoutes("HOME_NAV_GRAPH")
        object MenuNav : ScreenRoutes("MENU_NAV_GRAPH")
        object ListRequestNav : ScreenRoutes("LISTREQUEST_NAV_GRAPH")
        object NotiNav:ScreenRoutes("NOTI_NAV_GRAPH")
        object RequestFormNav : ScreenRoutes("RR_FORM_NAV_GRAPH")
        object ManageMenuNav : ScreenRoutes("MANAGE_MENU_NAV_GRAPH")
        object UserManageMenuNav : ScreenRoutes("USER_MANAGE_MENU_GRAPH")
        object DataManageMenuNav : ScreenRoutes("DATA_MANAGE_MENU_GRAPH")
        object DisplayNav:ScreenRoutes("DISPLAY_DETAIL_GRAPH")
        object AssignWorkNav:ScreenRoutes("ASSIGN_WORK_GRAPH")
        object DashboardNav:ScreenRoutes("DASHBOARD_GRAPH")
}