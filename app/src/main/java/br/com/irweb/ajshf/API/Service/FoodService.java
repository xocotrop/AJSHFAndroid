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
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Food;
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

public class FoodService extends ServiceBase {

    private Retrofit retrofit;
    private FoodClient foodClient;
    private Context context;

    public FoodService(Context context) {
        super(context);

        this.context = context;

        retrofit = AJSHFApp.getInstance().getRetrofit();
        foodClient = retrofit.create(FoodClient.class);
    }

    public void addItemOrder(Food food) throws Exception {
        if (food == null) {
            throw new Exception("Food is null");
        }

        Order order = AJSHFApp.getOrder();
        if (order.Items == null) {
            order.Items = new ArrayList<ItemOrder>();
        }

        ItemOrder item = createItemOrder(food);

        order.Items.add(item);
    }

    public void removeItemOrder(int id) {
        Order order = AJSHFApp.getOrder();
        if (order.Items != null) {
            for (ItemOrder item :
                    order.Items) {
                if (item.MenuId == id) {
                    order.Items.remove(item);
                    break;
                }
            }
        }
    }

    @NonNull
    private ItemOrder createItemOrder(Food food) {
        ItemOrder item = new ItemOrder();
        item.CustomMenu = food.Custom;
        item.MenuId = food.Id;
        item.Name = food.Title;
        item.Quantity = 1;
        item.Value = food.Value;
        if (food.Custom) {
            //custom items
        }
        return item;
    }

    public Food GetFood(int foodId) throws IOException, ApiException {
        Response<ResponseBody> response;
        while (true) {
            Call<ResponseBody> foodClient = this.foodClient.GetFood(foodId, getUserToken());

            response = foodClient.execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                String content = response.body().string();

                Type t = new TypeToken<Food>() {
                }.getType();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create();

                return gson.fromJson(content, t);
            } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                if (RefreshToken()) {
                    continue;
                }
                break;
            }
            break;
        }
        ApiException ex = new ApiException();
        ex.setStatusCode(response.code());
        ex.setMessage(response.errorBody().string());

        throw ex;

    }

    public List<Food> GetFood() throws IOException, ApiException {

        Response<ResponseBody> response;
        String message = "";
        boolean errorRefreshToken = false;
        while (true) {
            Call<ResponseBody> foodClient = this.foodClient.GetFood(getUserToken());

            response = foodClient.execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                String content = response.body().string();

                Type t = new TypeToken<List<Food>>() {
                }.getType();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                        .create();

                return gson.fromJson(content, t);
            } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND) {
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
        ex.setMessage(errorRefreshToken ? message : response.errorBody().string());

        throw ex;
    }
}
