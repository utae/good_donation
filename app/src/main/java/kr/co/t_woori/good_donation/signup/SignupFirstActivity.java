package kr.co.t_woori.good_donation.signup;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.HashMap;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.SignupFirstActivityBinding;
import kr.co.t_woori.good_donation.login.LoginActivity;

/**
 * Created by utae on 2017-07-26.
 */

public class SignupFirstActivity extends AppCompatActivity {

    private SignupFirstActivityBinding binding;

    private HashMap<String, String> signupInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.signup_first_activity);
        signupInfo = new HashMap<>();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupInfo.put("nation", (String)binding.nationSpn.getSelectedItem());
                signupInfo.put("region", (String)binding.regionSpn.getSelectedItem());
                Intent intent = new Intent(SignupFirstActivity.this, SignupSecondActivity.class);
                intent.putExtra("signupInfo", signupInfo);
                startActivity(intent);
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupFirstActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        binding.nationSpn.setAdapter(ArrayAdapter.createFromResource(this, R.array.nation, R.layout.spinner_text_view));
        binding.regionSpn.setAdapter(ArrayAdapter.createFromResource(this, R.array.region, R.layout.spinner_text_view));
    }

    @Override
    public void onBackPressed() {
        binding.cancelBtn.performClick();
    }
}
