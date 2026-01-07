package com.scspro.onepos.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

/**
 * Hilt에게 데이터베이스 DI 방법 알려주는 설정파일 클래스
 */
@Module
@InstallIn(SingletonComponent::class)   //앱 전체 생명주기 동안 유지
abstract class RepositoryModule {

    // ProductRepository 인터페이스 요청 시 ProductRepositoryImpl 구현체를 바인딩(연결)
    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: OneposRepositoryImpl): OneposRepository

}