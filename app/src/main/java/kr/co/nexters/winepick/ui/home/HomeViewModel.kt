package kr.co.nexters.winepick.ui.home

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.auth.LoginClient
import kr.co.nexters.winepick.WinePickApplication
import kr.co.nexters.winepick.constant.TestConstant.A
import kr.co.nexters.winepick.constant.TestConstant.B
import kr.co.nexters.winepick.constant.TestConstant.C
import kr.co.nexters.winepick.constant.TestConstant.D
import kr.co.nexters.winepick.constant.TestConstant.E
import kr.co.nexters.winepick.constant.TestConstant.F
import kr.co.nexters.winepick.data.repository.WinePickRepository
import kr.co.nexters.winepick.di.AuthManager
import kr.co.nexters.winepick.ui.base.BaseViewModel
import kr.co.nexters.winepick.ui.like.LikeListActivity
import kr.co.nexters.winepick.ui.search.SearchActivity
import kr.co.nexters.winepick.ui.survey.SurveyActivity
import kr.co.nexters.winepick.ui.type.TypeDetailActivity
import kr.co.nexters.winepick.util.KakaoLoginHelper
import timber.log.Timber

/**
 * Kotlin 에서 사용하는 ViewModel 예
 *
 * @since v1.0.0 / 2021.01.28
 */
class HomeViewModel(private val repo: WinePickRepository, private val auth: AuthManager) : BaseViewModel() {
    private var _likecnt = MutableLiveData<String>()
    var likeCnt : LiveData<String> = _likecnt

    private var _isTest : MutableLiveData<Boolean> = MutableLiveData()
    val isTest : LiveData<Boolean>
        get() = _isTest

    private var _isUser : MutableLiveData<Boolean> = MutableLiveData()
    val isUser : LiveData<Boolean>
        get() = _isUser

    private var _loginWarningDlg : MutableLiveData<Boolean> = MutableLiveData()
    val loginWarningDlg : LiveData<Boolean> = _loginWarningDlg


    private var _keyword1 = MutableLiveData<String>()
    var keyword1 : LiveData<String> = _keyword1
    private var _keyword2 = MutableLiveData<String>()
    var keyword2 : LiveData<String> = _keyword2


    /** 생성자 */
    init {
        _likecnt.value = "0"
        _isTest.value = false
        _isUser.value = false
        _loginWarningDlg.value = false

    }
    fun getUserLikes(){
        if (!auth.testType.isNullOrEmpty()) {
            _isTest.value = true
        }
        if (auth.token != "guest") {
            _isUser.value = true
            repo.getUser(
                    userId = auth.id,
                    accessToken = auth.token,
                    onSuccess = {
                        if(it.likes!! > 99) {
                            _likecnt.value = "99+"
                        }
                        _likecnt.value = it.likes.toString()
                        if(it.personalityType != null) {
                            setUserPersonalType()
                        }
                    },
                    onFailure = {
                    }
            )
        } else {
            setUserPersonalType()
        }

    }



    fun setUserPersonalType() {
        return when(auth.testType) {
            "A" -> {getUserType(A) }
            "B" -> {getUserType(B)}
            "C" -> {getUserType(C)}
            "D" -> {getUserType(D)}
            "E" -> {getUserType(E)}
            "F" -> {getUserType(F)}
            else -> {}

        }
    }
    fun getUserType(resultId : Int) {
        Timber.e("${auth.testType}")
        repo.getResult(
            resultId = resultId,
            onSuccess = {
                _keyword1.value = it.keyword1
                _keyword2.value = it.keyword2
            },
            onFailure = {

            }
        )
    }

    fun testClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext,SurveyActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun searchClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext,SearchActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    fun likeClick() {
        if(isUser.value!!) {
            WinePickApplication.getGlobalApplicationContext().startActivity(
                Intent(
                    WinePickApplication.appContext,
                    LikeListActivity::class.java
                )
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } else {
            _loginWarningDlg.value = true
        }
    }
    fun myTypeClick() {
        WinePickApplication.getGlobalApplicationContext().startActivity(Intent(WinePickApplication.appContext,
            TypeDetailActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
    /** UI 의 onDestroy 개념으로 생각하면 편할듯 */
    override fun onCleared() {
        super.onCleared()
    }

    override fun onResume() {
        super.onResume()
        getUserLikes()
    }

}
