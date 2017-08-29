package br.com.irweb.ajshf.API;


import br.com.irweb.ajshf.Entities.Address;
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

public interface FreightClient {

    @Headers("Content-type: application/json")
    @GET("/api/Freight/{idAddress}")
    Call<ResponseBody> getFreight(@Path("idAddress") int idAddress, @Header("Authorization") String authorization);

}
