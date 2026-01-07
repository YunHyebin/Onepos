package com.scspro.onepos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.scspro.onepos.data.dao.CategoryDAO
import com.scspro.onepos.data.dao.OptionDAO
import com.scspro.onepos.data.dao.ProductDAO
import com.scspro.onepos.data.entity.CategoryEntity
import com.scspro.onepos.data.entity.OptionEntity
import com.scspro.onepos.data.entity.ProductEntity

 /**
  * 실제 SQLite를 추상화한 데이터베이스 본체.
  * 싱글톤 패턴으로 데이터베이스 인스턴스를 생성하는 클래스]
  */
@Database(
    entities = [CategoryEntity::class, ProductEntity::class, OptionEntity::class],
    version = 1,
    exportSchema = false
) //exportSchema: 잠시 스키마 추출x
@TypeConverters(RoomConverters::class)      //JSON<>OptionItem 컨버터 등록
abstract class AppRoomDatabase : RoomDatabase() {
    //dao
    abstract fun getCategoryDao(): CategoryDAO
    abstract fun getProductDao(): ProductDAO
    abstract fun getOptionDao(): OptionDAO


    //database
    companion object {
        @Volatile
        private var INSTANCE: com.scspro.onepos.data.AppRoomDatabase? = null   //싱글톤 객체이므로 상수로 표현

        fun getInstance(context: Context): com.scspro.onepos.data.AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.scspro.onepos.data.AppRoomDatabase::class.java,
                    "onepose_db"
                )
//                    .addCallback(object : Callback() { //샘플 데이터 삽입, 초기화)
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            super.onCreate(db)
//                            val request = OneTimeWorkRequestBuilder<PreDBWorker>().build()
//                            WorkManager.getInstance(context.applicationContext)
//                                .enqueueUniqueWork(
//                                    "prepopulate_db",
//                                    ExistingWorkPolicy.KEEP,
//                                    request
//                                )
//                        }
//                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /*fun getInstance(context: Context): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "onepose_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }*/
    }
}