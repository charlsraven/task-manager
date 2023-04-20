package com.techno_3_team.task_manager.screen

import com.kaspersky.kaspresso.screens.KScreen
import com.techno_3_team.task_manager.MainActivity
import io.github.kakaocup.kakao.text.KButton
import com.techno_3_team.task_manager.R

object Sidemenu : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.side_bar
    override val viewClass: Class<*> = MainActivity::class.java

    val mainManagementButton = KButton { withId(R.id.btGoogleSideBAr) }
    val accountManagementButton = KButton { withId(R.id.btGoogleSideBAr2AccountManagement) }
    val synchronizeButton = KButton { withId(R.id.btGoogleSideBAr2Sync) }
    val switchThemeButton = KButton {withId(R.id.btSwitcherTheme)}
    val engLangButton = KButton {withId(R.id.radioButtonEn)}
    val ruLangButton = KButton {withId(R.id.radioButtonRus)}

}