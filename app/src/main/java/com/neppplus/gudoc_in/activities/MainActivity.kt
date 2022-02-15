package com.neppplus.gudoc_in.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.neppplus.gudoc_in.R
import com.neppplus.gudoc_in.databinding.ActivityMainBinding
import com.neppplus.gudoc_in.datas.BasicResponse
import com.neppplus.gudoc_in.fragments.MainFragment
import com.neppplus.gudoc_in.fragments.ProductListFragment
import com.neppplus.gudoc_in.fragments.ProfileFragment
import com.neppplus.gudoc_in.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backKeyPressedTime >= 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "백버튼을 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            System.exit(0)
        }
    }

    override fun setupEvents() {
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navMain -> binding.viewPager.currentItem = 0
                R.id.navReview -> binding.viewPager.currentItem = 1
                else -> binding.viewPager.currentItem = 2
            }
            true
        }
        getAndSendDeviceToken()
        getKeyHash()
    }

    override fun setValues() {
        btnBack.visibility = View.INVISIBLE
        txtTitleInActionBar.visibility = View.INVISIBLE
        exploreBoxInActionBar.visibility = View.VISIBLE

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@MainActivity)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNav.selectedItemId = when (position) {
                        0 -> R.id.navMain
                        1 -> R.id.navReview
                        else -> R.id.navProfile
                    }
                }
            })
        }
//        3장의 화면을 계속 유지하도록
        binding.viewPager.offscreenPageLimit = 3
    }

    inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount() = 3

        val fragmentList = arrayListOf(MainFragment(), ProductListFragment(), ProfileFragment())

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

        fun getFragment(position: Int) = fragmentList[position]
    }

    fun getAndSendDeviceToken() {
        if (ContextUtil.getDeviceToken(mContext) != "") {
            apiService.patchRequestUpdateUserInfo(
                "android_device_token",
                ContextUtil.getDeviceToken(mContext)
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
        }
    }

    fun getKeyHash() {
        val info = packageManager.getPackageInfo(
            "com.neppplus.gudoc_in",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }

}