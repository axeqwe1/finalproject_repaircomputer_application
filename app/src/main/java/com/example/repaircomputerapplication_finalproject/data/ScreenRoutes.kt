package com.example.repaircomputerapplication_finalproject.data

sealed class ScreenRoutes(val route:String){

        //---------------------------Screen Route
        object Home : ScreenRoutes("Home")
        object Notification : ScreenRoutes("Notification")
        object RequestRepairList : ScreenRoutes("RequestRepairList")
        object LoginScreens : ScreenRoutes("Login")
        object Menu : ScreenRoutes("Menu")

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
//        object DepartmentForm:ScreenRoutes("DepartmentForm?isEdit={isEdit}&DepartID={DepartID}"){
//                fun passIsEditAndId(
//                        isEdit:Boolean = false,
//                        DepartID:String = ""
//                ):String{
//                        return "DepartmentForm?isEdit=$isEdit&BuildID=$DepartID"
//                }
//        }
//        object EquipmentForm:ScreenRoutes("EquipmentForm?isEdit={isEdit}&EqID={EqID}"){
//                fun passIsEditAndId(
//                        isEdit:Boolean = false,
//                        EqID:String = ""
//                ):String{
//                        return "EquipmentForm?isEdit=$isEdit&BuildID=$EqID"
//                }
//        }
//        object EquipmentTypeForm:ScreenRoutes("EquipmentTypeForm?isEdit={isEdit}&EqCID={EqCID}"){
//                fun passIsEditAndId(
//                        isEdit:Boolean = false,
//                        EqCID:String = ""
//                ):String{
//                        return "EquipmentTypeForm?isEdit=$isEdit&BuildID=$EqCID"
//                }
//        }
//        object LevelOfDamageForm:ScreenRoutes("LevelOfDamageForm?isEdit={isEdit}&LoedID={LoedID}"){
//                fun passIsEditAndId(
//                        isEdit:Boolean = false,
//                        LoedID:String = ""
//                ):String{
//                        return "LevelOfDamageForm?isEdit=$isEdit&BuildID=$LoedID"
//                }
//        }
        //---------------------------Report
        object Report : ScreenRoutes("report_screen")








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
}
