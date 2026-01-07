package com.scspro.onepos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/*
Hilt 의존성 주입 프레임워크를 애플리케이션 전체에서 활성화하고 초기화
*/

@HiltAndroidApp
class OneposApplication : Application() {}