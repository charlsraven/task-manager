package com.techno_3_team.task_manager.test

import android.Manifest
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.techno_3_team.task_manager.MainActivity
import com.techno_3_team.task_manager.screen.MainScreen
import com.techno_3_team.task_manager.screen.Sidemenu
import com.techno_3_team.task_manager.screen.Sidemenu.accountManagementButton
import com.techno_3_team.task_manager.screen.Sidemenu.mainManagementButton
import com.techno_3_team.task_manager.screen.Sidemenu.switchThemeButton
import com.techno_3_team.task_manager.screen.Sidemenu.synchronizeButton
import org.junit.Rule
import org.junit.Test

class AccountManagementButtonSwitchThemeTest : TestCase() {
    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @get:Rule
    val activityRule = activityScenarioRule<MainActivity>()

    /**
     * проверка на фичу -- при смене темы
     * развёрнутые кнопки управления аккаунтом обратно сворачиваются
     */

    @Test
    fun accountManagementButtonSwitchThemeTest() = run {
        step("Open Main Screen") {
            testLogger.i("open main screen")
            MainScreen {
                sidemenuButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Check Sidemenu Buttons") {
            Sidemenu {
                mainManagementButton {
                    isVisible()
                }
                accountManagementButton {
                    isInvisible()
                }
                synchronizeButton {
                    isInvisible()
                }
            }
        }
        step("Expand Main Management Button") {
            testLogger.i("expand main management button")
            Sidemenu {
                mainManagementButton {
                    click()
                }
                accountManagementButton {
                    isVisible()
                }
                synchronizeButton {
                    isVisible()
                }
            }
        }
        step("Switch Theme") {
            testLogger.i("switch theme")
            Sidemenu {
                switchThemeButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Check Management Buttons") {
            testLogger.i("check management buttons")
            Sidemenu {
                mainManagementButton {
                    isVisible()
                }
                accountManagementButton {
                    isInvisible()
                }
                synchronizeButton {
                    isInvisible()
                }
            }
        }
    }
}