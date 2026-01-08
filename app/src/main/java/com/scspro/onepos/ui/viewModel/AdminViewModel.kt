package com.scspro.onepos.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.scspro.onepos.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.viewModelScope
import com.scspro.onepos.data.repository.OneposRepository
import com.scspro.onepos.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [관리자용 ViewModel]
 * 공통 기능 + 관리자 전용 편집 로직 (추후 추가 가능)
 */
@HiltViewModel
class AdminViewModel @Inject constructor(repository: OneposRepository) : BaseViewModel(repository) {
////////////////////////////////////////////////////////////////////////////////////////////////////
// 카테고리 추가
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun addCategory(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newCategory = Category(name = name) //id를 0으로 여기서 명시하면 계속 id가 0으로 저장되지 않나
            Log.d("새 카테고리 추가", "new cateogory = $newCategory")
            repository.addCategory(newCategory)
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////
// 상품 추가
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun addProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val newProduct = Product(
                id = product.id,
                name = product.name,
                price = product.price,
                imageRes = product.imageRes,
                categoryId = product.categoryId,
                optionId = product.optionId
            )
            repository.addProduct(newProduct)
        }
    }

}
