package kr.co.t_woori.good_donation.signup;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.SignupAgreementActivityBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupAgreementActivity extends AppCompatActivity {

    private SignupAgreementActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_agreement_activity);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.agreementChk.isChecked()){
                    Intent intent = new Intent(SignupAgreementActivity.this, SignupFirstActivity.class);
                    startActivity(intent);
                }else{
                    Utilities.showToast(SignupAgreementActivity.this, "약관에 동의하셔야 합니다.");
                }
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
