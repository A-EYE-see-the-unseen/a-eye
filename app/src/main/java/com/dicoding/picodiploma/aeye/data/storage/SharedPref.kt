package com.dicoding.picodiploma.aeye.data.storage

import android.annotation.SuppressLint
import android.content.Context
import com.dicoding.picodiploma.aeye.data.UserData
import com.dicoding.picodiploma.aeye.data.response.LoginResponse

class SharedPref constructor(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    private val isLoginKey = "isLoginKey"

    var isLogin: Boolean
        set(value) {
            sharedPreferences.edit()
                .putBoolean(isLoginKey, value)
                .apply()
        }
        get() = sharedPreferences.getBoolean(isLoginKey, false)

    val user: UserData
        get() {
            return UserData(
                sharedPreferences.getString("nip", null),
                sharedPreferences.getString("nama_pengawas", null)
            )
        }

    fun saveUser(loginResponse: LoginResponse){
        val editor = sharedPreferences.edit()

        editor.putString("token", loginResponse.token)
        editor.putString("nip", user.nip)
        editor.putString("nama_pengawas", user.nama_pengawas)

        editor.apply()
    }

    fun clear(){
        val editor = sharedPreferences.edit()

        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "shared_pref"
        @SuppressLint("StaticFieldLeak")
        private var mInstance: SharedPref? = null

        @Synchronized
        fun getInstance(context: Context): SharedPref{
            if(mInstance == null){
                mInstance = SharedPref(context)
            }
            return mInstance as SharedPref
        }
    }

}