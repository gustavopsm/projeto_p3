package com.example.p3_project.network

import com.example.p3_project.data.entities.Torneio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TorneioApiService {

    @POST("torneios")
    suspend fun criarTorneio(@Body torneio: Torneio): Response<Torneio>

    @GET("torneios")
    suspend fun listarTorneios(): Response<List<Torneio>>
}
