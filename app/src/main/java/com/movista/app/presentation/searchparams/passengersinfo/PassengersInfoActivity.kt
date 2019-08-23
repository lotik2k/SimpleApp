package com.movista.app.presentation.searchparams.passengersinfo

import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.flexbox.FlexboxLayout
import com.movista.app.R
import com.movista.app.presentation.base.BaseActivity
import com.movista.app.utils.EMPTY
import com.movista.app.utils.toPx
import kotlinx.android.synthetic.main.activity_passengers_info.*
import kotlinx.android.synthetic.main.item_choose_count.view.*
import kotlin.math.roundToInt

class PassengersInfoActivity : BaseActivity(), PassengersInfoView {

    private var childInfoDialog: AlertDialog? = null
    private var comfortTypesDialog: BottomSheetDialog? = null

    @InjectPresenter
    lateinit var presenter: PassengersInfoPresenter

    override fun getLayoutRes() = R.layout.activity_passengers_info

    override fun onActivityInject() {

//        App.appComponent.inject(this)
    }

    override fun onPause() {
        childInfoDialog?.dismiss()
        comfortTypesDialog?.dismiss()
        super.onPause()
    }

    override fun initUI() {
        super.initUI()

        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.back_button_black)
            setTitle(R.string.title_passengers_info)
            elevation = 0f
            setDisplayHomeAsUpEnabled(true)
        }

        choose_comfort_type.setOnClickListener {
            presenter.onChooseComfortTypeClicked()
        }

        passengers_choose_adult_count.increase_count.setOnClickListener {
            presenter.onIncreaseAdultCountClicked()
        }

        passengers_choose_adult_count.decrease_count.setOnClickListener {
            presenter.onDecreaseAdultCountClicked()
        }

        passengers_choose_children_count.increase_count.setOnClickListener {
            presenter.onIncreaseChildrenCountClicked()
        }

        passengers_choose_children_count.decrease_count.setOnClickListener {
            presenter.onDecreaseChildrenCountClicked()
        }

        passengers_done_button.setOnClickListener {
            presenter.onDoneButtonClicked()
        }

        passengers_more_info.setOnClickListener {
            presenter.onPassengersMoreInfoClicked()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    override fun showChooseComfortTypeDialog(comfortTypes: Array<String>, selected: Int) {
        val view = View.inflate(this, R.layout.dialog_passengers_comfort_type, null)
        comfortTypesDialog = BottomSheetDialog(this)

        val first = view.findViewById<RadioButton>(R.id.first_comfort_type)
        val second = view.findViewById<RadioButton>(R.id.second_comfort_type)
        val third = view.findViewById<RadioButton>(R.id.third_comfort_type)
        val fourth = view.findViewById<RadioButton>(R.id.fourth_comfort_type)

        first.text = comfortTypes[0]
        second.text = comfortTypes[1]
        third.text = comfortTypes[2]
        fourth.text = comfortTypes[3]

        when (selected) {
            0 -> first.isChecked = true
            1 -> second.isChecked = true
            2 -> third.isChecked = true
            3 -> fourth.isChecked = true
        }

        view.findViewById<RadioGroup>(R.id.comfort_types_radio_group).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                -1 -> { /*ничего не выбрано*/
                }
                first.id -> presenter.onComfortTypeSelected(0)
                second.id -> presenter.onComfortTypeSelected(1)
                third.id -> presenter.onComfortTypeSelected(2)
                fourth.id -> presenter.onComfortTypeSelected(3)
            }

            comfortTypesDialog?.dismiss()
        }
        comfortTypesDialog?.setContentView(view)
        comfortTypesDialog?.show()
    }

    override fun showPassengersInfo(passengersInfoViewModel: PassengersInfoViewModel) {

        with(passengersInfoViewModel) {
            passengers_choose_adult_count.count_text.text = adultCount.toString()
            passengers_choose_children_count.count_text.text = childrenCount.toString()
            choose_comfort_type.text = comfortTypeLabel

            if (children_info_layout.childCount > 0) children_info_layout.removeAllViews()

            (0 until childrenCount).forEach { i ->
                val childInfo = childrenInfo[i]

                if (childInfo.infoEdited) {
                    val childInfoText = resources.getString(
                            R.string.child_info_edited,
                            childInfo.ageLabel,
                            childInfo.seatLabel
                    )

                    addChildInfoButton(
                            i,
                            R.layout.item_child_info_edited_button,
                            childInfoText
                    )
                } else {
                    addChildInfoButton(i, R.layout.item_add_child_count_button, null)
                }
            }
        }
    }

    override fun disableIncreaseAdultCountIf(condition: Boolean) {
        val increaseButton = passengers_choose_adult_count.increase_count
        disableButtonIf(increaseButton, condition)
    }

    override fun disableIncreaseChildrenCountIf(condition: Boolean) {
        val increaseButton = passengers_choose_children_count.increase_count
        disableButtonIf(increaseButton, condition)
    }

    override fun disableDecreaseAdultCountIf(condition: Boolean) {
        val decreaseButton = passengers_choose_adult_count.decrease_count
        disableButtonIf(decreaseButton, condition)
    }

    override fun disableDecreaseChildrenCountIf(condition: Boolean) {
        val decreaseButton = passengers_choose_children_count.decrease_count
        disableButtonIf(decreaseButton, condition)
    }

    override fun deleteChildInfoButton(id: Int) {
        children_info_layout.removeView(findViewById(id))
    }


    override fun showChildInfoDialog(
            id: Int,
            childInfo: ChildInfo,
            minAge: Int,
            maxAge: Int,
            disableIsSeatRequired: Pair<Boolean, Boolean>
    ) {

        val dialogView = layoutInflater.inflate(R.layout.dialog_child_info, null)

        val isSeatRequiredLabel = dialogView.findViewById<TextView>(R.id.dialog_child_is_seat_required_label)
        val isSeatRequiredSwitch = dialogView.findViewById<Switch>(R.id.dialog_child_info_is_seat_required)

        val dialogContent = dialogView.findViewById<ConstraintLayout>(R.id.dialog_child_info_age)
        val dialogAge = dialogContent.findViewById<TextView>(R.id.count_text)
        val dialogIncreaseAge = dialogContent.findViewById<ImageButton>(R.id.increase_count)
        val dialogDecreaseAge = dialogContent.findViewById<ImageButton>(R.id.decrease_count)

        when (childInfo.age) {
            minAge -> {
                dialogAge.text = "<1"
                disableButtonIf(dialogDecreaseAge, true)
            }

            maxAge -> {
                dialogAge.text = maxAge.toString()
                disableButtonIf(dialogIncreaseAge, true)
            }

            else -> {
                dialogAge.text = childInfo.age.toString()
                disableButtonIf(dialogDecreaseAge, false)
                disableButtonIf(dialogIncreaseAge, false)
            }
        }

        disableIsSeatRequiredIf(disableIsSeatRequired.first, isSeatRequiredLabel, isSeatRequiredSwitch)
        if (disableIsSeatRequired.first) {
            isSeatRequiredSwitch.isChecked = disableIsSeatRequired.second
        } else {
            isSeatRequiredSwitch.isChecked = childInfo.seatRequired
        }


        dialogIncreaseAge.setOnClickListener {
            onChangeChildAgeClicked(
                    true,
                    dialogAge,
                    dialogAge.text.toString(),
                    dialogIncreaseAge,
                    dialogDecreaseAge,
                    minAge,
                    maxAge,
                    isSeatRequiredLabel,
                    isSeatRequiredSwitch
            )
        }

        dialogDecreaseAge.setOnClickListener {
            onChangeChildAgeClicked(
                    false,
                    dialogAge,
                    dialogAge.text.toString(),
                    dialogIncreaseAge,
                    dialogDecreaseAge,
                    minAge,
                    maxAge,
                    isSeatRequiredLabel,
                    isSeatRequiredSwitch
            )
        }

        childInfoDialog = AlertDialog.Builder(this)
                .setPositiveButton(
                        "OK"
                ) { _, _ ->
                    presenter.onConfirmNewChildInfoClicked(
                            id,
                            dialogAge.text.toString(),
                            isSeatRequiredSwitch.isChecked
                    )
                }
                .create()


        childInfoDialog?.setView(dialogView)
        childInfoDialog?.show()
    }

    override fun showAgeCategoryInfoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_age_categories_info, null)

        val ageDialog = AlertDialog.Builder(this)
                .setTitle("Возрастные категории")
                .setPositiveButton(
                        "закрыть"
                ) { _, _ -> }
                .create()


        ageDialog?.setView(dialogView)
        ageDialog?.show()
    }

    override fun navigateBack() {
        onSupportNavigateUp()
    }

    override fun showError(error: String) {
        snackbar.show(messageText = error)
    }

    private fun addChildInfoButton(id: Int, @LayoutRes layout: Int, childInfoText: String?) {
        val button = createChildInfoButton(layout)
        button.id = id
        if (childInfoText != null) button.text = childInfoText
        button.setOnClickListener { presenter.onAddChildInfoClick(button.id) }
        children_info_layout.addView(button)
    }

    private fun createChildInfoButton(@LayoutRes layout: Int): Button {

        val button = layoutInflater.inflate(layout, null) as Button

        val params = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                32.toPx().roundToInt()
        )
        params.setMargins(0, 8.toPx().roundToInt(), 8.toPx().roundToInt(), 0)
        button.layoutParams = params

        return button
    }

    private fun disableButtonIf(view: View, condition: Boolean) {
        with(view) {
            if (condition) {
                alpha = 0.3f
                isEnabled = false
            } else {
                alpha = 1.0f
                isEnabled = true
            }
        }
    }

    // todo refactor
    private fun onChangeChildAgeClicked(
            onIncreaseClicked: Boolean,
            dialogAge: TextView,
            currentAge: String,
            increaseButton: ImageButton,
            decreaseButton: ImageButton,
            minAge: Int,
            maxAge: Int,
            seatRequiredText: TextView,
            seatRequiredSwitch: Switch
    ) {
        val age = if (currentAge == "<1") minAge else currentAge.toInt()
        if (onIncreaseClicked) {
            dialogAge.text = when (age) {
                minAge -> {
                    disableButtonIf(decreaseButton, false)
                    disableIsSeatRequiredIf(false, seatRequiredText, seatRequiredSwitch)
                    "1"
                }
                in (minAge - 1)..(maxAge - 2) -> {
                    disableButtonIf(decreaseButton, false)
                    if (age > 5) {
                        seatRequiredSwitch.isChecked = true
                        disableIsSeatRequiredIf(true, seatRequiredText, seatRequiredSwitch)
                    } else {
                        disableIsSeatRequiredIf(false, seatRequiredText, seatRequiredSwitch)
                    }
                    (age + 1).toString()
                }
                maxAge - 1 -> {
                    disableButtonIf(increaseButton, true)
                    seatRequiredSwitch.isChecked = true
                    disableIsSeatRequiredIf(true, seatRequiredText, seatRequiredSwitch)
                    (age + 1).toString()
                }
                else -> String.EMPTY
            }
        } else {
            dialogAge.text = when (age) {
                minAge + 1 -> {
                    disableButtonIf(decreaseButton, true)
                    seatRequiredSwitch.isChecked = false
                    disableIsSeatRequiredIf(true, seatRequiredText, seatRequiredSwitch)
                    "<1"
                }
                in (minAge + 2)..maxAge -> {
                    disableButtonIf(increaseButton, false)
                    if (age < 8) {
                        disableIsSeatRequiredIf(false, seatRequiredText, seatRequiredSwitch)
                    } else {
                        seatRequiredSwitch.isChecked = true
                        disableIsSeatRequiredIf(true, seatRequiredText, seatRequiredSwitch)
                    }
                    (age - 1).toString()
                }
                else -> String.EMPTY
            }
        }
    }

    private fun disableIsSeatRequiredIf(condition: Boolean, text: TextView, switch: Switch) {
        if (condition) {
            text.alpha = 0.3f
            switch.alpha = 0.3f
            switch.isClickable = false
        } else {
            text.alpha = 1.0f
            switch.alpha = 1.0f
            switch.isClickable = true
        }

    }
}