package com.neppplus.gudocin_android

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.firebase.messaging.FirebaseMessaging
import com.neppplus.gudocin_android.adapters.ApproachViewPagerAdapter
import com.neppplus.gudocin_android.databinding.ActivityApproachBinding
import com.neppplus.gudocin_android.datas.BasicResponse
import com.neppplus.gudocin_android.datas.GlobalData
import com.neppplus.gudocin_android.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApproachActivity : BaseActivity() {

    lateinit var binding: ActivityApproachBinding

    internal lateinit var viewPager: ViewPager

    var currentPosition = 0

    val handler = Handler(Looper.getMainLooper()) {
        setPage()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_approach)
        setupEvents()
        setValues()

        viewPager = findViewById(R.id.viewPager) as ViewPager

        val adapter = ApproachViewPagerAdapter(this)
        viewPager.adapter = adapter

        val thread = Thread(PagerRunnable())
        thread.start()

    }

    fun setPage() {

        if (currentPosition == 3) currentPosition = 0
        viewPager.setCurrentItem(currentPosition, true)
        currentPosition += 1

    }

    inner class PagerRunnable : Runnable {
        override fun run() {
            while (true) {
                Thread.sleep(2500)
                handler.sendEmptyMessage(0)
            }
        }
    }

    fun setFirebaseToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener {

//            토큰을 잘 받아왔다면
            if (it.isSuccessful) {

                val deviceToken = it.result

                Log.d("FCM토큰", deviceToken!!)
                ContextUtil.setDeviceToken(mContext, deviceToken)

                GlobalData.loginUser?.let {

                    apiService.patchRequestUpdateUserInfo(
                        "android_device_token",
                        ContextUtil.getDeviceToken(mContext)
                    )
                }

            }

        }
    }

    override fun setupEvents() {

        binding.btnLogin.setOnClickListener {

            val myHandler = Handler(Looper.getMainLooper())

            myHandler.postDelayed({

                val myIntent: Intent

                if (ContextUtil.getAutoLogin(mContext) && ContextUtil.getToken(mContext) != "") {
                    myIntent = Intent(mContext, NavigationActivity::class.java)
                } else {
                    myIntent = Intent(mContext, LoginActivity::class.java)
                }

                startActivity(myIntent)

                finish()

            }, 1000)

        }

        binding.btnSignUp.setOnClickListener {

            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)

        }

        binding.txtMaybeLater.setOnClickListener {

            val alert = AlertDialog.Builder(mContext, R.style.MyDialogTheme)
            alert.setTitle("나가기 확인")
            alert.setMessage("정말 나가시겠습니까?")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                finish()
            })
            alert.setNegativeButton("취소", null)

            alert.show()

        }

    }

    override fun setValues() {

        setFirebaseToken()

        apiService.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    GlobalData.loginUser = response.body()!!.data.user

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        apiService.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    GlobalData.loginUser = response.body()!!.data.user

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }

}