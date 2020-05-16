package kr.co.t_woori.good_donation.donation;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.MyDonationActivityBinding;

/**
 * Created by rladn on 2017-10-10.
 */

public class MyDonationActivity extends AppCompatActivity {

    private MyDonationActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.my_donation_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getMyDonationInfo();
    }

    private void initListView(ArrayList<DonationLog> logList, ArrayList<Charity> projectList){
        SimpleDonationLogListAdapter logListAdapter = new SimpleDonationLogListAdapter(this, logList);
        binding.logListView.setAdapter(logListAdapter);

        SimpleChooseCharityListAdapter charityListAdapter = new SimpleChooseCharityListAdapter(this, projectList);
        binding.projectListView.setAdapter(charityListAdapter);
    }

    private void getMyDonationInfo(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(DonationAPIService.class).getMyDonationInfo()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                ArrayList<DonationLog> logList = new ArrayList<>();
                ArrayList<Charity> projectList = new ArrayList<>();
                binding.rankTextView.setText((String)results.get("rank"));
                binding.amountTextView.setText((String)results.get("accumulation"));
                if(Integer.parseInt((String)results.get("log_count")) > 0){
                    if(results.get("log") instanceof List){
                        for(Object log : (List)results.get("log")){
                            if(log instanceof Map){
                                Map map = (Map)log;
                                logList.add(new DonationLog((String)map.get("time"), (String)map.get("charityName"), (String)map.get("amount")));
                            }
                        }
                    }
                }else{
                    binding.logListView.setEmptyView(binding.logEmptyView);
                }

                if(Integer.parseInt((String)results.get("project_count")) > 0){
                    if(results.get("project") instanceof List){
                        for(Object log : (List)results.get("project")){
                            if(log instanceof Map){
                                Map map = (Map)log;
                                projectList.add(new Charity((String)map.get("idNum"), (String)map.get("name")));
                            }
                        }
                    }
                }else{
                    binding.projectListView.setEmptyView(binding.projectEmptyView);
                }

                initListView(logList, projectList);
            }
        };
        serverCommunicator.execute();
    }
}
