package kr.co.t_woori.good_donation.rank;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.RankRowBinding;

/**
 * Created by rladn on 2017-09-01.
 */

public class DonationRankListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> userList;

    public DonationRankListAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankRowBinding binding;

        if(convertView == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.rank_row, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }else{
            binding = (RankRowBinding) convertView.getTag();
        }

        binding.rankTextView.setText(userList.get(position).getRank());
        binding.nameTextView.setText(userList.get(position).getNickName());
        binding.accumTextView.setText(userList.get(position).getAccumulation());

        return convertView;
    }
}
