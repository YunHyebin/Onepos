package com.scspro.onepos.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scspro.onepos.data.repository.OneposRepository
import com.scspro.onepos.model.CartItemUi
import com.scspro.onepos.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * [키오스크 사용자용 ViewModel]
 * 키오스크 UI에서 사용하는 상태 데이터 저장 & 로드
 * 공통 기능 + 장바구니 관련 로직
 */
@HiltViewModel
class KioskViewModel @Inject constructor(repository: OneposRepository) : BaseViewModel(repository) {

    // ==== 장바구니 UI ==============================================================================
    private val _cart = MutableStateFlow<List<CartItemUi>>(emptyList())
    val cart: StateFlow<List<CartItemUi>> = _cart.asStateFlow()

    // ==== 장바구니 총 금액 ==========================================================================
    //private val _totalPrice = MutableStateFlow(0L)
    //val totalPrice = _totalPrice.asStateFlow()
    val totalPrice : StateFlow<Long> = _cart.map { items ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)


////////////////////////////////////////////////////////////////////////////////////////////////////
// 장바구니 상품 추가
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun addProductToCard(product: Product, quantity: Int = 1) {
    val currentCart = _cart.value.toMutableList()
    var idx = currentCart.indexOfFirst { it.product.id == product.id }

    if (idx >= 0) {
        val old = currentCart[idx]
        currentCart[idx] = old.copy(quantity = old.quantity + quantity)
    } else {
        currentCart.add(CartItemUi(product, quantity))
    }
    _cart.value = currentCart
}
////////////////////////////////////////////////////////////////////////////////////////////////////
// 장바구니 수량 변경
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun updateCartQuantity(productId: Int, qty: Int) {
        _cart.value = _cart.value.mapNotNull {
            if (it.product.id == productId) {
                if (qty <= 0) null else it.copy(quantity = qty)
            } else it
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
// 장바구니 클리어
////////////////////////////////////////////////////////////////////////////////////////////////////
    fun clearCart() { _cart.value = emptyList() }

}