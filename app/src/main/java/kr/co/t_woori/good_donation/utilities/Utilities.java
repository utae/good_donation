package kr.co.t_woori.good_donation.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.t_woori.good_donation.BuildConfig;

/**
 * Created by Utae on 2017-07-18.
 */

public class Utilities {
    public static void logD (String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    public static long getCurrentTimeMinutes(){
        return System.currentTimeMillis()/60000;
    }

    public static long convertMinutesToTimeMillis(long minutes){
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    public static String convertTimeMillisToTimeFormat(long timeMillis){
        return convertTimeMillisToTimeFormat(timeMillis, null);
    }

    public static String convertTimeMillisToTimeFormat(long timeMillis, String dateFormat){
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        if(dateFormat == null){
            simpleDateFormat.applyPattern("yyyyMMddHHmmss");
        }else{
            simpleDateFormat.applyPattern(dateFormat);
        }
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static String convertStringToDateFormat(String dateString){
        SimpleDateFormat format = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        format.applyPattern("yyyyMMdd");
        try {
            return SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStringToDateFormat(String dateString, String currentFormatString, String convertFormatString){
        SimpleDateFormat currentFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        currentFormat.applyPattern(currentFormatString);
        SimpleDateFormat convertFormat = (SimpleDateFormat)SimpleDateFormat.getDateInstance();
        convertFormat.applyPattern(convertFormatString);
        try {
            return convertFormat.format(currentFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStringToNumberFormat(String numberString){
        return NumberFormat.getInstance().format(Integer.parseInt(numberString));
    }

    public static float getDip(Context context, int value){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidPassword(CharSequence password){
        String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^&*()+=-]|.*[0-9]+).{6,20}$";
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isOnlyKorean(CharSequence input){
        String Passwrod_PATTERN = "^[가-힣]+$";
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static String byteAsString(byte[] dataBytes) throws Exception{
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataBytes.length; i++) {
            String hex = Integer.toHexString(0xFF & dataBytes[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void hideKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText editText){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return  spans;
    }

    public static ArrayList<String> getSpanStrings(String body, char prefix) {
        ArrayList<String> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        // Check all occurrences
        while (matcher.find()) {
            spans.add(matcher.group());
        }

        return spans;
    }

    static public BitmapFactory.Options getBitmapSize(File imageFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        return options;
    }

    static public void showToast(Context context, CharSequence message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    static public boolean isServiceRunningCheck(Context context, String serviceName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getShortClassName())) {
                return true;
            }
        }
        return false;
    }

    static public String getCharityImgDir(){
        return "http://221.160.225.186/image/charity/";
    }

    static public String getPlaceImgDir(){
        return "http://221.160.225.186/image/place/";
    }

    static public String getHomeBannerDir(){
        return "http://221.160.225.186/image/home/";
    }

    static public void clearGlideCache(Context context){
        Glide.get(context).clearMemory();
        new AsyncTask<Context, Void, Void>() {
            @Override
            protected Void doInBackground(Context... params) {
                Glide.get(params[0]).clearDiskCache();
                return null;
            }
        }.execute(context);
    }
}
