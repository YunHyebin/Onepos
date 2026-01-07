package com.scspro.onepos.model

/**
 * Menu 관련 통합 모델 파일
 * 모든 메뉴 아이템의 베이스가 되는 인터페이스와 구체적인 kiosk & 관리자 메뉴바 ENUM 구현체
*/


enum class AdminMenu(val title: String){
    SWITCH_TO_KIOSK("키오스크 전환"),
    ADD_CATEGORY("카테고리 추가"),
    EDIT_CATEGORY("카테고리 편집"),
    ADD_PRODUCT("상품 추가"),
    EDIT_PRODUCT("상품 편집"),
    EDIT_OPTION("옵션 설정")
}

enum class KioskMenu(val title: String){
    SWITCH_TO_ADMIN("관리자 모드 전환")
}