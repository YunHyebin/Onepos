package com.scspro.onepos

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.scspro.onepos.ui.navigation.OneposNavGraph
import com.scspro.onepos.ui.theme.OneposTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {            //MainActivity의 root view 등록하여 화면 연결
            EnterFullScreen()
            OneposTheme {
                OneposNavGraph()
            }
        }
    }
}

@Composable
fun EnterFullScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val view = LocalView.current
    if(!view.isInEditMode && activity != null) {
        DisposableEffect(Unit) {
            val window = activity.window
            val insetsController = WindowCompat.getInsetsController(window, view)

            // 1. Edge-to-Edge 활성화 (이미 되어있다면 중복 호출 무방)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // 2. 상태 바와 내비게이션 바 숨기기
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            //insetsController.hide(WindowInsetsCompat.Type.navigationBars())


            // 3. 사용자가 스와이프 시 시스템 바가 일시적으로 나타나도록 설정
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            onDispose {
                // 전체 화면 모드에서 나갈 때 시스템 바 다시 표시 (선택 사항)
                // insetsController.show(WindowInsetsCompat.Type.systemBars())
                // WindowCompat.setDecorFitsSystemWindows(window, true) // 원래대로 돌리기
            }
        }
    }

}