package kr.co.t_woori.good_donation.communication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kr.co.t_woori.good_donation.utilities.ProgressDialog;
import kr.co.t_woori.good_donation.utilities.Utilities;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Utae on 2017-07-20.
 */

public abstract class ServerCommunicator extends AsyncTask<Void, Void, Response<HashMap<String, Object>>> {

    private Context context;
    private Call<HashMap<String, Object>> call;
    private ProgressDialog progressDialog = null;

    public ServerCommunicator(Context context, Call<HashMap<String, Object>> call) {
        this(context, call, false);
    }

    public ServerCommunicator(Context context, Call<HashMap<String, Object>> call, boolean isService) {
        this.context = context;
        this.call = call;
        if(!isService){
            this.progressDialog = new ProgressDialog(context);
        }
    }

    @Override
    protected void onPreExecute() {
        if(!NetworkConnectivityManager.isOnline(context)){
            Utilities.showToast(context, "네트워크를 확인해주세요.");
            cancel(true);
        }else{
            if(progressDialog != null){
//                progressDialog.show();
            }
        }
    }

    @Override
    protected Response<HashMap<String, Object>> doInBackground(Void... params) {
        Response<HashMap<String, Object>> response = null;
        if(!isCancelled()){
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response<HashMap<String, Object>> response) {
        dismissProgressDialog();
        if(response != null){
            Utilities.logD("Test", "response code : " + response.code());
            if(response.isSuccessful()){
                onSuccess(response.body());
                return;
            }
        }
        onServerError(response);
    }

    protected abstract void onSuccess(HashMap<String, Object> results);

    protected void onServerError(Response<HashMap<String, Object>> response){
        String message;
        if(response == null){
            message = "서버와 연결할 수 없습니다.";
        }else{
            message = response.message();
        }
        Utilities.showToast(context, message);
    }

    public void dismissProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
