package br.com.irweb.ajshf.API;

import br.com.irweb.ajshf.Entities.Client;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Igor on 27/05/2017.
 */

public interface AccountClient {
    @FormUrlEncoded
    @POST("/token")
    Call<ResponseBody> GenerateTokenFacebook(@Field("token") String token, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/token")
    Call<ResponseBody> GenerateToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/token")
    Call<ResponseBody> RefreshToken(@Field("refresh_token") String refreshToken, @Field("grant_type") String grantType);

    @Headers("Content-type: application/json")
    @GET("/api/client/me")
    Call<ResponseBody> getMe(@Header("Authorization") String auth);

    @Headers("Content-type: application/json")
    @POST("/api/client")
    Call<ResponseBody> createUser(@Body Client client);
}
