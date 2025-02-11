package kr.co.nexters.winepick.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import kr.co.nexters.winepick.R
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.home.HomeActivity
import kr.co.nexters.winepick.ui.login.LoginActivity
import kr.co.nexters.winepick.util.startActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val authManager : AuthManager by inject()
    private lateinit var splashView : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashView = findViewById(R.id.lottie_splash)
        splashView.apply {
            setAnimation("winepick.json")
            playAnimation()
            loop(true)
        }


        Handler().postDelayed({
            checkToken()
        },DURATION)

    }
    private fun checkToken() {
        /**
         * 토큰 처리
         */
        if (authManager.autoLogin) {
            authManager.apply {
                this.testType = "A"
            }
           startActivity(HomeActivity::class, isFinish = true)
        } else {
           startActivity(LoginActivity::class, isFinish = true)
        }
//        UserApiClient.instance.accessTokenInfo { tokenInfo , error ->
//            if (error != null) {
//                Timber.e("토큰 정보 보기 실패")
//                startActivity(LoginActivity::class,isFinish = true)
//
//            }
//            else if (tokenInfo!=null) {
//                Timber.e("${tokenInfo.expiresIn}")
//                startActivity(HomeActivity::class,isFinish = true)
//            } else {
//                startActivity(LoginActivity::class,isFinish = true)
//
//            }
//        }
    }
    companion object {
        private const val DURATION : Long = 1500
    }
}