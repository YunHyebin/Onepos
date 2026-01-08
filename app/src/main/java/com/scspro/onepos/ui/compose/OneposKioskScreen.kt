package com.scspro.onepos.ui.compose


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
//import com.scspro.onepos.data.repository.MockProductRepository
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product
import com.scspro.onepos.ui.viewModel.KioskViewModel
import com.scspro.onepos.ui.theme.Beige
import com.scspro.onepos.ui.theme.Grey
import com.scspro.onepos.ui.theme.SCSProBlack
import com.scspro.onepos.ui.theme.SCSProGold
import kotlinx.coroutines.launch
import com.scspro.onepos.utils.Util.Companion.toFormattedWon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OneposKioskScreen(
    viewModel : KioskViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit,  //상품 ID 전달
    onNavigateToAdmin: () -> Unit
) {
    // ==== State 관찰 ==============================================================================
    val categories by viewModel.categories.collectAsState()             // 카테고리
    val totalPrice by viewModel.totalPrice.collectAsState()             // 장바구니 총 금액
    val currentProductList by viewModel.products.collectAsState()       // 상품 리스트
    val currentLanguage by viewModel.currentLanguage.collectAsState()   // 언어 설정

    //==== 비밀번호 입력 창 표시 상태==================================================================
    var showPwdDialog by remember { mutableStateOf(false) } //처음에는 표시x

    // ==== 카테고리 데이터가 로드될 때까지 대기 ====
    if (categories.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = SCSProGold)
        }
        return
    }



    // ==== Pager 상태 설정 =========================================================================
    //카테고리 수 만큼 페이지 설정
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val coroutineScope = rememberCoroutineScope()

    // ==== Pager 스와이프 시 ViewModel의 선택 인덱스 업데이트 ===========================================
    LaunchedEffect(pagerState.currentPage) {
        viewModel.selectCategory(pagerState.currentPage + 1)
    }

    Scaffold(
        topBar = {
            Column {
                // ==== 설정바 + 언어 토글 ============================================================
                KioskTopBar (
                    onMenuClick = { showPwdDialog = true },  //설정 아이콘 클릭 시 비밀번호 입력 다이어로그 true
                    languageLabel = currentLanguage.label,
                    onLanguageClick = { viewModel.toggleLanguage()}
                )

                // ==== 카테고리 탭 ==================================================================
                //Pager, Tab 동기화
                KioskCategoryTabs(
                    categories = categories,
                    selectedIndex = pagerState.currentPage,
                    onTabSelected = {index ->
                        //탭 클릭 시 Pager 이동 & ViewModel 상태 업데이트
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        },
        // ==== 하단 바 (장바구니 & 결제)==========================================================
        bottomBar = {
            KioskBottomBar(
                totalPrice = totalPrice,
                onCartClick = { /*장바구니 화면 이동 로직 나중에.. */},
                onPayClick = { /*결제 화면 이동 로직 나중에.. */ }
            )
        }
    ) { paddingValues ->
        // ==== 상품 리스트 ==========================================================================
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) { pageIndex ->

            //ViewModel의 productsFlow는 현재 선택된 페이지(pagerState.currentPage)에 해당하는 데이터만 가지고 있다.
            //따라서 현재 페이지일 때만 데이터 렌더링.
            //그렇지 않으면 Box 표시하여 메모리 사용 줄임.
            val isCurrentPage = pagerState.currentPage == pageIndex

            if(isCurrentPage) {
                KioskProductGrid (
                    products = currentProductList,
                    onProductClick = onNavigateToDetail
                )
            } else {
                Box(modifier = Modifier.fillMaxSize())
            }
        }

    }

    // ==== 비밀번호 입력 Dialog 표시 =====================================================================
    if(showPwdDialog) {
        PwdInputDialog(
            onDismiss = { showPwdDialog = false },  //취소 시 Dialog 닫기
            onSuccess = {
                showPwdDialog = false   //성공 시 Dialog 닫기
                onNavigateToAdmin()     //관리자 화면으로 이동
            }
        )
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상단 탑 바 Composable Components
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun KioskTopBar(onMenuClick: () -> Unit, languageLabel: String, onLanguageClick: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(60.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ==== 설정 아이콘 (좌측)=====================================================================
        IconButton(onClick = onMenuClick) {  //좌측 설정 아이콘 클릭 시
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "메뉴 열기",
                modifier = Modifier.size(40.dp),
                tint = SCSProBlack
            )
        }

        // ==== 언어 토글버튼 (우측)===================================================================
        TextButton(onClick = onLanguageClick) {
            Text(
                text = languageLabel,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = SCSProBlack
            )
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 카테고리 탭
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun KioskCategoryTabs(categories: List<Category>, selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            containerColor = Color.White,
            edgePadding = 0.dp,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]).fillMaxWidth(),
                    color = SCSProGold,
                    height = 5.dp
                )
            },
            modifier = Modifier.weight(1f).height(45.dp)
        ) {
            categories.forEachIndexed { index, category ->
                val isSelected = selectedIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = category.name,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 18.sp,
                            color = if (selectedIndex == index) SCSProGold else Grey
                        )
                    },
                    //modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }
    }
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 리스트
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun KioskProductGrid(products: List<Product>, onProductClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(products, key = { it.id }) { product ->
            KioskProductCard (
                product = product,
                onClick = {onProductClick(product.id)}
            )
        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 아이템 카드
////////////////////////////////////////////////////////////////////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KioskProductCard(product: Product, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) //가로세로 비율
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) //기본 배경
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. 상품 이미지 영역 (상단) - weight(1f)
            Box(
                modifier = Modifier
                    .weight(1f) // 남은 공간 모두 차지
                    .fillMaxWidth()
                    .background(Color.LightGray), // 이미지 로딩 전 배경
                contentAlignment = Alignment.Center
            ) {
                // TODO: imageRes를 활용하여 이미지 표시 로직 추가 (예: painterResource)
                if (product.imageRes == null) {
                    Text(text = product.name, fontSize = 24.sp, color = Color.DarkGray)
                }
            }

            // 2. 상품 정보 영역 (하단) - weight 없이 고정 높이 또는 내용 기반 높이
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.background(Beige) // 베이지색 하단 영역
                    .padding(10.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = SCSProBlack,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${product.price.toFormattedWon()}원",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = SCSProBlack
                )
            }
        }
    }

}



////////////////////////////////////////////////////////////////////////////////////////////////////
// 장바구니 + 결제 버튼 하단 바
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun KioskBottomBar(totalPrice: Long, onCartClick: () -> Unit, onPayClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // ==== 장바구니 버튼 ====
        Button(
            onClick = onCartClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = SCSProGold),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text("장바구니", fontWeight = FontWeight.Bold, color = Color.White)
        }
        // ==== {x} 결제 버튼
        Button(
            onClick = onPayClick,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = SCSProGold),
            shape = RoundedCornerShape(5.dp)
        ) {
            val buttonText = if (totalPrice > 0) "${totalPrice.toFormattedWon()}원 결제" else "결제하기"
            Text(buttonText, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

}

// =================================================================================
// Preview
// =================================================================================
//@SuppressLint("ViewModelConstroctorInComposable")
//@Preview(showBackground = true, widthDp = 720, heightDp = 1600)
//@Composable
//fun KioskScreenPreview() {
//    //mockViewModel로 Preview 데이터 로딩 지연 방지 (임시)
//    val mockViewModel = remember {KioskViewModel(MockProductRepository()) }
//
//    // ==== 네비게이션 목업 상태 및 스낵바 =============================================================
//    var adminNavMsg by remember { mutableStateOf<String?>(null)}
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    // ==== 관리자 화면 이동 목업 ====================================================================
//    val dummyNavigatoionAdmin: () -> Unit = {
//        adminNavMsg = "관리자로 전환"
//        coroutineScope.launch {
//            snackbarHostState.showSnackbar(
//                message = adminNavMsg!!,
//                duration = SnackbarDuration.Short
//            )
//        }
//    }
//
//    // ==== 상품 상세 화면 이동 목업 ==================================================================
//    val dummyNavigateDetail: (Int) -> Unit = { id ->
//        coroutineScope.launch {
//            snackbarHostState.showSnackbar(
//                message = "상품 ID${id} 상세화면으로 이동",
//                duration = SnackbarDuration.Short
//            )
//        }
//    }
//
//    MaterialTheme {
//
//        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
//            OneposKioskScreen(
//                viewModel = mockViewModel,
//                onNavigateToDetail = dummyNavigateDetail,
//                onNavigateToAdmin = dummyNavigatoionAdmin
//            )
//
//            SnackbarHost(
//                hostState = snackbarHostState,
//                modifier = Modifier.align(Alignment.TopCenter) //align은 Box 최상위 컴포넌트 있어야 함.
//            )
//        }
//    }
//
//}