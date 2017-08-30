package br.com.irweb.ajshf.API.Service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.FoodClient;
import br.com.irweb.ajshf.API.FreightClient;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.Entities.Freight;
import br.com.irweb.ajshf.Entities.ItemOrder;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Igor on 22/05/2017.
 */

public class FreightService {

    private Retrofit retrofit;
    private FreightClient freightClient;
    private Context context;
    private UserAuthAJSHF user;
    private String userToken;
    private UserBusiness userBusiness;

    public FreightService(Context context) {
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        freightClient = retrofit.create(FreightClient.class);
        user = AJSHFApp.getInstance().getUser();
        userBusiness = new UserBusiness(context);
    }

    private String getUserToken() {
        if (userToken != null) {
            return userToken;
        }
        userToken = String.format("%s %s", user.tokenType, user.accessToken);
        return userToken;
    }

    public List<Freight> getFreights(int idAddress) throws IOException, ApiException {
        Response<ResponseBody> response;

        Call<ResponseBody> freight = freightClient.getFreight(idAddress, getUserToken());

        response = freight.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {
            String content = response.body().string();

            Type t = new TypeToken<List<Freight>>() {
            }.getType();

            return new Gson().fromJson(content, t);
        }
        else{
            //Mock provisorio
            List<Freight> fake = new ArrayList<>();
            for(int i = 0; i < 3; i++){
                Freight f = new Freight();
                f.IdAddress = 1;
                f.IdCity = 1;
                f.Period = i ;
                f.Price = 10 + i;
                fake.add(f);
            }
            return fake;
        }

//        ApiException ex = new ApiException();
        //ex.setStatusCode(response.code());
        //ex.setMessage(response.errorBody().string());

  //      throw ex;
    }

}
