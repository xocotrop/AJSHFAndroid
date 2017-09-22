package br.com.irweb.ajshf.Business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import br.com.irweb.ajshf.API.Entities.GenerateTokenResponse;
import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.UserService;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.AddressUserAJSHF;
import br.com.irweb.ajshf.Entities.Client;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;

/**
 * Created by Igor on 27/05/2017.
 */

public class UserBusiness {

    private UserService service;
    private Context context;


    public UserBusiness(Context context) {
        this.context = context;
        service = new UserService(context);
    }

    public GenerateTokenResponse Auth(String login, String password) throws ApiException {

        try {
            GenerateTokenResponse userAuth = service.Auth(login, password);
            return userAuth;
        } catch (ApiException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void Register(Client client) throws Exception {

        try {

            String id = service.createUser(client);

        } catch (Exception e) {
            throw e;
        }

    }

    public boolean Authentication(String login, String password) throws ApiException {

        try {
            GenerateTokenResponse userAuth = service.Auth(login, password);


            UserAuthAJSHF authAJSHF = createUserAuth(userAuth);

            String serialize = new Gson().toJson(authAJSHF);

            AJSHFApp.getInstance().setUserToken(authAJSHF);
            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERTOKEN, serialize);

            List<Address> addresses = service.getAddress();

            AddressUserAJSHF addressUserAJSHF = createAddress(addresses);

            String addressSerialize = new Gson().toJson(addressUserAJSHF);

            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERADDRESSTOKEN, addressSerialize);
            AJSHFApp.getInstance().setADdressUserToken(addressUserAJSHF);

            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean AuthenticationFB(String token) throws ApiException {

        try {
            GenerateTokenResponse userAuth = service.AuthFacebook(token);


            UserAuthAJSHF authAJSHF = createUserAuth(userAuth);

            String serialize = new Gson().toJson(authAJSHF);

            AJSHFApp.getInstance().setUserToken(authAJSHF);
            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERTOKEN, serialize);

            List<Address> addresses = service.getAddress();

            AddressUserAJSHF addressUserAJSHF = createAddress(addresses);

            String addressSerialize = new Gson().toJson(addressUserAJSHF);

            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERADDRESSTOKEN, addressSerialize);
            AJSHFApp.getInstance().setADdressUserToken(addressUserAJSHF);

            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean RefreshToken() {
        //todo arrumar este método
        try {
            GenerateTokenResponse userAuth = service.RefreshToken();


            UserAuthAJSHF authAJSHF = createUserAuth(userAuth);

            String serialize = new Gson().toJson(authAJSHF);

            AJSHFApp.getInstance().setUserToken(authAJSHF);
            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERTOKEN, serialize);

            List<Address> addresses = service.getAddress();

            AddressUserAJSHF addressUserAJSHF = createAddress(addresses);

            String addressSerialize = new Gson().toJson(addressUserAJSHF);

            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERADDRESSTOKEN, addressSerialize);
            AJSHFApp.getInstance().setADdressUserToken(addressUserAJSHF);

            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @NonNull
    private AddressUserAJSHF createAddress(List<Address> addresses) {
        AddressUserAJSHF addressUserAJSHF = new AddressUserAJSHF();
        addressUserAJSHF.addresses = addresses;
        return addressUserAJSHF;
    }

    @NonNull
    private UserAuthAJSHF createUserAuth(GenerateTokenResponse userAuth) {
        UserAuthAJSHF authAJSHF = new UserAuthAJSHF();
        authAJSHF.accessToken = userAuth.accessToken;
        authAJSHF.tokenType = userAuth.tokenType;
        authAJSHF.Email = userAuth.Email;
        authAJSHF.Id = userAuth.Id;
        authAJSHF.LastName = userAuth.LastName;
        authAJSHF.Name = userAuth.Name;
        authAJSHF.ReceiveUpdates = userAuth.ReceiveUpdates;
        authAJSHF.refreshToken = userAuth.refreshToken;
        return authAJSHF;
    }

    public Client GetMe(String tokeType, String token) throws ApiException {
        return service.getMe(tokeType, token);

    }

}
