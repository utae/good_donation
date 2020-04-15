package kr.co.t_woori.good_donation.rank;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.DonationRankActivityBinding;

/**
 * Created by rladn on 2017-09-01.
 */

public class DonationRankActivity extends AppCompatActivity {

    private DonationRankActivityBinding binding;

    private ArrayList<User> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.donation_rank_activity);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userList = new ArrayList<>();

        getRank();
    }

    private void initListView(){
        DonationRankListAdapter listAdapter = new DonationRankListAdapter(this, userList);
        binding.rankListView.setAdapter(listAdapter);
    }

    private void getRank(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(RankAPIService.class).getRank()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(results.get("rank") instanceof List){
                    for(Object json : (List)results.get("rank")){
                        if(json instanceof Map){
                            Map map = (Map)json;
                            userList.add(new User((String)map.get("rank"), (String)map.get("nickname"), (String)map.get("accumulation")));
                        }
                    }
                    initListView();
                }
            }
        };
        serverCommunicator.execute();
    }
}
