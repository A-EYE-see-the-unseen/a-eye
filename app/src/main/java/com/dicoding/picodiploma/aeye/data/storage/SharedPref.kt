package com.dicoding.picodiploma.aeye.data.storage

import android.content.Context

class SharedPref constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
//    private val isLoginKey = "isLoginKey"

//    var isLogin: Boolean
//        set(value) {
//            sharedPreferences.edit()
//                .putBoolean(isLoginKey, value)
//                .apply()
//        }
//        get() = sharedPreferences.getBoolean(isLoginKey, false)

//    val user: UserData
//        get() {
//            return UserData(
//                sharedPreferences.getString("nip", null),
//                sharedPreferences.getString("nama_pengawas", null)
//            )
//        }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("TOKEN_KEY", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("TOKEN_KEY").apply()
    }

//    fun saveId(id: Int) {
//        sharedPreferences.edit().putInt("ID",id).apply()
//    }
//
//    fun getId(): Int {
//        return sharedPreferences.getInt("ID", 15)
//    }
//
//    fun clearId() {
//        sharedPreferences.edit().remove("ID").apply()
//    }



//    fun saveUser(loginResponse: LoginResponse) {
//        val editor = sharedPreferences.edit()
//
//        editor.putString("token", loginResponse.token)
//        editor.putString("nip", user.nip)
//        editor.putString("nama_pengawas", user.nama_pengawas)
//
//        editor.apply()
//    }
//
//    fun clear(){
//        val editor = sharedPreferences.edit()
//
//        editor.clear()
//        editor.apply()
//    }

    companion object {
        private const val SHARED_PREF_NAME = "shared_pref"
//        @SuppressLint("StaticFieldLeak")
//        private var mInstance: SharedPref? = null

//        @Synchronized
//        fun getInstance(context: Context): SharedPref{
//            if(mInstance == null){
//                mInstance = SharedPref(context)
//            }
//            return mInstance as SharedPref
//        }
    }

}