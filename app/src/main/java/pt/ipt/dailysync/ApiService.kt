package pt.ipt.dailysync

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("compromissos")
    fun getCompromissos(): Call<List<Compromisso>>

    @POST("compromissos")
    fun addCompromisso(@Body body: CompromissoRequest): Call<Compromisso>

    @PUT("compromissos/{id}")
    fun updateCompromisso(@Path("id") id: Int, @Body body: CompromissoRequest): Call<Compromisso>

    @DELETE("compromissos/{id}")
    fun deleteCompromisso(@Path("id") id: Int): Call<Void>
}
