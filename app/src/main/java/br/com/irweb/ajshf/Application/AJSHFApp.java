package br.com.irweb.ajshf.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.TimeUnit;

import br.com.irweb.ajshf.BuildConfig;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Igor on 22/05/2017.
 */

public class AJSHFApp extends Application {
    public static final String PREF_USERTOKEN = "userToken";
    private static final String PREF_FILE_NAME = "prefApplication";
    private Retrofit retrofit;
    private static AJSHFApp instance;
    private static UserAuthAJSHF user;

    public static AJSHFApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initRetrofit();

        createInstance();

        LoadUserLogged();
    }

    public void setUserToken(UserAuthAJSHF userToken) {
        this.user = userToken;
    }

    public UserAuthAJSHF getUser(){
        return user;
    }

    private void LoadUserLogged() {
        String userToken = readFromPreference(this, PREF_USERTOKEN, null);
        if (userToken != null) {
            try {
                Gson gson = new Gson();
                user = gson.fromJson(userToken, UserAuthAJSHF.class);
            } catch (JsonSyntaxException ex) {
                Log.e("Error parseJson", ex.getMessage());
            }
        }
    }

    private void initRetrofit(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit
                .Builder()
                .baseUrl(BuildConfig.URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private void createInstance(){
        instance = this;
    }

    public static String readFromPreference(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreference(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(preferenceName, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    public static void saveToPreferences(Context context, String preferenceName, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName, value);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    private static void clearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            editor.commit();
        } else {
            editor.apply();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        retrofit = null;
        instance = null;
    }
}
