package com.techno_3_team.task_manager.screen

import android.app.ActionBar.DISPLAY_HOME_AS_UP
import com.kaspersky.kaspresso.screens.KScreen
import com.techno_3_team.task_manager.MainActivity
import io.github.kakaocup.kakao.text.KButton
import com.techno_3_team.task_manager.R

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.main_fragment
    override val viewClass: Class<*> = MainActivity::class.java

    val sidemenuButton = KButton {DISPLAY_HOME_AS_UP}

}