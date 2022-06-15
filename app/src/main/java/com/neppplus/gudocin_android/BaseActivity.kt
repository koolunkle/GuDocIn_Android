package com.neppplus.gudocin_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.neppplus.gudocin_android.network.RetrofitService
import com.neppplus.gudocin_android.network.RetrofitServiceInstance
import com.neppplus.gudocin_android.view.presenter.activity.cart.CartActivity
import com.neppplus.gudocin_android.view.presenter.activity.shopping.ShoppingActivity

abstract class BaseActivity<T : ViewDataBinding, U : BaseViewModel>(@LayoutRes private val layoutRes: Int) : AppCompatActivity(layoutRes) {
  lateinit var binding: T
  abstract val getViewModel: U

  lateinit var retrofitService: RetrofitServiceInstance

  /**
   * ActionBar Contents
   */
  private lateinit var back: ImageView
  lateinit var title: TextView
  lateinit var shopping: ImageView
  lateinit var cart: ImageView

  abstract fun initView()
  open fun observe() = Unit

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // 초기화 된 layoutRes 로 dataBinding 객체 생성
    binding = DataBindingUtil.setContentView(this@BaseActivity, layoutRes)
    binding.apply {
      lifecycleOwner = this@BaseActivity
      /**
       * 구체적인 Type 의존 없이 setVariable() 통해 variable set 가능
       * binding.xxx = "yyy" / binding.setVariable(BR.xxx, "yyy") 둘다 동일한 행위
       * BR = Binding Resource
       */
      setVariable(BR.viewModel, getViewModel)
      /**
       * 즉각적인 바인딩: 변수 또는 Observable 변경 시 바인딩은 다음 프레임 전에 변경되도록 예약
       * 바인딩을 즉시 실행해야 할 경우 executePendingBindings() 메소드 사용
       */
      executePendingBindings()
    }

    val retrofit = RetrofitService.getRetrofit(this@BaseActivity)
    retrofitService = retrofit.create(RetrofitServiceInstance::class.java)

    supportActionBar.let {
      setCustomActionBar()
      actionBarListener()
    }

    initView()
    observe()
  }

  private fun setCustomActionBar() {
    val defActionBar = supportActionBar!!
    Log.d("ActionBar", "Into the Setting")

    defActionBar.apply {
      displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
      setCustomView(R.layout.custom_action_bar)

      val toolbar = customView.parent as Toolbar
      toolbar.setContentInsetsAbsolute(0, 0)

      back = customView.findViewById(R.id.btnBack)
      title = customView.findViewById<TextView>(R.id.txtTitle).toString()
      shopping = customView.findViewById(R.id.btnShopping)
      cart = customView.findViewById(R.id.btnCart)
    }
  }

  val onClickListener = View.OnClickListener { view ->
    when (view) {
      back -> finish()
      shopping -> startActivity(Intent(this, ShoppingActivity::class.java))
      cart -> startActivity(Intent(this, CartActivity::class.java))
    }
  }

  private fun actionBarListener() {
    back.setOnClickListener(onClickListener)
    shopping.setOnClickListener(onClickListener)
    cart.setOnClickListener(onClickListener)
  }

  inline fun binding(block: T.() -> Unit) {
    binding.apply(block)
  }
}