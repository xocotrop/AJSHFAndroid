package br.com.irweb.ajshf.API.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Igor on 27/05/2017.
 */

public class GenerateTokenResponse {
    @SerializedName("access_token")
    public String accessToken;
//    @SerializedName("refresh_token")
//    public String refreshToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public long expires;
    public int Id;
    public String Name;
    public String LastName;
    public String Email;
    public String ReceiveUpdates;
    @SerializedName(".issued")
    public String Issued;
    @SerializedName(".expires")
    public String Expires;

}