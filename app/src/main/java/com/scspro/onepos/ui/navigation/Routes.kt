package com.scspro.onepos.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe Navigation을 위한 화면(Destination)의 경로를 정의
 * Serialization 라이브러리 사용하여 객체 형태로 경로 관리함.
 */
sealed interface Routes {
    // ==== 관리자 메인 화면 ====
    @Serializable data object Admin: Routes

    // ==== 키오스크 메인 화면 ====
    @Serializable data object Kiosk: Routes

    // ==== 상품 상세 화면 ====
    //@Serializable data class ProductDetail(val productId: Int): Routes  //상품 ID 전달

    // ==== 카테고리 추가 화면 ====
    //@Serializable data object AddCategory: Routes

    // ==== 상품 추가 화면 ====
    //@Serializable data class AddProduct(val categoryId: Int): Routes //카테고리 ID 전달
    
    // ==== 상품 수정 화면 ====
    //@Serializable data class EditProduct(val productId: Int): Routes //상품 ID 전달

}