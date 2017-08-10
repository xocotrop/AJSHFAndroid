package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.List;

import br.com.irweb.ajshf.API.AccountClient;
import br.com.irweb.ajshf.API.AddressClient;
import br.com.irweb.ajshf.API.Entities.GenerateTokenResponse;
import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.Client;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Igor on 27/05/2017.
 */

public class UserService {

    private Retrofit retrofit;
    private AccountClient accountClient;
    private Context context;
    private AddressClient addressClient;

    public UserService(Context context) {
        this.context = context;
        retrofit = AJSHFApp.getInstance().getRetrofit();
        accountClient = retrofit.create(AccountClient.class);
        addressClient = retrofit.create(AddressClient.class);

    }



    public List<Address> getAddress() throws Exception {

        if (AJSHFApp.getInstance().getUser() != null) {
            String auth = String.format("%s %s", AJSHFApp.getInstance().getUser().tokenType, AJSHFApp.getInstance().getUser().accessToken);
            Call<ResponseBody> address = addressClient.getAddress(auth);
            Response<ResponseBody> response = address.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){

                Type t = new TypeToken<List<Address>>() {
                }.getType();

                return new Gson().fromJson(response.body().string(), t);

            } else {
                return  null;
            }
        }
        if (AJSHFApp.getInstance().getUser() == null) {
            throw new Exception("Not logged");
        }

        return null;
    }

    public String createUser(Client client) throws Exception {
        Call<ResponseBody> create = accountClient.createUser(client);

        Response<ResponseBody> response;

        try {
            response = create.execute();
            if (response.code() == HttpURLConnection.HTTP_CREATED) {
                String str = response.body().string();
                JSONObject jsonObject = new JSONObject(str);
                String id = jsonObject.getString("id");
                return id;
            } else {
                String str = response.errorBody().string();
                throw new Exception(str);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Client getMe(String tokenType, String token) {

        String auth = String.format("%s %s", tokenType, token);

        Call<ResponseBody> account = accountClient.getMe(auth);

        try {
            Response<ResponseBody> response = account.execute();

            if (response.code() == HttpURLConnection.HTTP_OK) {
                Type t = new TypeToken<Client>() {
                }.getType();
                return new Gson().fromJson(response.body().string(), t);
            } else {

            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;

    }

    public GenerateTokenResponse Auth(String login, String pass) throws IOException, ApiException {

        Call<ResponseBody> generateToken = accountClient.GenerateToken(login, pass, "password");

        Response<ResponseBody> response = generateToken.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {

            Type t = new TypeToken<GenerateTokenResponse>() {
            }.getType();

            return new Gson().fromJson(response.body().string(), t);

        }
        ApiException exception = new ApiException();
        exception.setMessage(response.errorBody().string());
        exception.setStatusCode(response.code());
        throw exception;

    }

    public GenerateTokenResponse RefreshToken() throws IOException, ApiException {

        Call<ResponseBody> refreshToken = accountClient.RefreshToken("refresh_tokenhsdgfhjgsdfjgf", "refresh_token");

        Response<ResponseBody> response = refreshToken.execute();

        if (response.code() == HttpURLConnection.HTTP_OK) {

            Type t = new TypeToken<GenerateTokenResponse>() {
            }.getType();

            return new Gson().fromJson(response.body().string(), t);

        }
        ApiException exception = new ApiException();
        exception.setMessage(response.errorBody().string());
        exception.setStatusCode(response.code());
        throw exception;

    }
}
