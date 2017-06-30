package br.com.irweb.ajshf.Business;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import br.com.irweb.ajshf.API.Entities.GenerateTokenResponse;
import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.UserService;
import br.com.irweb.ajshf.Application.AJSHFApp;
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

            User me = GetMe(userAuth.tokenType, userAuth.accessToken);

            UserAuthAJSHF authAJSHF = createUserAuth(userAuth, me);

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
    private UserAuthAJSHF createUserAuth(GenerateTokenResponse userAuth, User me) {
        UserAuthAJSHF authAJSHF = new UserAuthAJSHF();
        authAJSHF.token = userAuth.accessToken;
        authAJSHF.tokenType = userAuth.tokenType;
        authAJSHF.Email = me.Email;
        authAJSHF.Id = me.Id;
        authAJSHF.LastName = me.LastName;
        authAJSHF.Name = me.Name;
        authAJSHF.ReceiveUpdates = me.ReceiveUpdates;
        return authAJSHF;
    }

    public User GetMe(String tokeType, String token) throws ApiException {
        return service.getMe(tokeType, token);

    }

}
