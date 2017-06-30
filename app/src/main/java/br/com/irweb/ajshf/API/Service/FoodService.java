package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.FoodClient;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Food;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Igor on 22/05/2017.
 */

public class FoodService {

    private Retrofit retrofit;
    private FoodClient foodClient;
    private Context context;

    public FoodService(Context context) {
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        foodClient = retrofit.create(FoodClient.class);
    }

    public Food GetFood(int foodId) throws IOException, ApiException {
        String auth = "Bearer ctR6d3rCQxbzPtlQ-8KYS-4xy-Caa1JHADpCJP-rJ9hPS17JUm-fBp_Lc_M3oZkOeAal_Ef8kwAVKEYQHPu42G4zb10x5n1mKqRiKGYOW_8kjynleEJJ06yhRhTSTp86j7M9yzn1yElpJoY-X3VJwXQ1Eij_Xtw0k_lYZfjBMa04H77JTMIMJMdp3QTwdzxCVBOyb0ETt4xPOEltcjvMc1UkJRpiFi-kaxM21BRkrlreDePy6lve-sO-SO3-_pZD";
        Call<ResponseBody> foodClient = this.foodClient.GetFood(foodId, auth);

        Response<ResponseBody> response = foodClient.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {
            String content = response.body().string();

            Type t = new TypeToken<Food>() {
            }.getType();

            return new Gson().fromJson(content, t);
        }

        ApiException ex = new ApiException();
        ex.setStatusCode(response.code());
        ex.setMessage(response.errorBody().string());

        throw ex;

    }

    public List<Food> GetFood() throws IOException, ApiException {
        String auth = "Bearer ctR6d3rCQxbzPtlQ-8KYS-4xy-Caa1JHADpCJP-rJ9hPS17JUm-fBp_Lc_M3oZkOeAal_Ef8kwAVKEYQHPu42G4zb10x5n1mKqRiKGYOW_8kjynleEJJ06yhRhTSTp86j7M9yzn1yElpJoY-X3VJwXQ1Eij_Xtw0k_lYZfjBMa04H77JTMIMJMdp3QTwdzxCVBOyb0ETt4xPOEltcjvMc1UkJRpiFi-kaxM21BRkrlreDePy6lve-sO-SO3-_pZD";
        Call<ResponseBody> foodClient = this.foodClient.GetFood(auth);

        Response<ResponseBody> response = foodClient.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {
            String content = response.body().string();

            Type t = new TypeToken<List<Food>>() {
            }.getType();

            return new Gson().fromJson(content, t);
        }

        ApiException ex = new ApiException();
        ex.setStatusCode(response.code());
        ex.setMessage(response.errorBody().string());

        throw ex;
    }
}
