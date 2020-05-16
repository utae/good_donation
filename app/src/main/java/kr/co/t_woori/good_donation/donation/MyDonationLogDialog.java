package kr.co.t_woori.good_donation.donation;

import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.communication.APICreator;
import kr.co.t_woori.good_donation.communication.ServerCommunicator;
import kr.co.t_woori.good_donation.databinding.MyDonationLogDialogBinding;

/**
 * Created by rladn on 2017-08-10.
 */

public class MyDonationLogDialog extends DialogFragment {

    private MyDonationLogDialogBinding binding;
    private ArrayList<DonationLog> donationLogList;
    private SimpleDonationLogListAdapter listAdapter;
    private String appreciation;

    public static MyDonationLogDialog create(String appreciation){
        MyDonationLogDialog dialog = new MyDonationLogDialog();
        Bundle args = new Bundle();
        args.putString("appreciation", appreciation);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appreciation = getArguments().getString("appreciation");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.my_donation_log_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.appreciationTextView.setText(appreciation);
        binding.appreciationTextView.setMovementMethod(ScrollingMovementMethod.getInstance());

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        donationLogList = new ArrayList<>();

        getMyDonationLog();

        return binding.getRoot();
    }

    private void initListView(){
        listAdapter = new SimpleDonationLogListAdapter(getContext(), donationLogList);
        binding.listView.setAdapter(listAdapter);
    }

    private void getMyDonationLog(){
        ServerCommunicator serverCommunicator = new ServerCommunicator(
                getContext(), APICreator.create(DonationAPIService.class).getMyDonationLog()
        ) {
            @Override
            protected void onSuccess(HashMap<String, Object> results) {
                if(Integer.parseInt((String)results.get("count")) > 0){
                    if(results.get("log") instanceof List){
                        for(Object log : (List)results.get("log")){
                            if(log instanceof Map){
                                Map map = (Map)log;
                                donationLogList.add(new DonationLog((String)map.get("time"), (String)map.get("charityName"), (String)map.get("amount")));
                            }
                        }
                    }
                }else{
                    binding.listView.setEmptyView(binding.emptyView);
                }
                initListView();
            }
        };
        serverCommunicator.execute();
    }
}
