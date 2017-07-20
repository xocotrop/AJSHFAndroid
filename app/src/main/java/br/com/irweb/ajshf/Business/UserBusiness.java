package br.com.irweb.ajshf.Business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.irweb.ajshf.API.Entities.GenerateTokenResponse;
import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.UserService;
import br.com.irweb.ajshf.Application.AJSHFApp;
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

    public boolean Authentication(String login, String password) throws ApiException {

        try {
            GenerateTokenResponse userAuth = service.Auth(login, password);


            UserAuthAJSHF authAJSHF = createUserAuth(userAuth);

            String serialize = new Gson().toJson(userAuth);

            AJSHFApp.getInstance().setUserToken(authAJSHF);
            AJSHFApp.saveToPreferences(context, AJSHFApp.PREF_USERTOKEN, serialize);

            return true;
        } catch (ApiException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
        return authAJSHF;
    }

    public Client GetMe(String tokeType, String token) throws ApiException {
        return service.getMe(tokeType, token);

    }

}
