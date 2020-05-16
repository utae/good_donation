package kr.co.t_woori.good_donation.setting;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.MyProfileActivityBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-09-25.
 */

public class MyProfileActivity extends AppCompatActivity {

    private MyProfileActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_profile_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    saveMyProfile(binding.nameEdt.getText().toString().trim(), binding.birthEdt.getText().toString().trim());
                }
            }
        });

        getMyProfile();
    }

    private boolean isAllFormFilled(){
        if(!isFilled(binding.nameEdt)){
            Utilities.showToast(this, "이름을 입력하세요.");
        }else if(!isFilled(binding.birthEdt, 8)){
            Utilities.showToast(this, "생년월일이 8자리인지 확인해주세요.");
        }else if(!Utilities.isOnlyKorean(binding.nameEdt.getText().toString().trim())){
            Utilities.showToast(this, "이름은 한글만 입력가능합니다.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isFilled(EditText editText){
        return isFilled(editText, 1);
    }

    private boolean isFilled(EditText editText, int length){
        return editText.getText().toString().trim().length() >= length;
    }

    private void getMyProfile(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(SettingAPIService.class).getMyProfile()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                binding.nameEdt.setText((String)results.get("name"));
                binding.birthEdt.setText((String)results.get("birth"));
            }
        };

        serverCommunicator.execute();
    }

    private void saveMyProfile(String name, String birth){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(SettingAPIService.class).saveMyProfile(name, birth)
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                Utilities.showToast(MyProfileActivity.this, "저장 완료");
            }
        };

        serverCommunicator.execute();
    }
}
