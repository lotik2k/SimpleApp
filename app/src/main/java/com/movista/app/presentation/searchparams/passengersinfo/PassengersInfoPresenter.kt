package com.movista.app.presentation.searchparams.passengersinfo

import android.content.res.Resources
import com.arellomobile.mvp.InjectViewState
import com.movista.app.App
import com.movista.app.R
import com.movista.app.domain.model.PassengersInfoModel
import com.movista.app.domain.searchparams.IComfortTypeUseCase
import com.movista.app.domain.searchparams.IPassengersUseCase
import com.movista.app.presentation.base.BasePresenter
import com.movista.app.presentation.common.ComfortType
import com.movista.app.presentation.converter.SearchConverter
import ru.terrakok.cicerone.Router
import javax.inject.Inject


@InjectViewState
class PassengersInfoPresenter : BasePresenter<PassengersInfoView>() {

    companion object {
        const val MIN_CHILD_AGE = 0
        const val MAX_CHILD_AGE = 17
        const val CHILD_AGE_CHOOSE_SEAT_DISABLED = 7
    }

    private val childInfoEditedIds = ArrayList<Int>()
    private lateinit var passengersInfoViewModel: PassengersInfoViewModel

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var passengersUseCase: IPassengersUseCase

    @Inject
    lateinit var comfortTypeUseCase: IComfortTypeUseCase

    @Inject
    lateinit var converter: SearchConverter

    @Inject
    lateinit var resources: Resources

    init {
        App.appComponent.inject(this)

        addDisposable(
                passengersUseCase.observePassengersInfoParamsUpdate()
                        .subscribe(
                                {
                                    onNewPassengersInfoReceived(it)
                                },
                                {}
                        )
        )

        passengersUseCase.requestPassengersModel()
    }

    fun onChooseComfortTypeClicked() {
        viewState.showChooseComfortTypeDialog(
                resources.getStringArray(R.array.comfort_types),
                passengersInfoViewModel.comfortType.ordinal
        )
    }


    fun onComfortTypeSelected(id: Int) {
        when (id) {
            0 -> comfortTypeUseCase.setComfortType(ComfortType.ECONOMY)
            1 -> comfortTypeUseCase.setComfortType(ComfortType.PREMIUM_ECONOMY)
            2 -> comfortTypeUseCase.setComfortType(ComfortType.BUSINESS)
            3 -> comfortTypeUseCase.setComfortType(ComfortType.FIRST_CLASS)
        }

    }

    fun onIncreaseAdultCountClicked() {
        passengersUseCase.increaseAdultCount()
    }

    fun onDecreaseAdultCountClicked() {
        passengersUseCase.decreaseAdultCount()
    }

    fun onIncreaseChildrenCountClicked() {
        passengersUseCase.increaseChildrenCount()
    }

    fun onDecreaseChildrenCountClicked() {
        passengersUseCase.decreaseChildrenCount()
    }

    fun onAddChildInfoClick(id: Int) {

        val age = passengersInfoViewModel.childrenInfo[id].age
        val shoudDisableIsSeatRequiredToggle = age == MIN_CHILD_AGE || age > CHILD_AGE_CHOOSE_SEAT_DISABLED

        viewState.showChildInfoDialog(
                id,
                passengersInfoViewModel.childrenInfo[id],
                MIN_CHILD_AGE,
                MAX_CHILD_AGE,
                Pair(shoudDisableIsSeatRequiredToggle, age > CHILD_AGE_CHOOSE_SEAT_DISABLED)
        )
    }

    fun onConfirmNewChildInfoClicked(id: Int, age: String, isSeatRequired: Boolean) {
        childInfoEditedIds.add(id)
        val ageInt = if (age == "<1") MIN_CHILD_AGE else age.toInt()
        passengersUseCase.updateChildInfo(id, ageInt, isSeatRequired)
    }

    fun onPassengersMoreInfoClicked() {
        viewState.showAgeCategoryInfoDialog()
    }

    fun onDoneButtonClicked() {

        if (allChildInfoEdited()) {
            passengersUseCase.notifyNewPassengersInfoConfirmed()
            viewState.navigateBack()
        } else {
            viewState.showError(resources.getString(R.string.error_passengers_no_child_age))
        }
    }

    private fun onNewPassengersInfoReceived(passengersInfoModel: PassengersInfoModel) {
        checkDisableNeeded(passengersInfoModel)

        val initPassengersInfoViewModel = converter.toPassengersInfoViewModel(passengersInfoModel, MIN_CHILD_AGE)
        passengersInfoViewModel = initPassengersInfoViewModel
        applyToView()
    }

    private fun applyToView() {
        viewState.showPassengersInfo(passengersInfoViewModel)
    }

    private fun checkDisableNeeded(passengersInfoModel: PassengersInfoModel) {

        viewState.disableIncreaseAdultCountIf(passengersInfoModel.maxCountReached)
        viewState.disableIncreaseChildrenCountIf(passengersInfoModel.maxCountReached)
        viewState.disableDecreaseAdultCountIf(passengersInfoModel.isMinAdultCount)
        viewState.disableDecreaseChildrenCountIf(passengersInfoModel.isMinChildCount)
    }

    private fun allChildInfoEdited(): Boolean {

        val childInfo = passengersInfoViewModel.childrenInfo
        return childInfo.all { it.infoEdited }
    }
}