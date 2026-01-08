package com.scspro.onepos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scspro.onepos.ui.compose.OneposAdminScreen
import com.scspro.onepos.ui.compose.OneposKioskScreen

@Composable
fun OneposNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Admin
    ){
        // ==== 관리자 화면 ====
        composable<Routes.Admin> {
            OneposAdminScreen(
                onNavigateToKiosk = {navController.navigate(Routes.Kiosk)},               // 키오스크로 이동
                onAddCategory = {navController.navigate(Routes.Admin)},                   // 카테고리 추가 후 Admin화면 Display
                onAddProduct = {navController.navigate(Routes.Admin)},                    // 상품 추가 후 Admin화면 display
                onEditProduct = {TODO()}                    // 상품 수정
            )
        }
        // ==== 키오스크 화면 ====
        composable<Routes.Kiosk> {
            OneposKioskScreen(
                onNavigateToAdmin = {navController.navigate(Routes.Admin)},                // 관리자로 이동
                onNavigateToDetail = {TODO()}              // 상품상세
            )
        }
    }
}