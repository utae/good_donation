package kr.co.t_woori.good_donation.signup;

import android.content.Intent;
import android.content.SharedPreferences;
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
import kr.co.t_woori.good_donation.databinding.SignupThirdActivityBinding;
import kr.co.t_woori.good_donation.main.MainActivity;
import kr.co.t_woori.good_donation.utilities.ScrollView;
import kr.co.t_woori.good_donation.utilities.UserInfo;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupThirdActivity extends AppCompatActivity{

    private SignupThirdActivityBinding binding;

    private HashMap<String, String> signupInfo;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.signup_third_activity);

        initScrollView();

        signupInfo = (HashMap<String, String>) getIntent().getSerializableExtra("signupInfo");

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initScrollView(){
        binding.scrollView.setOnSizeChangeListener(new ScrollView.OnSizeChangeListener() {
            @Override
            public void onSizeChanged(boolean smaller) {
                if(smaller) {
                    View curFocusView = getCurrentFocus();
                    binding.scrollView.fullScroll(View.FOCUS_DOWN);
                    if (curFocusView != null) {
                        curFocusView.requestFocus();
                    }
                }
            }
        });
    }

    private boolean isAllDataValid(){
        if(!isFilled(binding.nicknameEdt)){
            Utilities.showToast(SignupThirdActivity.this, "닉네임을 입력해주세요.");
        }else if(!Utilities.isValidEmail(binding.emailEdt.getText().toString().trim())){
            Utilities.showToast(SignupThirdActivity.this, "이메일 주소를 확인해주세요.");
        }else if(!Utilities.isValidPassword(binding.pwEdt.getText().toString().trim())){
            Utilities.showToast(SignupThirdActivity.this, "비밀번호가 기준에 맞는 지 확인해주세요.");
        }else if(!isPwConfirmCorrect()){
            Utilities.showToast(SignupThirdActivity.this, "비밀번호 확인이 일치하지 않습니다.");
        }else{
            return true;
        }
        return false;
    }

    private boolean isPwConfirmCorrect(){
        return binding.pwEdt.getText().toString().equals(binding.pwCfmEdt.getText().toString());
    }

    private boolean isFilled(EditText editText){
        return isFilled(editText, 1);
    }

    private boolean isFilled(EditText editText, int length){
        return editText.getText().toString().trim().length() >= length;
    }

    private void signup(){
        if(isAllDataValid()){
            signupInfo.put("nickname", binding.nicknameEdt.getText().toString().trim());
            signupInfo.put("email", binding.emailEdt.getText().toString().trim());
            signupInfo.put("pw", binding.pwEdt.getText().toString().trim());

            ServerCommunicator serverCommunicator = new ServerCommunicator(
                    SignupThirdActivity.this, APICreator.create(SignupAPIService.class).signup(signupInfo)
            ) {

                @Override
                protected void onSuccess(HashMap<String, Object> results) {
                    String token = (String)results.get("token");
                    if(token != null){
                        Utilities.showToast(SignupThirdActivity.this, "회원가입 성공");
                        SharedPreferences prefs = getSharedPreferences("token", MODE_PRIVATE);
                        prefs.edit().putString("token", token).apply();
                        UserInfo.setToken(token);
                        Intent intent = new Intent(SignupThirdActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Utilities.showToast(SignupThirdActivity.this, "서버오류 : 토큰을 받아올 수 없습니다.");
                    }
                }
            };

            serverCommunicator.execute();
        }
    }
}
