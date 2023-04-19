package com.techno_3_team.task_manager

import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.techno_3_team.task_manager.databinding.LoginFragmentBinding
import com.techno_3_team.task_manager.databinding.MainFragmentBinding
import com.techno_3_team.task_manager.fragments.ListsSettingsFragment
import com.techno_3_team.task_manager.fragments.SubtaskFragment
import com.techno_3_team.task_manager.fragments.TaskFragment
import com.techno_3_team.task_manager.fragments.TaskListContainerFragment
import com.techno_3_team.task_manager.structures.ListOfLists
import com.techno_3_team.task_manager.support.*
import java.util.*


class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var loginBinding: LoginFragmentBinding
    private lateinit var mainBinding: MainFragmentBinding
    private lateinit var listOfLists: ListOfLists

    private val preference by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private var sortOrder = SortOrder.BY_DATE
    private var isDay: Boolean = true
    private lateinit var randomData: RandomData
//    private lateinit var ltstViewModel: LTSTViewModel

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(mainBinding.mainContainer.id)

    private var fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentCreated(fm, f, savedInstanceState)
            updateUi()
        }
    }
    private var managementHidden = false

    //временная переменная до создания логики авторизированного пользователя
    //TODO: инициализировать переменную в правильных местах
    private var authorized = true
//    private var authorized = false

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme()
        super.onCreate(savedInstanceState)

        val showAuthScreen = preference.getBoolean(AUTH_KEY, true)
        if (showAuthScreen) {
            loginBinding = LoginFragmentBinding.inflate(layoutInflater)
            setContentView(loginBinding.root)

            loginBinding.continueWithoutAuthorization.setOnClickListener {
                initMainFragment(savedInstanceState)
                preference.edit()
                    .putBoolean(AUTH_KEY, false)
                    .apply()
            }
            loginBinding.continueWithGoogle.setOnClickListener{
                Log.e("tag", "clicked on google authorization")
                startAuthorization()
            }
        } else {
            initMainFragment(savedInstanceState)
        }
    }

    private fun initTheme() {
        val isDefaultThemeKey = preference.getBoolean(IS_DEFAULT_THEME_KEY, true)

        isDay = isDefaultThemeKey

        if (isDefaultThemeKey) {
            setTheme(R.style.Theme_CustomTheme_Default)
        } else {
            setTheme(R.style.Theme_CustomTheme_Light)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    private fun initData() {
        randomData = RandomData((4..12).random(), 20, 12)
        listOfLists = randomData.getRandomData()
    }

    private fun initMainFragment(savedInstanceState: Bundle?) {
        mainBinding = MainFragmentBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val toolbar: Toolbar = findViewById(mainBinding.toolbar.id)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
        supportActionBar?.title = getString(R.string.app_name)

        initData()

        val taskListContainerFragment = TaskListContainerFragment.newInstance(listOfLists)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(mainBinding.mainContainer.id, taskListContainerFragment, "MF")
                .commit()
        }
        mainBinding.sideBar.btListsSideBar.setOnClickListener {
            showListSettingsScreen()
        }
        mainBinding.sideBar.radioButtonEn.setOnClickListener {
            setLocaleLanguage(this, "en")
        }
        mainBinding.sideBar.radioButtonRus.setOnClickListener {
            setLocaleLanguage(this, "ru")
        }

        setAccountButton()
        accountManagement()
        mainBinding.sideBar.btGoogleSideBAr.setOnClickListener {
            accountManagement()
        }

        mainBinding.sideBar.btGoogleSideBAr2AccountManagement.setOnClickListener {
            //TODO: управление аккаунтами гугл
        }

        mainBinding.sideBar.btGoogleSideBAr2Sync.setOnClickListener {
            //TODO: синхорнизировать задачи с гугл аккаунтом.
            //для разреешения коллизий -- наше приложение в приоритете
        }

        setCurrentThemeIcon()
        setLanguageRadioButton()

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

//        ltstViewModel = ViewModelProvider(this)[LTSTViewModel::class.java]
//        ltstViewModel.readTasks.observe(viewLifecycleOwner) { }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        val fragment = currentFragment

        menu?.clear()
        if (fragment is HasMainScreenActions) {
            inflater.inflate(R.menu.main_menu, menu)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu)
        } else if (fragment is HasDeleteAction) {
            inflater.inflate(R.menu.menu_trashbox, menu)
            if (currentFragment is ListsSettingsFragment) {
                menu!![0].isVisible = false
            }
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun showMainTaskScreen(subtasksCount: Int) {
        val randomSubtaskList = randomData.getRandomSubtasks(subtasksCount)
        launchFragment(TaskFragment.newInstance(randomSubtaskList))
    }

    override fun showSubtaskScreen() {
        launchFragment(SubtaskFragment.newInstance(RandomData.getRandomSubtask()))
    }

    override fun showListSettingsScreen() {
        launchFragment(ListsSettingsFragment.newInstance(listOfLists))
        mainBinding.drawer.closeDrawer(Gravity.LEFT)
    }

    override fun goToMainScreen() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(RESULT_KEY to result)
        )
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .replace(mainBinding.mainContainer.id, fragment, "")
            .commit()
    }

    private fun updateUi() {
        val fragment = currentFragment
        if (fragment is HasCustomTitle) {
            supportActionBar?.title = (fragment as HasCustomTitle).getCustomTitle()
        } else {
            supportActionBar?.title = getString(R.string.app_name)
        }
        onCreateOptionsMenu(mainBinding.toolbar.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.clear_checked -> {
                clearCheckedTasks()
            }
            item.itemId == R.id.sort_by_date -> {
                updateTasksOrder()
                sortOrder = SortOrder.BY_DATE
            }
            item.itemId == R.id.sort_by_name -> {
                updateTasksOrder()
                sortOrder = SortOrder.BY_NAME
            }
            item.itemId == R.id.sort_by_importance -> {
                updateTasksOrder()
                sortOrder = SortOrder.BY_IMPORTANCE
            }
            item.itemId == android.R.id.home && currentFragment is TaskListContainerFragment -> {
                mainBinding.drawer.openDrawer(GravityCompat.START)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun clearCheckedTasks() {
        // TODO()
    }

    private fun updateTasksOrder() {
        // TODO()
    }

    fun onClickListenerButtonDayNight(view: View) {
        toggleButtonImageDayNight()
    }

    private fun toggleButtonImageDayNight() {
        isDay = !isDay
        updateButtonImageDayNight()
    }

    private fun setCurrentThemeIcon() {
        val imgBt = findViewById<ImageButton>(R.id.btSwitcherTheme)
        if (isDay) {
            imgBt.setImageResource(R.drawable.baseline_wb_sunny_32)
        } else {
            imgBt.setImageResource(R.drawable.baseline_nights_stay_32)
        }
    }

    private fun updateButtonImageDayNight() {
        preference.edit()
            .putBoolean(IS_DEFAULT_THEME_KEY, isDay)
            .apply()
        recreate()
    }

    private fun setLanguageRadioButton() {
        val languageCode = preference.getInt(LANGUAGE_KEY, -1)

        val currLang = Locale.getDefault().language
        if (languageCode < 1 && currLang == "en" || languageCode == 0 && currLang == "ru") {
            mainBinding.sideBar.radioButtonEn.isChecked = true
        }

        if (languageCode == 0 && currLang == "ru") {
            setLocaleLanguage(this, "en")
        } else if (languageCode > 0 && currLang == "en") {
            setLocaleLanguage(this, "ru")
        }
    }

    private fun setLocaleLanguage(activity: Activity, languageStringCode: String?) {
        if (Locale.getDefault().language == languageStringCode) {
            return
        }
        preference.edit()
            .putInt(
                LANGUAGE_KEY,
                if (languageStringCode == "ru") 1 else 0
            ).apply()

        val locale = languageStringCode?.let { Locale(it) }
        if (locale != null) {
            Locale.setDefault(locale)
        }
        val resources: Resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }

    fun rotateFab(view: View, rotate: Boolean): Boolean {
        view.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {})
            .rotation(if (!rotate) 180f else 0f)
        return rotate
    }

    private fun accountManagement() {
        Log.e("accountManagement", "managementHidden $managementHidden")
        if (authorized) {
            managementHidden = rotateFab(mainBinding.sideBar.accountSelectAction, !managementHidden)
            mainBinding.sideBar.managementGroup.visibility = when {
                managementHidden -> View.GONE
                else -> View.VISIBLE
            }
            Log.e("accountManagement", "managementHidden $managementHidden")
        } else {
            //авторизация -- войти
        }
    }


    private fun setAccountButton() {
        if(authorized){
            //TODO: получить имя пользователя
            val accountName = null
            mainBinding.sideBar.googleAccount.text = accountName
        } else {
            mainBinding.sideBar.accountSelectAction.visibility = View.GONE
            mainBinding.sideBar.managementGroup.visibility = View.GONE
            mainBinding.sideBar.accountImage.setImageResource(R.drawable.google)
            mainBinding.sideBar.googleAccount.setText(R.string.continue_with_google)
        }
    }

    private fun startAuthorization() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }


//    private fun insertExample() {
//        ltstViewModel.addList(
//            com.techno_3_team.task_manager.data.entities.List(
//                1,
//                "list_1"
//            )
//        )
//        ltstViewModel.addList(
//            com.techno_3_team.task_manager.data.entities.List(
//                2,
//                "list_2"
//            )
//        )
//        ltstViewModel.addList(
//            com.techno_3_team.task_manager.data.entities.List(
//                3,
//                "list_3"
//            )
//        )
//        ltstViewModel.addTask(
//            Task(
//                1,
//                2,
//                "first",
//                false,
//                null,
//                "komafdsg",
//                null, null
//            )
//        )
//        ltstViewModel.addTask(
//            Task(
//                2,
//                2,
//                "second",
//                true,
//                null,
//                "dsafnjkasdf",
//                null, null
//            )
//        )
//    }

}
