package com.movista.app.data.entity

import com.squareup.moshi.Json

/**
 * Data-классы для работы со списком маршрутов
 */

data class Passenger(

        @Json(name = "age")
        val age: Int = 18,

        @Json(name = "seatRequired")
        val seatRequired: Boolean = true
)

// для TripsRequestModel
data class Params(

        @Json(name = "departure")
        val departure: String,

        @Json(name = "tripTypes")
        val tripTypes: List<String> = listOf("flight", "train", "train"),

        @Json(name = "placeFromId")
        val placeFromId: Int = 65537, // Moscow

        @Json(name = "placeToId")
        val placeToId: Int = 83130, // SaintP-b

        @Json(name = "passengers")
        val passengers: List<Passenger>
)

data class Paging(

        @Json(name = "order")
        val order: List<Order>,

        @Json(name = "limit")
        val limit: Int = 15,

        @Json(name = "offset")
        val offset: Int = 0
)

data class Order(

        @Json(name = "by")
        val by: String = "departure",

        @Json(name = "type")
        val type: String = "asc"
)

data class Departure(

        @Json(name = "daysFilter")
        val daysFilter: List<DaysFilter> = emptyList()
)

data class Filters(

        @Json(name = "departure")
        val departure: Departure,

        @Json(name = "arrival")
        val arrival: Arrival,

        @Json(name = "tags")
        val tags: Tags
)

data class Trip(

        @Json(name = "id")
        val id: String, // T-84911-83524-201806080700-201806081100-756АА

        @Json(name = "number")
        val number: String, // 756А

        @Json(name = "direction")
        val direction: String, // Москва - Санкт-Петербург

        @Json(name = "validatingCarrierName")
        val validatingCarrierName: String, // РЖД

        @Json(name = "operatingCarrierName")
        val operatingCarrierName: String, // РЖД

        @Json(name = "placeFromId")
        val placeFromId: Int, // 84911

        @Json(name = "fromDescription")
        val fromDescription: String, // Ленинградский вокзал, Москва

        @Json(name = "placeToId")
        val placeToId: Int, // 83524

        @Json(name = "toCityName")
        val toCityName: String,

        @Json(name = "toDescription")
        val toDescription: String, // Московский вокзал, Санкт-Петербург

        @Json(name = "tripType")
        val tripType: String, // train

        @Json(name = "carTypeName")
        val carTypeName: String, // скоростной поезд

        @Json(name = "departure")
        val departure: String, // 2018-06-08T07:00:00+03:00

        @Json(name = "arrival")
        val arrival: String, // 2018-06-08T11:00:00+03:00

        @Json(name = "duration")
        val duration: String, // 04:00:00

        @Json(name = "boardingDuration")
        val boardingDuration: String, // 00:15:00

        @Json(name = "unboardingDuration")
        val unboardingDuration: String, // 00:05:00

        @Json(name = "distance")
        val distance: Int, // 642914

        @Json(name = "isReturnTrip")
        val isReturnTrip: Boolean, // false

        @Json(name = "isTransferTrip")
        val isTransferTrip: Boolean, // false

        @Json(name = "carrierName")
        val carrierName: String, // РЖД

        @Json(name = "scheduleBegin")
        val scheduleBegin: String, // 0001-01-01T00:00:00

        @Json(name = "scheduleEnd")
        val scheduleEnd: String, // 0001-01-01T00:00:00

        @Json(name = "title")
        val title: String // Сапсан
)

data class Filter(

        @Json(name = "tags")
        val tags: Tags,

        @Json(name = "tripTypes")
        val tripTypes: List<TripType>,

        @Json(name = "transfers")
        val transfers: Transfers,

        @Json(name = "departure")
        val departure: Departure,

        @Json(name = "arrival")
        val arrival: Arrival,

        @Json(name = "carriers")
        val carriers: List<Carrier>
)

data class TripType(

        @Json(name = "id")
        val id: String, // train

        @Json(name = "name")
        val name: String, // Автобус

        @Json(name = "isTransfer")
        val isTransfer: Boolean, // false

        @Json(name = "isActive")
        val isActive: Boolean // true
)

data class Carrier(

        @Json(name = "carriersFilter")
        val carriersFilter: List<CarriersFilter>,

        @Json(name = "tripType")
        val tripType: TripType
)

data class CarriersFilter(

        @Json(name = "value")
        val value: Int, // 11851

        @Json(name = "title")
        val title: String // ИП Унанян
)

data class DaysFilter(

        @Json(name = "dayParts")
        val dayParts: List<DayPart>,

        @Json(name = "date")
        val date: String // 2018-06-08T00:00:00
)


data class DayPart(

        @Json(name = "value")
        val value: Int, // 2

        @Json(name = "name")
        val name: String // Утро
)

data class Transfers(

        @Json(name = "transfers")
        val transfers: List<Transfer>
)

data class Transfer(

        @Json(name = "title")
        val title: String, // Без пересадок
        @Json(name = "value")
        val value: Int // 1
)

data class Tags(

        @Json(name = "tags")
        val tags: List<Any> = emptyList()
)

data class Arrival(

        @Json(name = "daysFilter")
        val daysFilter: List<DaysFilter> = emptyList()
)

data class Service(

        @Json(name = "tripIds")
        val tripIds: List<String>,

        @Json(name = "id")
        val id: String, // RZDS-T-84911-83524-201806080700-201806081100-756АА

        @Json(name = "price")
        val price: Double, // 7443.8

        @Json(name = "serviceData")
        val serviceData: ServiceData
)

data class ServiceData(

        @Json(name = "title")
        val title: String, // типы вагонов

        @Json(name = "serviceClasses")
        val serviceClasses: List<ServiceClasses>
)

data class ServiceClasses(

        @Json(name = "id")
        val id: String, // Seating

        @Json(name = "title")
        val title: String, // сидячий

        @Json(name = "seats")
        val seats: Int, // 2072

        @Json(name = "price")
        val price: Double, // 7443.8

        @Json(name = "bookingUrl")
        val bookingUrl: String // https://poezd.ru/?r=places/partner&pid=itforces&stationCodeFrom=2006004&stationCodeTo=2004001&date=08.06.2018&trainNumber=756А&cartype=с
)

data class Route(

        @Json(name = "id")
        val id: String, // 313b1d930044429bb4781ea589adac84

        @Json(name = "tripIds")
        val tripIds: List<String>,

        @Json(name = "minPrice")
        val minPrice: Double, // 7443.8

        @Json(name = "services")
        val services: List<String>
)
