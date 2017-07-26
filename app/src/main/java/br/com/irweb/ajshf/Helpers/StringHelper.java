package br.com.irweb.ajshf.Helpers;

/**
 * Created by Igor on 22/07/2017.
 */

public class StringHelper {
    public static boolean checkValidUrl(String url){

        if(url == null || url.isEmpty()){
            return false;
        }

        if(!url.startsWith("http://") && !url.startsWith("https://")){
            return false;
        }
        else if(!url.contains(".")){
            return false;
        }
        return true;
    }

    public static boolean checkValidUrlImage(String url){
        if(!checkValidUrl(url)){
            return false;
        }

        String urlLowerCase = url.toLowerCase();
        if(urlLowerCase.contains("jpg") || urlLowerCase.contains("jpeg") || urlLowerCase.contains("png") || urlLowerCase.contains("gif")){
            return true;
        }
        return false;
    }
}
