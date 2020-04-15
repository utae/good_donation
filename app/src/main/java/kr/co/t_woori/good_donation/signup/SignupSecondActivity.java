package kr.co.t_woori.good_donation.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.SignupSecondActivityBinding;
import kr.co.t_woori.good_donation.utilities.ScrollView;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupSecondActivity extends AppCompatActivity {

    private SignupSecondActivityBinding binding;

    private HashMap<String, String> signupInfo;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_second_activity);

        initScrollView();

        signupInfo = (HashMap<String, String>) getIntent().getSerializableExtra("signupInfo");

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllFormFilled()){
                    signupInfo.put("name", binding.nameEdt.getText().toString().trim());
                    signupInfo.put("birth", binding.birthEdt.getText().toString().trim());
                    signupInfo.put("phone", binding.phoneEdt.getText().toString().trim());

                    Intent intent = new Intent(SignupSecondActivity.this, SignupThirdActivity.class);
                    intent.putExtra("signupInfo", signupInfo);
                    startActivity(intent);
                }
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

    private boolean isAllFormFilled(){
        if(!isFilled(binding.nameEdt)){
            Utilities.showToast(this, "이름을 입력하세요.");
        }else if(!Utilities.isOnlyKorean(binding.nameEdt.getText().toString().trim())){
            Utilities.showToast(this, "이름은 한글만 입력가능합니다.");
        }else if(!isFilled(binding.birthEdt, 8)){
            Utilities.showToast(this, "생년월일이 8자리인지 확인해주세요.");
        }else if(!isFilled(binding.phoneEdt, 10)){
            Utilities.showToast(this, "핸드폰번호를 확인해주세요.");
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
}
