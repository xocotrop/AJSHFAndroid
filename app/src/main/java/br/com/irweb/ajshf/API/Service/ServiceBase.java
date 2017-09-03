package br.com.irweb.ajshf.API.Service;

import android.content.Context;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;

/**
 * Created by Igor on 03/09/2017.
 */

public class ServiceBase {

    private Context context;
    protected UserAuthAJSHF user;
    private UserBusiness userBusiness;
    protected String userToken;

    public ServiceBase(Context context) {
        this.context = context;
        user = AJSHFApp.getInstance().getUser();
        userBusiness = new UserBusiness(context);
    }

    protected void reloadUser(){
        userToken = null;
        user = AJSHFApp.getInstance().getUser();
    }

    protected String getUserToken() {
        if (userToken != null && !userToken.isEmpty()) {
            return userToken;
        }
        userToken = String.format("%s %s", user.tokenType, user.accessToken);
        return userToken;
    }

    protected boolean RefreshToken() {
        return userBusiness.RefreshToken();
    }


}
