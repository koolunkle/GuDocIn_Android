package com.neppplus.gudocin_android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.adapters.ReviewListRecyclerViewAdapterForProfile
import com.neppplus.gudocin_android.databinding.FragmentReviewListBinding
import com.neppplus.gudocin_android.datas.BasicResponse
import com.neppplus.gudocin_android.datas.ReviewData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewListFragment : BaseFragment() {

    lateinit var binding: FragmentReviewListBinding

    val mMyReviewList = ArrayList<ReviewData>()

    lateinit var mReviewRecyclerViewAdapterForProfile: ReviewListRecyclerViewAdapterForProfile

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_review_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        mReviewRecyclerViewAdapterForProfile = ReviewListRecyclerViewAdapterForProfile(mContext, mMyReviewList)
        binding.myReviewListRecyclerView.adapter = mReviewRecyclerViewAdapterForProfile
        binding.myReviewListRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }

    override fun setValues() {
        getMyReviewListFromServer()
    }

    fun getMyReviewListFromServer() {
        apiService.getRequestUserReviewList().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if (response.isSuccessful) {
                    val br = response.body()!!
                    mMyReviewList.clear()
                    mMyReviewList.addAll(br.data.reviews)
                    mReviewRecyclerViewAdapterForProfile.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

}