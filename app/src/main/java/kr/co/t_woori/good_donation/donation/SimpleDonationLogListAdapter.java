package kr.co.t_woori.good_donation.donation;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.DonationLogRowBinding;

/**
 * Created by rladn on 2017-08-17.
 */

public class SimpleDonationLogListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DonationLog> donationLogList;

    public SimpleDonationLogListAdapter(Context context, ArrayList<DonationLog> donationLogList) {
        this.context = context;
        this.donationLogList = donationLogList;
    }

    @Override
    public int getCount() {
        return donationLogList.size();
    }

    @Override
    public Object getItem(int position) {
        return donationLogList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DonationLogRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.donation_log_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (DonationLogRowBinding) convertView.getTag();
        }

        binding.timeTextView.setText(donationLogList.get(position).getTime());
        binding.charityNameTextView.setText(donationLogList.get(position).getCharityName());
        binding.amountTextView.setText(donationLogList.get(position).getAmount());

        return convertView;
    }
}
