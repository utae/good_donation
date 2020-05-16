package kr.co.t_woori.good_donation.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.main.MainActivity;
import kr.co.t_woori.good_donation.notification.NearPlaceNotifyService;
import kr.co.t_woori.good_donation.utilities.UserInfo;
import kr.co.t_woori.good_donation.utilities.Utilities;
import retrofit2.Response;

/**
 * Created by rladn on 2017-08-07.
 */

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.clearGlideCache(this);
        checkAutoLogin();
    }

    private void checkAutoLogin(){
        prefs = getSharedPreferences("good_donation", MODE_PRIVATE);

        if(prefs != null && prefs.getString("token", null) != null){
            autoLogin();
        }else{
            goToLogin();
        }
    }

    private void autoLogin(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(LoginAPIService.class).autoLogin(prefs.getString("token", null))
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                UserInfo.setToken(prefs.getString("token", null));
                SharedPreferences setting = getSharedPreferences("setting", MODE_PRIVATE);
                if(setting != null && setting.getBoolean("nearPlaceNotify", false) && !Utilities.isServiceRunningCheck(SplashActivity.this, ".notification.NearPlaceNotifyService")){
                    Intent intent = new Intent(SplashActivity.this, NearPlaceNotifyService.class);
                    startService(intent);
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            protected void onServerError(Response<HashMap<String, Object>> response) {
                super.onServerError(response);
                if(response != null && response.code() == 401){
                    goToLogin();
                }else{
                    finish();
                }
            }
        };
        serverCommunicator.execute();
    }

    private void goToLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
