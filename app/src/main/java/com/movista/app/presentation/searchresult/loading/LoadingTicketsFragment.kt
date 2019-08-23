package com.movista.app.presentation.searchresult.loading

import android.animation.*
import android.content.res.Resources
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.movista.app.R
import com.movista.app.presentation.base.BaseFragment
import com.movista.app.utils.toPx
import kotlinx.android.synthetic.main.fragment_loading_tickets.*

class LoadingTicketsFragment : BaseFragment() {

    companion object {

        const val ALPHA_TRANSPARENT = 0f
        const val ALPHA_FULL = 1f
        const val START_DELAY = 140L
        const val END_DELAY = 220L
        const val WITHOUT_DELAY = 0L

        fun newInstance(): LoadingTicketsFragment = LoadingTicketsFragment()
    }

    lateinit var startAnimatorSet: AnimatorSet
    lateinit var endAnimatorSet: AnimatorSet

    override fun getLayoutRes() = R.layout.fragment_loading_tickets

    override fun onFragmentInject() {}

    override fun onDestroyView() {
        startAnimatorSet.removeAllListeners()
        startAnimatorSet.cancel()
        endAnimatorSet.removeAllListeners()
        endAnimatorSet.cancel()
        super.onDestroyView()
    }

    override fun initUI() {
        super.initUI()

        val screenWidth = Resources.getSystem().displayMetrics.widthPixels.toFloat()

        changeDotsAlpha(ALPHA_TRANSPARENT)

        val startTranslationValue = resources.getDimension(R.dimen.size_medium)
        val endTranslationValue = screenWidth - startTranslationValue + resources.getDimension(R.dimen.size_medium).toInt().toPx()

        val xStartHolder = PropertyValuesHolder.ofFloat("translationX", startTranslationValue)
        val alphaStartHolder = PropertyValuesHolder.ofFloat("alpha", ALPHA_FULL)
        val xEndHolder = PropertyValuesHolder.ofFloat("translationX", endTranslationValue)
        val alphaEndHolder = PropertyValuesHolder.ofFloat("alpha", ALPHA_TRANSPARENT)

        val firstStartAnimator = prepareStartAnimator(
                first_dot,
                xStartHolder,
                alphaStartHolder,
                START_DELAY * 4
        )
        val secondStartAnimator = prepareStartAnimator(second_dot,
                xStartHolder,
                alphaStartHolder,
                START_DELAY * 3
        )
        val thirdStartAnimator = prepareStartAnimator(
                third_dot,
                xStartHolder,
                alphaStartHolder,
                START_DELAY * 2
        )
        val fourthStartAnimator = prepareStartAnimator(
                fourth_dot,
                xStartHolder,
                alphaStartHolder,
                START_DELAY
        )
        val fifthStartAnimator = prepareStartAnimator(
                fifth_dot,
                xStartHolder,
                alphaStartHolder,
                WITHOUT_DELAY
        )

        startAnimatorSet = AnimatorSet()
        startAnimatorSet.playTogether(
                fifthStartAnimator,
                fourthStartAnimator,
                thirdStartAnimator,
                secondStartAnimator,
                firstStartAnimator
        )

        val firstEndAnimator = prepareEndAnimator(
                first_dot,
                xEndHolder,
                alphaEndHolder,
                END_DELAY * 4
        )
        val secondEndAnimator = prepareEndAnimator(
                second_dot,
                xEndHolder,
                alphaEndHolder, END_DELAY * 3
        )
        val thirdEndAnimator = prepareEndAnimator(
                third_dot,
                xEndHolder,
                alphaEndHolder, END_DELAY * 2
        )
        val fourthEndAnimator = prepareEndAnimator(
                fourth_dot,
                xEndHolder,
                alphaEndHolder, END_DELAY
        )
        val fifthEndAnimator = prepareEndAnimator(
                fifth_dot,
                xEndHolder,
                alphaEndHolder, WITHOUT_DELAY
        )

        endAnimatorSet = AnimatorSet()
        endAnimatorSet.playTogether(
                fifthEndAnimator,
                fourthEndAnimator,
                thirdEndAnimator,
                secondEndAnimator,
                firstEndAnimator
        )

        startAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                endAnimatorSet.start()
            }
        })

        endAnimatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                first_dot.alpha = 0f
                second_dot.alpha = 0f
                first_dot.translationX = -(startTranslationValue + endTranslationValue)
                second_dot.translationX = -(startTranslationValue + endTranslationValue)

                startAnimatorSet.start()
            }
        })

        startAnimatorSet.start()
    }

    private fun changeDotsAlpha(alpha: Float) {
        first_dot.alpha = alpha
        second_dot.alpha = alpha
        third_dot.alpha = alpha
        fourth_dot.alpha = alpha
        fifth_dot.alpha = alpha
    }

    private fun prepareStartAnimator(
            view: View,
            translationXHolder: PropertyValuesHolder,
            alphaHolder: PropertyValuesHolder,
            delay: Long
    ): Animator {

        val animator = ObjectAnimator.ofPropertyValuesHolder(
                view,
                translationXHolder,
                alphaHolder
        )
        animator.startDelay = delay
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 500
        return animator
    }

    private fun prepareEndAnimator(
            view: View,
            translationXHolder: PropertyValuesHolder,
            alphaHolder: PropertyValuesHolder,
            delay: Long
    ): Animator {

        val animator = ObjectAnimator.ofPropertyValuesHolder(
                view,
                translationXHolder,
                alphaHolder
        )
        animator.startDelay = delay
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 1000
        return animator
    }
}
