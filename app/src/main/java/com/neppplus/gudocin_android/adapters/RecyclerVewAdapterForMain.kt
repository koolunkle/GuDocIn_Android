package com.neppplus.gudocin_android.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.neppplus.gudocin_android.NavigationActivity
import com.neppplus.gudocin_android.R
import com.neppplus.gudocin_android.ReviewActivity
import com.neppplus.gudocin_android.ReviewDetailActivity
import com.neppplus.gudocin_android.datas.BannerData
import com.neppplus.gudocin_android.datas.BasicResponse
import com.neppplus.gudocin_android.datas.ReviewData
import com.neppplus.gudocin_android.datas.SmallCategoriesData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class RecyclerVewAdapterForMain
    (val mContext: Context, val mList: List<ReviewData>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mBannerList = ArrayList<BannerData>()
    lateinit var mBannerViewPagerAdapter: BannerViewPagerAdapter


    inner class HeaderViewHolder(row: View) : RecyclerView.ViewHolder(row) {


        val bannerViewPager = row.findViewById<ViewPager>(R.id.bannerViewPager)

        fun bind() {
            mBannerViewPagerAdapter = BannerViewPagerAdapter(
                (mContext as NavigationActivity).supportFragmentManager,
                mBannerList
            )

            bannerViewPager.adapter = mBannerViewPagerAdapter

            var currentPage = 0

            val nextPage = {

                currentPage++

                if (currentPage == mBannerList.size) {
                    currentPage = 0
                }
                bannerViewPager.currentItem = currentPage

            }
            val myHandler = Handler(Looper.getMainLooper())

//            Timer클래스 활용 =>  할 일 (코드)를 2초마다 반복.

            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {

                    myHandler.post(nextPage)

                }

            }, 2000, 2000)
        }
    }


    inner class ItemViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val txtReviewerNickName = row.findViewById<TextView>(R.id.txtReviewerNickName)
        val txtProductName = row.findViewById<TextView>(R.id.txtProductName)
        val txtProductPrice = row.findViewById<TextView>(R.id.txtProductPrice)
        val btnOpenPreview = row.findViewById<LinearLayout>(R.id.btnOpenPreview)
        val imgReviewSomeNail = row.findViewById<ImageView>(R.id.imgReviewSomeNail)
        val imgReviewerImage = row.findViewById<ImageView>(R.id.imgReviewerImage)
        val btnWriteReview = row.findViewById<TextView>(R.id.btnWriteReview)
        val txtOpenPreView = row.findViewById<TextView>(R.id.txtOpenPreView)
        val preViewLayout = row.findViewById<LinearLayout>(R.id.preViewLayout)
        val btnGotoReviewDetail = row.findViewById<LinearLayout>(R.id.btnGotoReviewDetail)
        val txReviewTitle = row.findViewById<TextView>(R.id.txReviewTitle)
        var isPreViewOpen = false


        fun bind(data: ReviewData) {

            txtReviewerNickName.text = "${data.user.nickname} 님의 리뷰"
            txtProductName.text = data.product.name
            txtProductPrice.text = data.product.price.toString()
            Glide.with(mContext).load(data.thumbNailImg).into(imgReviewSomeNail)
            Glide.with(mContext).load(data.user.profileImageURL).into(imgReviewerImage)
            txReviewTitle.text = data.title
            btnWriteReview.setOnClickListener {
                val myIntent = Intent(mContext, ReviewActivity::class.java)
                myIntent.putExtra("product", data.product)
                mContext.startActivity(myIntent)
            }

            if (isPreViewOpen == false) {
                btnOpenPreview.setOnClickListener {
                    preViewLayout.visibility = View.VISIBLE
                    txtOpenPreView.text = "닫기"
                    isPreViewOpen = true
                    notifyDataSetChanged()
                }
            } else {
                btnOpenPreview.setOnClickListener {
                    preViewLayout.visibility = View.GONE
                    txtOpenPreView.text = "더보기.."
                    isPreViewOpen = false
                    notifyDataSetChanged()
                }
            }

            btnGotoReviewDetail.setOnClickListener {

                val myIntent = Intent(mContext, ReviewDetailActivity::class.java)
                myIntent.putExtra("review", data)
                mContext.startActivity(myIntent)

            }
        }
    }

    val HEADER_VIEW_TYPE = 1000
    val REVIEW_ITEM_TYPE = 1001

    override fun getItemViewType(position: Int): Int {

        return when (position) {

            0 -> HEADER_VIEW_TYPE
            else -> REVIEW_ITEM_TYPE

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            HEADER_VIEW_TYPE -> {
                val row = LayoutInflater.from(mContext)
                    .inflate(R.layout.main_top_viewpager_item, parent, false)
                HeaderViewHolder(row)
            }
            else -> {
//                리뷰 아이템

                val row = LayoutInflater.from(mContext)
                    .inflate(R.layout.review_item_for_main, parent, false)
                ItemViewHolder(row)

            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind()

            }
            is ItemViewHolder -> {

                holder.bind(mList[position - 1])

            }
        }
    }

    override fun getItemCount() = mList.size + 1





}
