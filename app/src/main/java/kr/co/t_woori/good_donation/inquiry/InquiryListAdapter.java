package kr.co.t_woori.good_donation.inquiry;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.SimpleRowBinding;

/**
 * Created by rladn on 2017-09-29.
 */

public class InquiryListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Inquiry> inquiryList;

    public InquiryListAdapter(Context context, ArrayList<Inquiry> inquiryList) {
        this.context = context;
        this.inquiryList = inquiryList;
    }


    @Override
    public int getCount() {
        return inquiryList.size();
    }

    @Override
    public Object getItem(int position) {
        return inquiryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.simple_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (SimpleRowBinding) convertView.getTag();
        }

        binding.rowTextView.setText(inquiryList.get(position).getTitle());

        return convertView;
    }
}
