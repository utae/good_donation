package kr.co.t_woori.good_donation.setting;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.SettingActivityBinding;
import kr.co.t_woori.good_donation.login.LoginActivity;
import kr.co.t_woori.good_donation.notification.NearPlaceNotifyService;
import kr.co.t_woori.good_donation.utilities.UserInfo;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-07.
 */

public class SettingActivity extends AppCompatActivity{

    private SettingActivityBinding binding;
    private SharedPreferences settingPref;

    private static final int PERMISSIONS_REQUEST_LOCATION = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.setting_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initNearPlaceNotifySwitch();

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void initNearPlaceNotifySwitch(){
        settingPref = getSharedPreferences("setting", MODE_PRIVATE);
        if(settingPref != null){
            if(settingPref.getBoolean("nearPlaceNotify", false)){
                if(Utilities.isServiceRunningCheck(this, ".notification.NearPlaceNotifyService")){
                    binding.nearPlaceNotifySwitch.setChecked(true);
                }else{
                    settingPref.edit().putBoolean("nearPlaceNotify", false).apply();
                }
            }
        }
        binding.nearPlaceNotifySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(checkLocationPermission()){
                        startNearPlaceNotifyService();
                    }else{
                        buttonView.setChecked(false);
                    }
                }else{
                    stopNearPlaceNotifyService();
                }
            }
        });
    }

    private void startNearPlaceNotifyService(){
        Utilities.showToast(SettingActivity.this, "근처 기부매장 알림이 켜졌습니다.");
        settingPref.edit().putBoolean("nearPlaceNotify", true).apply();
        Intent intent = new Intent(SettingActivity.this, NearPlaceNotifyService.class);
        startService(intent);
    }

    private void stopNearPlaceNotifyService(){
        Utilities.showToast(SettingActivity.this, "근처 기부매장 알림이 꺼졌습니다.");
        settingPref.edit().putBoolean("nearPlaceNotify", false).apply();
        if(Utilities.isServiceRunningCheck(SettingActivity.this, ".notification.NearPlaceNotifyService")){
            Intent intent = new Intent(SettingActivity.this, NearPlaceNotifyService.class);
            stopService(intent);
        }
    }

    private boolean checkLocationPermission(){
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
                !isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST_LOCATION);
            }
        }else{
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_LOCATION :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    binding.nearPlaceNotifySwitch.setChecked(true);
                }else{
                    Utilities.showToast(this, "권한이 없습니다.");
                }
                break;
        }
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void logout(){
        if(Utilities.isServiceRunningCheck(this, ".notification.NearPlaceNotifyService")){
            stopNearPlaceNotifyService();
        }
        SharedPreferences prefs = getSharedPreferences("good_donation", MODE_PRIVATE);
        if(prefs != null){
            prefs.edit().clear().apply();
        }
        UserInfo.clearUserInfo();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
