package br.com.irweb.ajshf.API;


import br.com.irweb.ajshf.Entities.Address;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Igor on 23/05/2017.
 */

public interface AddressClient {

    @Headers("Content-type: application/json")
    @GET("/api/Address/Data/{CEP}")
    Call<ResponseBody> getAddressInfo(@Path("CEP") String cep, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @POST("/api/Address")
    Call<ResponseBody> postAddress(@Body Address address, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @PUT("/api/Address/{id}")
    Call<ResponseBody> putAddress(@Path("id") int id, @Body Address address, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Address")
    Call<ResponseBody> getAddress(@Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Address/GetCities")
    Call<ResponseBody> getCities(@Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Address/city/{idCity}/GetAllNeighborhood")
    Call<ResponseBody> getAllNeighborhood(@Path("idCity") int idCity, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Address")
    Call<ResponseBody> getAllAddress(@Header("Authorization") String auth);
}
