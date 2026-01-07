package com.scspro.onepos.data.repository

import com.scspro.onepos.data.dao.CategoryDAO
import com.scspro.onepos.data.dao.ProductDAO
import com.scspro.onepos.data.toEntity
import com.scspro.onepos.data.toModel
import com.scspro.onepos.model.Category
import com.scspro.onepos.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
/**
 * 데이터 소스 관리 & 도메인 모델 변환
*/

class OneposRepositoryImpl @Inject constructor( //Hilt로 의존성 주입
    private val categoryDao: CategoryDAO,
    private val productDao: ProductDAO
): OneposRepository {

    override suspend fun addCategory(category: Category) {
        categoryDao.add(category.toEntity())
    }

    override suspend fun removeCategory(id: Int) {
        categoryDao.removeById(id)
    }

    override suspend fun addProduct(product: Product) {
        productDao.add(product.toEntity())
    }

    override suspend fun removeProduct(id: Int) {
        productDao.removeById(id)
    }

    override fun observeCategories(): Flow<List<Category>> =
        //db에서 category들 조회. 조회한 entity를 모두 domain model로 변환
        categoryDao.observeAll().map { it.map { entity -> entity.toModel() } }

    override fun observeProductsInCategory(categoryId: Int): Flow<List<Product>> =
        productDao.observeByCategory(categoryId).map { it.map { entity -> entity.toModel() } }

}