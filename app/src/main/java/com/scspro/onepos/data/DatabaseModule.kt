package com.scspro.onepos.data

import android.content.Context
import com.scspro.onepos.data.dao.CategoryDAO
import com.scspro.onepos.data.dao.OptionDAO
import com.scspro.onepos.data.dao.ProductDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * AppRoomDatabse와 DAO(CategoryDAO, ProductDAO)의 인스턴스 생성방법 Hilt에게 제공
*/
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    // === AppRoomDatabase 인스턴스 제공 =============================================================
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppRoomDatabase {
        //AppRoomDatabase.getInstance() 호출하여 DB 인스턴스 Get
        return AppRoomDatabase.getInstance(context)
    }

    // === CategoryDAO 인스턴스 제공 =================================================================
    @Provides
    fun provideCategoryDAO(db: AppRoomDatabase): CategoryDAO {
        //위에서 제공된 AppRoomDatabase 인스턴스 사용하여 DAO Get
        return db.getCategoryDao()
    }

    // === ProductDAO 인스턴스 제공 ==================================================================
    @Provides
    fun provideProductDAO(db: AppRoomDatabase): ProductDAO {
        return db.getProductDao()
    }

    // ==== OptionDAO 인스턴스 제공 ==================================================================
    @Provides
    fun provideOptionDAO(db: AppRoomDatabase): OptionDAO {
        return db.getOptionDao()
    }

}