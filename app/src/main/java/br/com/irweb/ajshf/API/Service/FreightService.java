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

public class FreightService extends ServiceBase {

    private Retrofit retrofit;
    private FreightClient freightClient;
    private Context context;

    public FreightService(Context context) {
        super(context);
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        freightClient = retrofit.create(FreightClient.class);
    }


    public List<Freight> getFreights(int idAddress, int quantity) throws IOException, ApiException {
        Response<ResponseBody> response;

        String message = "";
        boolean errorRefreshToken = false;

        while (true) {

            Call<ResponseBody> freight = freightClient.getFreight(idAddress, quantity, getUserToken());

            response = freight.execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                String content = response.body().string();

                Type t = new TypeToken<List<Freight>>() {
                }.getType();

                return new Gson().fromJson(content, t);
            }else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                return null;
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (RefreshToken()) {
                    reloadUser();
                    continue;
                }
                errorRefreshToken = true;
                message = "Você precisa fazer o login novamente";
                break;
            }
            break;
        }

        ApiException ex = new ApiException();
        ex.setStatusCode(errorRefreshToken ? 400 : response.code());
        ex.setMessage(errorRefreshToken ? message :response.errorBody().string());

        throw ex;
    }

}
