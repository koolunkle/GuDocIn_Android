package com.neppplus.gudocin_android.model.product

import com.google.gson.annotations.SerializedName
import com.neppplus.gudocin_android.model.review.ReviewData
import com.neppplus.gudocin_android.model.store.StoreData
import java.io.Serializable
import java.text.NumberFormat
import java.util.*

data class ProductData(
  var id: Int,
  var name: String,
  var price: Int,
  @SerializedName("image_url")
  var imageUrl: String,
  var store: StoreData,
  var reviews: List<ReviewData>,
) : Serializable {

  fun getFormattedPrice(): String {
    return "${NumberFormat.getInstance(Locale.KOREA).format(this.price)}원"
  }
}