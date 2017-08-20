package br.com.irweb.ajshf.API;

import br.com.irweb.ajshf.Entities.Order;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Igor on 23/05/2017.
 */

public interface OrderClient {

    @Headers("Content-type: application/json")
    @GET("/api/Order/{orderId}")
    Call<ResponseBody> getOrder(@Path("orderId") int orderId, @Header("Authorization") String authorization);

    @Headers("Content-type: application/json")
    @GET("/api/Order")
    Call<ResponseBody> getOrders(@Header("Authorization") String authorization);

    @Headers({"Content-type: application/json", "device: 1"})
    @POST("/api/Order")
    Call<ResponseBody> postOrder(@Body Order order, @Header("Authorization") String authorization);
}
