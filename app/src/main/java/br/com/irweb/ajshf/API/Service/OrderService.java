package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.OrderClient;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
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
    private String userToken;
    private UserAuthAJSHF user;
    private Order order;
    private UserBusiness userBusiness;

    public OrderService(Context context) {
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        orderClient = retrofit.create(OrderClient.class);

        user = AJSHFApp.getInstance().getUser();
        order = AJSHFApp.getOrder();
        userBusiness = new UserBusiness(context);
    }

    public int createOrder() throws Exception {
        Call<ResponseBody> order = orderClient.postOrder(this.order, getUserToken());

        Response<ResponseBody> orderExecute = order.execute();

        if (orderExecute.code() == HttpURLConnection.HTTP_CREATED) {
            for (int i = 0; i < orderExecute.headers().size(); i++) {
                String location = orderExecute.headers().get("Location");

                assert location != null;

                String[] urlSplit = location.split("/");
                return Integer.parseInt(urlSplit[urlSplit.length - 1]);

            }
        } else {
            throw new Exception(orderExecute.errorBody().string());
        }
        return 0;
    }

    private String getUserToken() {
        if (userToken != null) {
            return userToken;
        }
        userToken = String.format("%s %s", user.tokenType, user.accessToken);
        return userToken;
    }

    public List<Order> getOrders() throws IOException {

        Response<ResponseBody> response;

        try {
            while (true) {
                Call<ResponseBody> client = orderClient.getOrders(getUserToken());

                response = client.execute();

                if (response.code() == HttpURLConnection.HTTP_OK) {
                    Type t = new TypeToken<List<Order>>() {
                    }.getType();
                    return new Gson().fromJson(response.body().string(), t);
                } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    if (userBusiness.RefreshToken()) {
                        continue;
                    }
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getOrders(int idOrder) throws IOException {
        Call<ResponseBody> client = orderClient.getOrder(idOrder, getUserToken());

        try {
            Response<ResponseBody> response = client.execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
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
