package com.neppplus.gudocin_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.adapters.RecyclerVewAdapterForMain
import com.neppplus.gudocin_android.adapters.SmallCategoriesListAdapter
import com.neppplus.gudocin_android.databinding.FragmentHomeBinding
import com.neppplus.gudocin_android.datas.BasicResponse
import com.neppplus.gudocin_android.datas.ReviewData
import com.neppplus.gudocin_android.datas.SmallCategoriesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding

    val mReviewList = ArrayList<ReviewData>()
    lateinit var mMainRecyclerAdapter : RecyclerVewAdapterForMain



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {



    }

    override fun setValues() {

        getReviewListFromServer()
        getBannerListFromServer()


        mMainRecyclerAdapter = RecyclerVewAdapterForMain(mContext, mReviewList)
        binding.reviewListRecyclerView.adapter = mMainRecyclerAdapter
        binding.reviewListRecyclerView.layoutManager = LinearLayoutManager(mContext)



    }

    fun getReviewListFromServer() {

        apiService.getRequestReviewList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {

                    var br = response.body()!!
                    mReviewList.clear()
                    mReviewList.addAll(br.data.reviews)
                    mMainRecyclerAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })


    }

    ////위의 함수는 아래로 대체되어야 함////
    // fun getReviewListInSmallCategoryFromServer()

    fun getBannerListFromServer(){
        apiService.getRequestMainBanner().enqueue(  object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if (response.isSuccessful) {

                    val br = response.body()!!
                    mMainRecyclerAdapter.mBannerList.clear()
                    mMainRecyclerAdapter.mBannerList.addAll( br.data.banners )

//                    (뷰페이저) 어댑터 새로고침
                    mMainRecyclerAdapter.mBannerViewPagerAdapter.notifyDataSetChanged()

                }

            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        } )
    }







    }


