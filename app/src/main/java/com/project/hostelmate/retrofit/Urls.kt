package com.project.hostelmate.retrofit

class Urls {

    companion object{
        const val BASE_URL = "http://campus.sicsglobal.co.in/Project/Hostel/api/"

        //endpoints

        const val LOGIN = "user_login.php"
        const val VIEW_OUT_PASS = "view_all_outpass.php"
        const val VIEW_VISITOR_PASS = "view_all_visitorpass.php"
        const val SUBMIT_OUT_PASS = "request_outpass.php"
        const val REQ_VISITOR_PASS = "visitor_request.php"
        const val MARK_ATTENDANCE = "attendence_mark.php"
        const val USER_PROFILE = "view_user_profile.php"
        const val EDIT_PROFILE = "edit_user.php"
        const val VIEW_MESS_PREF = "view_messpreference.php"
        const val CHANGE_MESS_PREF = "change_mess_pref.php"
        const val VIEW_ALL_CHAT = "view_message.php"
        const val SENT_MESSAGE = "message.php"
        const val HOSTEL_VERIFICATION = "hostal_verify.php"
        const val COLLAGE_HOSTEL_REG = "user_college_registration.php"
        const val PRIVATE_HOSTEL_REG = "user_private_registration.php"

    }
}