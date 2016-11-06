package com.example.gerardo.miestacionamiento.controller.rest;

import com.example.gerardo.miestacionamiento.model.ResponseAllEstacionamientos;
import com.example.gerardo.miestacionamiento.model.ResponseLogin;
import com.example.gerardo.miestacionamiento.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Gerardo on 29/09/2016.
 */
public interface IApiService {

//    @GET(ApiConstants.URL_LOGIN)
//    Call<ResponseLogin> login(@Query(ApiConstants.PARAM_RUT) String rut,
//                                @Query(ApiConstants.PARAM_CLAVE) String clave);

    @POST(ApiConstants.URL_LOGIN)
    Call<ResponseLogin> login(@Body Usuario usuario);

    @POST(ApiConstants.URL_GET_ESTACIONAMIENTOS)
    Call<List<ResponseAllEstacionamientos>> getEstacionamientos();


}
