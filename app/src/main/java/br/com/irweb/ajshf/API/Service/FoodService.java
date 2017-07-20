package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.FoodClient;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
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
    private UserAuthAJSHF user;
    private String userToken;

    public FoodService(Context context) {
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        foodClient = retrofit.create(FoodClient.class);
        user = AJSHFApp.getInstance().getUser();
    }

    private String getUserToken(){
        if(userToken != null){
            return userToken;
        }
        userToken = String.format("%s %s", user.tokenType, user.accessToken);
        return userToken;
    }

    public Food GetFood(int foodId) throws IOException, ApiException {

        Call<ResponseBody> foodClient = this.foodClient.GetFood(foodId, getUserToken());

        Response<ResponseBody> response = foodClient.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {
            String content = response.body().string();

            Type t = new TypeToken<Food>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            return gson.fromJson(content, t);
        }

        ApiException ex = new ApiException();
        ex.setStatusCode(response.code());
        ex.setMessage(response.errorBody().string());

        throw ex;

    }

    public List<Food> GetFood() throws IOException, ApiException {
        Call<ResponseBody> foodClient = this.foodClient.GetFood(getUserToken());

        Response<ResponseBody> response = foodClient.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {
            String content = response.body().string();

            Type t = new TypeToken<List<Food>>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            return gson.fromJson(content, t);
        }
        else if(response.code() == HttpURLConnection.HTTP_NOT_FOUND){
            return null;
        }

        ApiException ex = new ApiException();
        ex.setStatusCode(response.code());
        ex.setMessage(response.errorBody().string());

        throw ex;
    }
}
