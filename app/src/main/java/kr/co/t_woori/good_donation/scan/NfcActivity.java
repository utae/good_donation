package kr.co.t_woori.good_donation.scan;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;
import java.util.Arrays;

import kr.co.t_woori.good_donation.R;
import kr.co.t_woori.good_donation.databinding.NfcActivityBinding;
import kr.co.t_woori.good_donation.utilities.Utilities;

/**
 * Created by rladn on 2017-08-02.
 */

public class NfcActivity extends AppCompatActivity{

    private NfcActivityBinding binding;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.nfc_activity);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        checkNfcStatus(nfcAdapter);

        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    private void checkNfcStatus(NfcAdapter nfcAdapter){
        if(!nfcAdapter.isEnabled()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("NFC를 활성화해주세요.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
                        startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                    }else{
                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            }).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(nfcAdapter != null){
            checkNfcStatus(nfcAdapter);
        }
    }

    @Override
    protected void onPause() {
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Ndef ndefTag = Ndef.get(tag);

        // 태그 크기
        int size = ndefTag.getMaxSize();
        // 쓰기 가능 여부
        boolean writable = ndefTag.isWritable();
        // 태그 타입
        String type = ndefTag.getType();
        // 태그 ID
        String id = byteArrayToHexString(tag.getId());

        //메세지 읽기
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (messages == null) return;

        String message = setReadTagData((NdefMessage) messages[0]);

        Utilities.logD("Test", message);

        if(message != null && !"".equals(message)){
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tag", message);
            finishWithResult(resultIntent);
        }else{
            Utilities.showToast(this, "기부 태그가 아닙니다.");
            finish();
        }
    }

    private String setReadTagData(NdefMessage ndefmsg) {
        if(ndefmsg == null ) {
            return null;
        }
        String msgs = "";
        msgs += ndefmsg.toString() + "\n";
        NdefRecord[] records = ndefmsg.getRecords();
        NdefRecord record = records[0];

        byte [] payload = record.getPayload();
        String textEncoding = "UTF-8";
        if(payload.length > 0)
            textEncoding = ( payload[0] & 128) == 0 ? "UTF-8" : "UTF-16";

        Short tnf = record.getTnf();
        String type = Arrays.toString(record.getType());
        String payloadStr = new String(record.getPayload(), Charset.forName(textEncoding));

        return payloadStr;
    }

    public static String byteArrayToHexString(byte[] bytes) {
        String data = "";

        for (byte b : bytes) {
            data += Integer.toHexString((b >> 4) & 0xf);
            data += Integer.toHexString(b & 0xf);
        }
        return data;
    }

    private void finishWithResult(Intent intent){
        setResult(RESULT_OK, intent);
        finish();
    }
}
