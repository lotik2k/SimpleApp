package com.movista.app.data.source.remote

import com.google.gson.JsonObject
import com.movista.app.data.entity.PopularResponse
import com.movista.app.data.entity.SearchResponse
import com.movista.app.data.entity.TripsRequestModel
import com.movista.app.data.entity.TripsResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiEndpoints {

    companion object {
        private const val REGISTRATION = "api/users/simpleregister"
        private const val GET_TOKEN = "connect/token"
        private const val SEARCH_ROUTES = "api/v1/routes/search"
        private const val SEARCH_BY_NAME = "api/v1/places/searchByName"
        private const val POPULAR = "api/v1/places/popularRecent"
        private const val SEARCH_BY_LOCATION = "api/v1/places/searchByLocation"

        private const val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IkEwRjQ3NTM2RjM5RUUzMDlBRDFFRkMzQjZFREFBRDFEQkZGM0M3MUEiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJvUFIxTnZPZTR3bXRIdnc3YnRxdEhiX3p4eG8ifQ.eyJuYmYiOjE1MzA2MjA5NDUsImV4cCI6MTU5MzY5Mjk0NSwiaXNzIjoiaHR0cHM6Ly9hdXRoLm1vdmlzdGEucnUiLCJhdWQiOlsiaHR0cHM6Ly9hdXRoLm1vdmlzdGEucnUvcmVzb3VyY2VzIiwiVUlXZWJBcGkuU2VhcmNoIiwiVUlXZWJBcGkuVGFncyJdLCJjbGllbnRfaWQiOiJNZXRhU2VhcmNoLkdVSSIsInN1YiI6IjI0MzEiLCJhdXRoX3RpbWUiOjE1MzA2MjA5NDUsImlkcCI6ImxvY2FsIiwic2NvcGUiOlsiVUlXZWJBcGkuU2VhcmNoLkZ1bGxBY2Nlc3MiLCJVSVdlYkFwaS5UYWdzLkZ1bGxBY2Nlc3MiXSwiYW1yIjpbInB3ZCJdfQ.lhVRxnw7O_n4utlS_mppg0nl9QZAOcnzrQi00ePEx3z2VFB-5Wrx7d3b4jbrY1WNAUMhMgb3TAWglFgm0ZOpeP8RCE-t3WJ7378w5MDTqLJgGcy21ODkt3aEmYX5EipJXeZdq8gAh6XEyt5HK23AdVYUtlM2wjrQpiMRPFYrZppJhoCHnAm6i6aiXCn9qNAw_KfuVzvHUNZXR3o7vxULD44KYrtZ3-LwES4RxYjLxGxftZvSTokYpHfnHG85kdiAfnAgss3vv0T_dNQnI-Q6IQMuuYis8GYIbtVWce0vuu1lDd4noWC1v9fvSYXEduPsDGVEChh-lVn8ti3nVncjJw"
        private const val AUTH_HEADER = "Authorization: $token"
    }

    @POST(REGISTRATION)
    fun registerUser(
            @Body
            params: JsonObject
    ): Single<ResponseBody>

    @FormUrlEncoded
    @POST(GET_TOKEN)
    fun getToken(
            @Field("grant_type")
            value: String = "password",
            @Field("username")
            name: String,
            @Field("password")
            password: String,
            @Field("client_id")
            clientId: String = "MetaSearch.GUI",
            @Field("client_secret")
            secret: String = "secret"
    ): Single<ResponseBody>


    @POST(SEARCH_ROUTES)
    @Headers(AUTH_HEADER, "Content-Type: application/json")
    fun getRoutes(
            @Body
            params: TripsRequestModel
    ): Single<TripsResponse>

    @GET(SEARCH_BY_NAME)
    @Headers(AUTH_HEADER)
    fun searchByName(
            @Query("name")
            name: String,
            @Query("count")
            count: Int = 10
    ): Single<SearchResponse>


    @GET(POPULAR)
    @Headers(AUTH_HEADER)
    fun getPopular(
            @Query("fromTo")
            from: String = "from"
    ): Single<PopularResponse>

    @GET(SEARCH_BY_LOCATION)
    @Headers(AUTH_HEADER)
    fun searchByLocation(
            @Query("lat")
            latitude: Double,

            @Query("lon")
            longitude: Double
    ): Single<SearchResponse>
}

