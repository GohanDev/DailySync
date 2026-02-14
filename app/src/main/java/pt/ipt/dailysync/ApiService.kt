package pt.ipt.dailysync

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("compromissos")
    fun getCompromissos(): Call<List<Compromisso>>

    @POST("compromissos")
    fun adicionarCompromisso(@Body compromisso: CompromissoRequest): Call<Compromisso>

    @PUT("compromissos/{id}")
    fun editarCompromisso(
        @Path("id") id: Int,
        @Body compromisso: CompromissoRequest
    ): Call<Compromisso>

    @DELETE("compromissos/{id}")
    fun eliminarCompromisso(@Path("id") id: Int): Call<Void>
}
