package kr.co.t_woori.good_donation.map;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.PlaceDetailDialogBinding;

/**
 * Created by rladn on 2017-10-10.
 */

public class PlaceDetailDialog extends DialogFragment {

    private PlaceDetailDialogBinding binding;

    private Place place;

    static public PlaceDetailDialog create(Place place){
        PlaceDetailDialog dialog = new PlaceDetailDialog();
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        place = (Place) getArguments().getSerializable("place");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.place_detail_dialog, container, false);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL);

        binding.titleTextView.setText(place.getName());

        binding.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initPager();

        return binding.getRoot();
    }

    private void initPager(){
        PlaceImageAdapter adapter = new PlaceImageAdapter(getContext(), place, binding.viewPagerIndicator);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(adapter.getOnPageChangeListener());
    }
}
