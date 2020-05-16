package kr.co.t_woori.good_donation.inquiry;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.InquiryActivityBinding;

/**
 * Created by rladn on 2017-09-26.
 */

public class InquiryActivity extends AppCompatActivity {

    private InquiryActivityBinding binding;

    private ArrayList<Inquiry> inquiryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.inquiry_activity);

        inquiryList = new ArrayList<>();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.listView.setEmptyView(binding.emptyView);

        binding.inquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InquiryInsertDialog inquiryInsertDialog = new InquiryInsertDialog();
                inquiryInsertDialog.setOnInsertQuestionListener(new InquiryInsertDialog.OnInsertQuestionListener() {
                    @Override
                    public void onInsertQuestion() {
                        getMyInquiry();
                    }
                });
                inquiryInsertDialog.show(getSupportFragmentManager(), "inquiryDialog");
            }
        });

        getMyInquiry();
    }

    private void initListView(){
        InquiryListAdapter listAdapter = new InquiryListAdapter(this, inquiryList);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InquiryDetailDialog.create(inquiryList.get(position)).show(getSupportFragmentManager(), "inquiryDetailDialog");
            }
        });
        binding.listView.setAdapter(listAdapter);
    }

    private void getMyInquiry(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                this, APICreator.create(InquiryAPIService.class).getMyInquiry()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(!inquiryList.isEmpty()){
                    inquiryList.clear();
                }
                if(results.get("inquiry") instanceof List){
                    if(Integer.parseInt((String)results.get("count")) > 0){
                        Inquiry inquiry;
                        for(Object json : (List)results.get("inquiry")){
                            if(json instanceof Map){
                                Map map = (Map)json;
                                String id = (String)map.get("idNum");
                                String title = (String)map.get("title");
                                String question = (String)map.get("question");
                                inquiry = new Inquiry(id, title, question);
                                if(map.get("answer") != null){
                                    inquiry.setAnswer((String)map.get("answer"));
                                }
                                inquiryList.add(inquiry);
                            }
                        }
                    }
                    initListView();
                }
            }
        };
        serverCommunicator.execute();
    }
}
