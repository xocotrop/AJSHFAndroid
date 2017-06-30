package br.com.irweb.ajshf.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Igor on 22/05/2017.
 */

public interface FoodClient {

    @Headers("Content-type: application/json")
    @GET("/api/Food/{foodId}")
    Call<ResponseBody> GetFood(@Path("foodId") int foodId, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Food")
    Call<ResponseBody> GetFood(@Header("Authorization") String authorization);

}
