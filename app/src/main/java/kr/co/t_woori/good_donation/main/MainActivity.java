package kr.co.t_woori.good_donation.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.MainActivityBinding;
import kr.co.t_woori.good_donation.donation.ChooseCharityDialog;
import kr.co.t_woori.good_donation.donation.DonationAPIService;
import kr.co.t_woori.good_donation.donation.MyDonationActivity;
import kr.co.t_woori.good_donation.donation.MyDonationLogDialog;
import kr.co.t_woori.good_donation.rank.DonationRankActivity;
import kr.co.t_woori.good_donation.inquiry.InquiryActivity;
import kr.co.t_woori.good_donation.setting.MyProfileActivity;
import kr.co.t_woori.good_donation.setting.SettingActivity;
import kr.co.t_woori.good_donation.utilities.BackPressCloseSystem;
import kr.co.t_woori.good_donation.utilities.BottomNavigationController;
import kr.co.t_woori.good_donation.utilities.BottomNavigationListenerContainer;
import kr.co.t_woori.good_donation.utilities.Utilities;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 200;
    private MainActivityBinding binding;
    private BackPressCloseSystem backPressCloseSystem;
    private BottomNavigationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        init();
    }

    private void init(){
        initDrawer();
        initBottomNavigation();
        backPressCloseSystem = new BackPressCloseSystem(this);
    }

    private void initDrawer(){
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        binding.navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener());
    }

    private void initBottomNavigation(){
        BottomNavigationListenerContainer listenerContainer = new BottomNavigationListenerContainer();
        listenerContainer.setOnHomeBannerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.bottomNavigation.setSelectedItemId(R.id.navigation_charity);
            }
        });
        controller = new BottomNavigationController(getSupportFragmentManager(), R.id.fragment_container, listenerContainer);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(new OnBottomNavigationItemSelectedListener(controller));
        if(getIntent().getStringExtra("notification") != null && "nearPlace".equals(getIntent().getStringExtra("notification"))){
            binding.bottomNavigation.setSelectedItemId(R.id.navigation_place);
        }else{
            controller.selectMenu(BottomNavigationController.Menu.home);
        }
    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            backPressCloseSystem.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        binding.drawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSIONS_REQUEST_LOCATION :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    controller.selectMenu(BottomNavigationController.Menu.map);
                }else{
                    Utilities.showToast(this, "권한이 없습니다.");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result.getContents() != null){
                String token = getDonationToken(result.getContents());
                if(token != null && !"".equals(token)){
                    showChoiceCharity(token);
                }else{
                    Utilities.showToast(this, "기부 태그가 아닙니다.");
                }
            }
        }else if(data != null && data.getStringExtra("tag") != null){
            if(resultCode == RESULT_OK){
                String token = getDonationToken(data.getStringExtra("tag"));
                if(token != null && !"".equals(token)){
                    showChoiceCharity(token);
                }else{
                    Utilities.showToast(this, "기부 태그가 아닙니다.");
                }
            }
        }
    }

    private void showChoiceCharity(String token){
        ChooseCharityDialog chooseCharityDialog = new ChooseCharityDialog();
        chooseCharityDialog.setOnItemClickListener(new OnCharityItemClickListener(chooseCharityDialog, token));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(chooseCharityDialog, "chooseCharityDialog").commitAllowingStateLoss();
    }

    private void showMyDonationLog(String appreciation){
        MyDonationLogDialog myDonationLogDialog = MyDonationLogDialog.create(appreciation);
        myDonationLogDialog.show(getSupportFragmentManager(), "myDonationLogDialog");
    }

    private String getDonationToken(String tag){
        try {
            JSONObject jsonObject = new JSONObject(tag);
            if("donation".equals(jsonObject.get("provider"))){
                return (String)jsonObject.get("token");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class OnNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()){
                case R.id.drawer_my_donation :
                    intent = new Intent(MainActivity.this, MyDonationActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.drawer_my_profile :
                    intent = new Intent(MainActivity.this, MyProfileActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.drawer_donation_rank :
                    intent = new Intent(MainActivity.this, DonationRankActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.drawer_inquiry :
                    intent = new Intent(MainActivity.this, InquiryActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.drawer_setting :
                    intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    }

    private class OnBottomNavigationItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        private BottomNavigationController bottomNavigationController;

        public OnBottomNavigationItemSelectedListener(BottomNavigationController bottomNavigationController) {
            this.bottomNavigationController = bottomNavigationController;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Utilities.logD("Test", Boolean.toString(binding.bottomNavigation.getSelectedItemId() == item.getItemId()));
            if(binding.bottomNavigation.getSelectedItemId() == item.getItemId()){
                bottomNavigationController.refreshCurFragment();
                return true;
            }else{
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        bottomNavigationController.selectMenu(BottomNavigationController.Menu.home);
                        return true;
                    case R.id.navigation_scan:
                        bottomNavigationController.selectMenu(BottomNavigationController.Menu.scan);
                        return true;
                    case R.id.navigation_place:
                        requestLocationPermission();
                        return true;
                    case R.id.navigation_charity:
                        bottomNavigationController.selectMenu(BottomNavigationController.Menu.charity);
                        return true;
                }
            }
            return false;
        }

        private void requestLocationPermission(){
            if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    !isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)){
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                        ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_REQUEST_LOCATION);
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSIONS_REQUEST_LOCATION);
                }
            }else{
                bottomNavigationController.selectMenu(BottomNavigationController.Menu.map);
            }
        }
    }

    private class OnCharityItemClickListener implements AdapterView.OnItemClickListener{

        private DialogFragment dialog;
        private String token;

        public OnCharityItemClickListener(DialogFragment dialog, String token) {
            this.dialog = dialog;
            this.token = token;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent.getItemAtPosition(position) instanceof Charity){
                Charity charity = (Charity)parent.getItemAtPosition(position);
                donate(charity.getIdNum(), charity.getAppreciationPhrase());
            }
        }

        private void donate(String charityId, final String appreciation){
            ServerCommunicator serverCommunicator = new ServerCommunicator(
                    MainActivity.this, APICreator.create(DonationAPIService.class).donate(token, charityId)
            ) {
                @Override
                protected void onSuccess(HashMap<String, Object> results) {
                    Utilities.showToast(MainActivity.this, "기부성공");
                    dialog.dismiss();
                    showMyDonationLog(appreciation);
                }

                @Override
                protected void onServerError(Response<HashMap<String, Object>> response) {
                    super.onServerError(response);
                    dialog.dismiss();
                }
            };

            serverCommunicator.execute();
        }
    }
}
