package com.project.hostelmate.retrofit

import com.project.hostelmate.model.AttendanceModel
import com.project.hostelmate.model.ChangeMessPref
import com.project.hostelmate.model.ChatModel
import com.project.hostelmate.model.CollegeHostellRegModel
import com.project.hostelmate.model.EditProfile
import com.project.hostelmate.model.Login
import com.project.hostelmate.model.MessPrefModel
import com.project.hostelmate.model.OutPassModel
import com.project.hostelmate.model.ReqOutPassModel
import com.project.hostelmate.model.ReqVisitorPassModel
import com.project.hostelmate.model.UserProfile
import com.project.hostelmate.model.VerifyHostel
import com.project.hostelmate.model.VisitorPassModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ApiService {

    @FormUrlEncoded
    @POST(Urls.LOGIN)
    suspend fun loginuser(@FieldMap params: HashMap<String?, String?>)
            : Response<Login>

    @FormUrlEncoded
    @POST(Urls.VIEW_OUT_PASS)
    suspend fun viewOutPass(@FieldMap params: HashMap<String?, String?>)
            : Response<OutPassModel>

    @FormUrlEncoded
    @POST(Urls.VIEW_VISITOR_PASS)
    suspend fun viewVisitorPass(@FieldMap params: HashMap<String?, String?>)
            : Response<VisitorPassModel>

    @FormUrlEncoded
    @POST(Urls.SUBMIT_OUT_PASS)
    suspend fun reqOutPass(@FieldMap params: HashMap<String?, String?>)
            : Response<ReqOutPassModel>

    @FormUrlEncoded
    @POST(Urls.REQ_VISITOR_PASS)
    suspend fun reqVisitorPass(@FieldMap params: HashMap<String?, String?>)
            : Response<ReqVisitorPassModel>

    @FormUrlEncoded
    @POST(Urls.MARK_ATTENDANCE)
    suspend fun markAttendance(@FieldMap params: HashMap<String?, String?>)
            : Response<AttendanceModel>

    @FormUrlEncoded
    @POST(Urls.USER_PROFILE)
    suspend fun viewUserProfile(@FieldMap params: HashMap<String?, String?>)
            : Response<UserProfile>

    @FormUrlEncoded
    @POST(Urls.EDIT_PROFILE)
    suspend fun editUserProfile(@FieldMap params: HashMap<String?, String?>)
            : Response<EditProfile>

    @FormUrlEncoded
    @POST(Urls.VIEW_MESS_PREF)
    suspend fun viewMessPref(@FieldMap params: HashMap<String?, String?>)
            : Response<MessPrefModel>

    @FormUrlEncoded
    @POST(Urls.CHANGE_MESS_PREF)
    suspend fun changeMessPref(@FieldMap params: HashMap<String?, String?>)
            : Response<ChangeMessPref>

    @FormUrlEncoded
    @POST(Urls.VIEW_ALL_CHAT)
    suspend fun viewAllChat(@FieldMap params: HashMap<String?, String?>)
            : Response<ChatModel>

    @FormUrlEncoded
    @POST(Urls.SENT_MESSAGE)
    suspend fun sentMessage(@FieldMap params: HashMap<String?, String?>)
            : Response<ChatModel>

    @FormUrlEncoded
    @POST(Urls.HOSTEL_VERIFICATION)
    suspend fun hostelVerification(@FieldMap params: HashMap<String?, String?>)
            : Response<VerifyHostel>

    @FormUrlEncoded
    @POST(Urls.COLLAGE_HOSTEL_REG)
    suspend fun collegeHostelReg(@FieldMap params: HashMap<String?, String?>)
            : Response<CollegeHostellRegModel>

    @FormUrlEncoded
    @POST(Urls.PRIVATE_HOSTEL_REG)
    suspend fun privateHostelReg(@FieldMap params: HashMap<String?, String?>)
            : Response<CollegeHostellRegModel>

    companion object {
        private val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
            .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        operator fun invoke(): ApiService {
            return Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)


        }
    }
}