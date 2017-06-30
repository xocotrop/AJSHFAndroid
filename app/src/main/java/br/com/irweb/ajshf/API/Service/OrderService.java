package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.irweb.ajshf.API.OrderClient;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Client;
import br.com.irweb.ajshf.Entities.Order;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Igor on 26/05/2017.
 */

public class OrderService {
    private Retrofit retrofit;
    private OrderClient orderClient;
    private Context context;

    public OrderService(OrderClient orderClient, Context context) {
        this.orderClient = orderClient;
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
    }


    public List<Order> getOrders() throws IOException {
        String auth = "Bearer ctR6d3rCQxbzPtlQ-8KYS-4xy-Caa1JHADpCJP-rJ9hPS17JUm-fBp_Lc_M3oZkOeAal_Ef8kwAVKEYQHPu42G4zb10x5n1mKqRiKGYOW_8kjynleEJJ06yhRhTSTp86j7M9yzn1yElpJoY-X3VJwXQ1Eij_Xtw0k_lYZfjBMa04H77JTMIMJMdp3QTwdzxCVBOyb0ETt4xPOEltcjvMc1UkJRpiFi-kaxM21BRkrlreDePy6lve-sO-SO3-_pZD";
        Call<ResponseBody> client = orderClient.getOrders(auth);

        try {
            Response<ResponseBody> response = client.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Type t = new TypeToken<List<Order>>() {
                }.getType();
                return new Gson().fromJson(response.body().string(), t);
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Order> getOrders(int idOrder) throws IOException {
        String auth = "Bearer ctR6d3rCQxbzPtlQ-8KYS-4xy-Caa1JHADpCJP-rJ9hPS17JUm-fBp_Lc_M3oZkOeAal_Ef8kwAVKEYQHPu42G4zb10x5n1mKqRiKGYOW_8kjynleEJJ06yhRhTSTp86j7M9yzn1yElpJoY-X3VJwXQ1Eij_Xtw0k_lYZfjBMa04H77JTMIMJMdp3QTwdzxCVBOyb0ETt4xPOEltcjvMc1UkJRpiFi-kaxM21BRkrlreDePy6lve-sO-SO3-_pZD";
        Call<ResponseBody> client = orderClient.getOrder(idOrder, auth);

        try {
            Response<ResponseBody> response = client.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Type t = new TypeToken<List<Order>>() {
                }.getType();
                return new Gson().fromJson(response.body().string(), t);
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
