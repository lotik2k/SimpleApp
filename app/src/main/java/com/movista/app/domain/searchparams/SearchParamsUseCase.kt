package com.movista.app.domain.searchparams

import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.domain.model.*
import com.movista.app.presentation.common.ComfortType
import com.movista.app.utils.EMPTY
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList

class SearchParamsUseCase(
        private val repository: ISearchParamsRepository
) :
        ISearchParamsUseCase,
        IDateSelectUseCase,
        ITripTypesUseCase,
        IPlaceSearchUseCase,
        IPassengersUseCase,
        IComfortTypeUseCase {

    private var searchModelPublishSubject: PublishSubject<SearchModel> = PublishSubject.create()
    private var passengersInfoPublishSubject: PublishSubject<PassengersInfoModel> = PublishSubject.create()
    private val searchModel = repository.getSearchModel()
    private lateinit var passengersInfoModel: PassengersInfoModel
    private lateinit var localPassengerModel: PassengersInfoModel

    /**
     * Инициализация модели поиска маршрутов со значениями по-умолчанию
     */
    override fun initSearchParams(): SearchModel {

        initPassengersInfoParams()

        with(searchModel) {
            isBusSelected = true
            isPlainSelected = false
            isPlainSelected = true
            fromDate = Calendar.getInstance().time // сегодня
            passengers = passengersInfoModel.passengers
            comfortType = passengersInfoModel.comfortType
        }

        return searchModel
    }

    override fun setTripTypes() {

    }


    override fun setFromCity(name: String, lat: Double, lon: Double) {
        with(searchModel) {
            fromCity = PlaceModel(lat, lon, name, String.EMPTY)
        }
        searchModelSubjectCallOnNext()
    }

    override fun setToCity(name: String, lat: Double, lon: Double) {
        with(searchModel) {
            toCity = PlaceModel(lat, lon, name, String.EMPTY)
        }
        searchModelSubjectCallOnNext()
    }


    override fun setTripDate(from: Date, to: Date?) {
        searchModel.fromDate = from
        searchModel.toDate = to
        searchModelSubjectCallOnNext()
    }

    override fun getToday(): Date {
        return Calendar.getInstance().time
    }

    override fun setPassengersInfo() {

    }

    override fun observeSearchParamsUpdate(): PublishSubject<SearchModel> {
        return searchModelPublishSubject
    }

    override fun search(chars: String): Single<SearchResponse> {

        return repository.search(chars)
    }

    override fun getPlaceInfo(latitude: Double, longitude: Double): Single<SearchResponse> {
        return repository.getPlaceInfo(latitude, longitude)
    }

    override fun getPopular(): Single<PopularResponse> {

        return repository.getPopular()
    }

    override fun requestPassengersModel() {
        val passengersCopy = copyPassengersList(passengersInfoModel)
        localPassengerModel = passengersInfoModel.copy(passengers = passengersCopy) // сохраняем локально начальное состояние
        passengersModelSubjectCallOnNext()
    }


    override fun observePassengersInfoParamsUpdate(): PublishSubject<PassengersInfoModel> {
        return passengersInfoPublishSubject
    }

    override fun increaseAdultCount() {

        if (localPassengerModel.maxCountReached) return

        val adults = getAdults()
        adults.add(AdultPassengerModel())
        updateAdults(adults)

        checkIfMaxCountReached()
        localPassengerModel.isMinAdultCount = false

        passengersModelSubjectCallOnNext()
    }

    override fun decreaseAdultCount() {

        if (localPassengerModel.isMinAdultCount) return

        val adults = getAdults()
        if (adults.isNotEmpty()) adults.removeAt(adults.size - 1)
        updateAdults(adults)

        checkIfMinAdultCountReached(adults)
        localPassengerModel.maxCountReached = false

        passengersModelSubjectCallOnNext()
    }

    override fun increaseChildrenCount() {

        if (localPassengerModel.maxCountReached) return

        val children = getChildren()
        children.add(ChildPassengerModel())
        updateChildren(children)

        checkIfMaxCountReached()
        localPassengerModel.isMinChildCount = false

        passengersModelSubjectCallOnNext()
    }

    override fun decreaseChildrenCount() {

        if (localPassengerModel.isMinChildCount) return

        val children = getChildren()

        if (children.any { it.age != null }) {
            if (children.all { it.age != null }) {
                // у всех детей заполнены дынные
                children.removeAt(children.size - 1)
            } else {
                // удалить последнего добавленного, чьи данные не заполнены
                children.remove(children.last { it.age == null })
            }
        } else {
            // нет детей с заполненными данными
            children.removeAt(children.size - 1)
        }

        updateChildren(children)

        checkIfMinChildrenCountReached(children)
        localPassengerModel.maxCountReached = false

        passengersModelSubjectCallOnNext()
    }

    override fun setComfortType(comfortType: ComfortType) {
        localPassengerModel.comfortType = comfortType
        passengersModelSubjectCallOnNext()
    }

    override fun notifyNewPassengersInfoConfirmed() {
        val passengers = copyPassengersList(localPassengerModel)
        passengersInfoModel = localPassengerModel.copy(passengers = passengers)
        searchModel.passengers = passengersInfoModel.passengers
        searchModel.comfortType = passengersInfoModel.comfortType
        searchModelSubjectCallOnNext()
    }

    override fun updateChildInfo(index: Int, age: Int, isSeatRequired: Boolean) {

        val childrenInfos = localPassengerModel.passengers.filter { it is ChildPassengerModel }
        val childInfo = childrenInfos[index] as ChildPassengerModel
        childInfo.age = age
        childInfo.isSeatRequired = isSeatRequired

        passengersModelSubjectCallOnNext()
    }

    private fun initPassengersInfoParams() {

        val passenger = AdultPassengerModel() // 1 взрослый пассажир

        passengersInfoModel = PassengersInfoModel(
                arrayListOf(passenger),
                ComfortType.ECONOMY,
                false,
                true,
                true
        )

    }

    private fun getPassengers(): ArrayList<PassengerModel> {
        return localPassengerModel.passengers
    }

    private fun getAdults(): ArrayList<PassengerModel> {
        val passengers = getPassengers()
        return passengers.filter { it is AdultPassengerModel } as ArrayList
    }

    private fun updateAdults(adults: ArrayList<PassengerModel>) {
        val passengers = getPassengers()
        passengers.removeAll { it is AdultPassengerModel }
        passengers.addAll(adults)
    }

    private fun getChildren(): ArrayList<PassengerModel> {
        val passengers = getPassengers()
        return passengers.filter { it is ChildPassengerModel } as ArrayList
    }

    private fun updateChildren(children: ArrayList<PassengerModel>) {
        val passengers = getPassengers()
        passengers.removeAll { it is ChildPassengerModel }
        passengers.addAll(children)
    }

    private fun searchModelSubjectCallOnNext() {
        searchModelPublishSubject.onNext(searchModel)
    }

    private fun passengersModelSubjectCallOnNext() {
        passengersInfoPublishSubject.onNext(localPassengerModel)
    }

    private fun checkIfMaxCountReached() {

        localPassengerModel.maxCountReached = localPassengerModel.passengers.size == 4
    }

    private fun checkIfMinAdultCountReached(adults: List<PassengerModel>) {
        localPassengerModel.isMinAdultCount = adults.size < 2
    }

    private fun checkIfMinChildrenCountReached(children: List<PassengerModel>) {
        localPassengerModel.isMinChildCount = children.isEmpty()
    }


    private fun copyPassengersList(passengersInfoModel: PassengersInfoModel): ArrayList<PassengerModel> {
        val passengersCopy = ArrayList<PassengerModel>()
        passengersInfoModel.passengers.forEach {
            when (it) {
                is AdultPassengerModel -> passengersCopy.add(it.copy() as PassengerModel)
                is ChildPassengerModel -> passengersCopy.add(it.copy() as PassengerModel)
            }
        }
        return passengersCopy
    }

}