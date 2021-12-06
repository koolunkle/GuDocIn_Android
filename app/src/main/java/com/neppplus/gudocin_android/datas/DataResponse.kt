package com.neppplus.gudocin_android.datas

class DataResponse(
    var user: UserData,
    var token: String,
    var reviews: List<ReviewData>,
    var products: List<ProductData>,
    var product: ProductData,
    var categories :List<CategoriesData>,
    var small_categories :List<SmallCategoriesData>,
    val banners : List<BannerData>,
) {
}