package com.scspro.onepos.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scspro.onepos.data.repository.OneposRepository
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Language
import com.scspro.onepos.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * [공통 데이터 관리]
 * Admin과 Kiosk 화면 모두에서 사용하는 카테고리, 상품, 언어 설정을 관리
 */
open class BaseViewModel(val repository: OneposRepository): ViewModel() {
    // ==== 전체 카테고리 목록 ========================================================================
    val categories: StateFlow<List<Category>> = repository.observeCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ==== 현재 선택된 카테고리 id ===================================================================
    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId.asStateFlow()

    fun selectCategory(index: Int) {
        _selectedCategoryId.value = index
    }

    // ==== 선택된 카테고리에 따른 상품 리스트 필터링 =====================================================
    val products: StateFlow<List<Product>> = _selectedCategoryId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.observeProductsInCategory(id)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // ==== 언어 설정 ===============================================================================
    private val _currentLanguage = MutableStateFlow(Language.KOREAN)
    val currentLanguage = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = _currentLanguage.value.next()
    }

    init {
        // 초기 데이터 로드 시 첫번째 카테고리 자동 선택
        viewModelScope.launch {
            categories.collect { list ->
                if (list.isNotEmpty()) {
                    // 현재 선택된 인덱스(초기값 0)가 불러온 리스트에 포함되어 있지 않다면
                    // 리스트의 첫 번째 항목을 선택하도록 강제
                    if (_selectedCategoryId.value == null || list.none { it.id == _selectedCategoryId.value }) {
                        _selectedCategoryId.value = list.first().id
                    }
                }
                Log.d("KioskViewModel", "Categories Loaded: ${list.size}")
            }
        }
    }
}