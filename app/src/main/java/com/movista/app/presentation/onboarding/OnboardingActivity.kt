package com.movista.app.presentation.onboarding

import android.R
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.WindowManager
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage
import com.movista.app.App
import com.movista.app.presentation.common.Screens
import com.movista.app.presentation.main.MainActivity
import org.jetbrains.anko.startActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import javax.inject.Inject

class OnboardingActivity : AppIntro() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = createNavigator()

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)

        App.appComponent.inject(this)

        initUI()
    }

    override fun onResume() {
        super.onResume()
        getNavigationHolder().setNavigator(getNavigator())
    }

    override fun onPause() {
        getNavigationHolder().removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        router.exit()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        gotoMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        gotoMainActivity()
    }

    private fun initUI() {
        setSeparatorColor(Color.TRANSPARENT)

        val sliderPageOne = SliderPage()
        sliderPageOne.title = "Title One"
        sliderPageOne.description = "This is a description!"
        sliderPageOne.imageDrawable = R.drawable.ic_menu_send
        sliderPageOne.bgColor = Color.parseColor("#03A9F4")


        val sliderPageTwo = SliderPage()
        sliderPageTwo.title = "Title Two"
        sliderPageTwo.imageDrawable = R.drawable.ic_menu_view
        sliderPageTwo.bgColor = Color.parseColor("#FF7043")

        val sliderPageThree = SliderPage()
        sliderPageThree.title = "Title Three"
        sliderPageThree.bgColor = Color.parseColor("#EC407A")

        addSlide(AppIntroFragment.newInstance(sliderPageOne))
        addSlide(AppIntroFragment.newInstance(sliderPageTwo))
        addSlide(AppIntroFragment.newInstance(sliderPageThree))
    }

    private fun gotoMainActivity() {
        router.newRootScreen(Screens.MAIN)
    }

    private fun getNavigationHolder(): NavigatorHolder = navigatorHolder

    private fun getNavigator(): Navigator = navigator

    private fun createNavigator(): Navigator {

        return object : Navigator {

            override fun applyCommands(commands: Array<Command>) {
                for (command in commands) applyCommand(command)
            }

            fun applyCommand(command: Command) {
                when (command) {
                    is Back -> finish()
                    is Replace -> {
                        startActivity<MainActivity>()
                        finish()
                    }
                }
            }
        }
    }
}
