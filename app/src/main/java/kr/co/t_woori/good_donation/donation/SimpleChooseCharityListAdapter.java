package kr.co.t_woori.good_donation.donation;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.charity.Charity;
import kr.co.t_woori.good_donation.databinding.SimpleRowBinding;

/**
 * Created by rladn on 2017-08-14.
 */

public class SimpleChooseCharityListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Charity> charityList;

    public SimpleChooseCharityListAdapter(Context context, ArrayList<Charity> charityList) {
        this.context = context;
        this.charityList = charityList;
    }

    @Override
    public int getCount() {
        return charityList.size();
    }

    @Override
    public Object getItem(int position) {
        return charityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(charityList.get(position).getIdNum());
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

        binding.rowTextView.setText(charityList.get(position).getName());

        return convertView;
    }

    public void latestSort(){
        Collections.sort(charityList, new LatestSort());
        notifyDataSetChanged();
    }

    private class LatestSort implements Comparator<Charity> {

        @Override
        public int compare(Charity o1, Charity o2) {
            return o2.getIdNum().compareTo(o1.getIdNum());
        }
    }
}
