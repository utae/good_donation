package kr.co.t_woori.good_donation.scan;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.ScanFragmentBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-01.
 */

public class ScanFragment extends Fragment {

    private ScanFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scan_fragment, container, false);
        binding = ScanFragmentBinding.bind(view);

        init();

        return view;
    }

    private void init(){
        binding.qrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.initiateScan();
            }
        });

        binding.nfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNfcExisting()){
                    Intent intent = new Intent(getContext(), NfcActivity.class);
                    startActivityForResult(intent, 0);
                }else{
                    Utilities.showToast(getContext(), "NFC를 지원하지 않는 단말기입니다.");
                }
            }
        });
    }

    private boolean isNfcExisting(){
        return NfcAdapter.getDefaultAdapter(getContext()) != null;
    }
}
