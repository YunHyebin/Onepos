package com.scspro.onepos.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.scspro.onepos.data.entity.CategoryEntity
import com.scspro.onepos.data.entity.ProductEntity
import kotlin.text.insert

/**
 * 앱 설치 후 최초 실행 시 초기 샘플 데이터 넣는 클래스
*/
class PreDBWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = AppRoomDatabase.getInstance(applicationContext)
        val categoryDAO = db.getCategoryDao()
        val productDAO = db.getProductDao()

        // 이미 데이터가 있으면 중복 삽입 방지
        //if (categoryDao.getCount() > 0) return Result.success()

        // DAO의 suspend 함수 사용하여 초기 샘플 데이터 삽입
//        val category1 = categoryDAO.add(CategoryEntity(name = "아아아"))
//        val product1 = productDAO.add(
//            ProductEntity(101, 1, "김치찌개", 9000)
//        )

//        val id1 = categpryDAO.add(CategoryEntity(name = "한식"))
//        val id2 = categpryDAO.add(CategoryEntity(name = "양식"))
//        val id3 = categpryDAO.add(CategoryEntity(name = "일식"))
//        val id4 = categpryDAO.add(CategoryEntity(name = "중식"))

        return Result.success()

    }
}