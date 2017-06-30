package br.com.irweb.ajshf;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.test.mock.MockContext;
import android.util.Base64;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.FoodService;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Food;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    Context context;
    AJSHFApp app;
    FoodService s;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void generateHash() {
        try {
            context = new MockContext();
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Test
    public void a() {
        context = new MockContext();

        app = new AJSHFApp();
        app.onCreate();
        s = new FoodService(context);

        try {
            List<Food> a = s.GetFood();
            Assert.assertTrue(a != null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}