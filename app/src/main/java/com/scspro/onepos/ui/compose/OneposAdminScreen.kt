package com.scspro.onepos.ui.compose


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.scspro.onepos.model.AdminMenu
//import com.scspro.onepos.data.repository.MockProductRepository
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product
import com.scspro.onepos.ui.viewModel.KioskViewModel
import com.scspro.onepos.ui.theme.Beige
import com.scspro.onepos.ui.theme.Grey
import com.scspro.onepos.ui.theme.SCSProBlack
import com.scspro.onepos.ui.theme.SCSProGold
import com.scspro.onepos.ui.viewModel.AdminViewModel
import kotlinx.coroutines.launch
import com.scspro.onepos.utils.Util.Companion.toFormattedWon

/**
 * Onepos 관리자 화면 UI
 * 카테고리 및 상품 추가/수정/삭제
 * 설정 버튼 클릭 시 키오스크 전환
*/
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun OneposAdminScreen(
    viewModel : AdminViewModel = hiltViewModel(), // 자동으로 ProductRepositoryImpl이 주입된 ViewModel을 가져옴.
    onNavigateToKiosk: () -> Unit,     // 키오스크 이동
    onAddCategory: () -> Unit,         // 카테고리 추가
    onAddProduct: (Int) -> Unit,          // 상품 추가
    onEditProduct: (Int) -> Unit       // 상품 수정
) {
    // ==== State 관찰 ============================================================================
    val categories by viewModel.categories.collectAsState()             // 카테고리
    val currentProductList by viewModel.products.collectAsState()       // 상품 리스트
    val currentLanguage by viewModel.currentLanguage.collectAsState()   // 언어 설정

    // ==== UI 상태 관리 ============================================================================
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    //==== Dialog 상태 관리 =========================================================
    var showKioskDialog by remember { mutableStateOf(false) }   // 키오스크 전환 창
    var showAddCategory by remember { mutableStateOf(false) }   // 카테고리 추가 창
    var showEditCategory by remember { mutableStateOf(false) }  // 카테고리 편집 창(수정/삭제)
    var showAddProduct by remember { mutableStateOf(false) }    // 상품 추가 창
    var showEditProduct by remember { mutableStateOf(false) }   // 상품 편집 창(수정/삭제)

    // ==== Pager 상태 설정 =========================================================================
    val pagerState = rememberPagerState(pageCount = { categories.size })    //카테고리 수 만큼 페이지 설정

    LaunchedEffect(pagerState.currentPage) {                        //Pager 스와이프 시 ViewModel의 선택 인덱스 업데이트
        viewModel.selectCategory(pagerState.currentPage)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,    //스와이프로 열기 허용
        drawerContent = {
            // ==== 관리자 슬라이드 메뉴 ==============================================================
            AdminMenuBar(
                onClose = { coroutineScope.launch { drawerState.close() } },
                onMenuClick = { menu ->
                    when (menu) {
                        AdminMenu.SWITCH_TO_KIOSK -> showKioskDialog = true
                        AdminMenu.ADD_CATEGORY -> showAddCategory = true
                        AdminMenu.EDIT_CATEGORY -> showEditCategory = true
                        AdminMenu.ADD_PRODUCT -> {
                            if (categories.isNotEmpty()) {
                                val currentCategoryId = categories[pagerState.currentPage].id
                                onAddProduct(currentCategoryId)
                            }
                        }
                        AdminMenu.EDIT_PRODUCT -> showEditProduct = true
                        AdminMenu.EDIT_OPTION -> { /* 옵션 설정 로직 */}
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    // ==== 설정바 + 언어 토글 ============================================================
                    AdminTopBar (
                        onMenuClick = { coroutineScope.launch { drawerState.open() } },  //메뉴 아이콘 클릭 시 메뉴바 open
                        languageLabel = currentLanguage.label,
                        onLanguageClick = { viewModel.toggleLanguage()}
                    )

                    // ==== 카테고리 탭 ==================================================================
                    //Pager, Tab 동기화
                    AdminCategoryTabs(
                        categories = categories,
                        selectedIndex = pagerState.currentPage,
                        onTabSelected = { index ->
                            //탭 클릭 시 Pager 이동 & ViewModel 상태 업데이트
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        onAddCategory = { showAddCategory = true }
                    )
                }
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
                //현재 페이지만 렌더링하도록 조건부 처리 가능

                if (pagerState.currentPage == pageIndex) {
                    AdminProductGrid(
                        products = currentProductList,
                        onProductClick = onEditProduct,
                        onAddProductClick = {
                            val currentCategoryId = categories[pageIndex].id
                            onAddProduct(currentCategoryId)
                        }
                    )
                }
            }
        }
    }

    // ==== 키오스크 전환 Dialog 표시 =====================================================================
    if(showKioskDialog) {
        ChangeKioskDialog(
            onDismiss = { showKioskDialog = false }, // 취소 시 키오스크 전환 Dialog 닫기
            onConfirm = {
                showKioskDialog = false
                onNavigateToKiosk()                 // 키오스크 화면으로 이동 (navigation에 전달)
            },
            categories.isEmpty()
        )
    }

    // ==== 카테고리 추가 Dialog 표시 =================================================================
    if(showAddCategory) {
        CategoryAddDialog(
            onDismiss = { showAddCategory = false },
            onSave = { newName -> //AdminDialog로부터 전달받은 categoryName
                viewModel.addCategory(newName)
                showAddCategory = false
                onAddCategory()
            }
        )
    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상단 탑 바 Composable Components
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminTopBar(onMenuClick: () -> Unit, languageLabel: String, onLanguageClick: () -> Unit) {
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
fun AdminCategoryTabs(categories: List<Category>,selectedIndex: Int,onTabSelected: (Int) -> Unit,onAddCategory: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ){
        // ==== 기존 카테고리 탭들 ====================================================================
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
                    }
                )
            }
            // ==== 카테고리 추가 "+" 버튼
            IconButton (
                onClick = onAddCategory,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "카테고리 추가", tint = SCSProGold)
            }
        }

    }
    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 리스트
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminProductGrid(products: List<Product>,onProductClick: (Int) -> Unit,onAddProductClick: () -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // ==== 기존 상품들 표시 ====================================================================
        items(products, key = { it.id }) { product ->
            AdminProductCard (
                product = product,
                onClick = {onProductClick(product.id)}
            )
        }

        // ==== 마지막 + 추가 카드 배치 ================================================================
        item { AddProductCard(onClick = onAddProductClick)}
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 아이템 카드
////////////////////////////////////////////////////////////////////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductCard(product: Product,onClick: () -> Unit){
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
// 상품 추가 카드
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AddProductCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Beige),
        border = BorderStroke(2.dp, SCSProGold)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
             Icon(
                 imageVector = Icons.Default.Add,
                 contentDescription = null,
                 modifier = Modifier.size(48.dp),
                 tint = SCSProBlack
             )
            }

        }
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
// 관리자 슬라이드 메뉴
////////////////////////////////////////////////////////////////////////////////////////////////////
@Composable
fun AdminMenuBar(onClose: () -> Unit, onMenuClick: (AdminMenu) -> Unit) {
    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2)  //화면 절반
    ) {
        Box(
            modifier = Modifier
                .height(105.dp)
                .fillMaxWidth()
                .padding(5.dp)
            // .border(...) 부분을 삭제하여 테두리를 제거했습니다.
        ){
            IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "메뉴 닫기",
                    modifier = Modifier.size(30.dp),
                    tint = SCSProBlack
                )
            }

            Text(
                text = "관리자 메뉴",
                color = SCSProGold,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = SCSProGold, thickness = 1.dp)
        Spacer(modifier = Modifier.height(20.dp))

        // AdminMenu Enum을 순회하며 메뉴 항목 생성
        AdminMenu.entries.forEach { menu ->
            NavigationDrawerItem(
                label = { Text(menu.title, fontWeight = FontWeight.Medium) },
                selected = false,
                onClick = {
                    // 메뉴 클릭 시 드로어 닫기
                    onClose()
                    onMenuClick(menu)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.White,
                    unselectedTextColor = SCSProBlack,
                    selectedContainerColor = SCSProGold,
                    selectedTextColor = Color.White
                )
            )
        }
    }
}

// =================================================================================
// Preview
// =================================================================================
//@SuppressLint("ViewModelConstroctorInComposable")
//@Preview(showBackground = true, widthDp = 720, heightDp = 1600)
//@Composable
//fun AdminScreenPreview() {
//    //mockViewModel로 Preview 데이터 로딩 지연 방지 (임시)
//    val mockViewModel = remember {KioskViewModel(MockProductRepository() }
//
//    // ==== 네비게이션 목업 상태 및 스낵바 =============================================================
//    var adminNavMsg by remember { mutableStateOf<String?>(null)}
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    // ==== 키오스크 화면 이동 목업 ====================================================================
//    val navigateToKiosk: () -> Unit = {
//        adminNavMsg = "관리자로 전환"
//        coroutineScope.launch {
//            snackbarHostState.showSnackbar(
//                message = adminNavMsg!!,
//                duration = SnackbarDuration.Short
//            )
//        }
//    }
//
//    MaterialTheme {
//        Box(modifier = Modifier.fillMaxSize()) {
//            OneposAdminScreen(
//                onNavigateToKiosk = {},
//                onAddCategory = {},
//                onAddProduct = { categoryId -> }, // 에러 해결: 파라미터 추가
//                onEditProduct = { productId -> }
//            )
//        }
//    }
//
//}
