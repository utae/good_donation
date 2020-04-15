package kr.co.t_woori.good_donation.communication;

import java.io.IOException;

import kr.co.t_woori.good_donation.utilities.UserInfo;
import kr.co.t_woori.good_donation.utilities.Utilities;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rladn on 2017-08-07.
 */

class AddTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("timestamp", Utilities.convertTimeMillisToTimeFormat(System.currentTimeMillis()));
        if(UserInfo.getToken() != null){
            builder.addHeader("token", UserInfo.getToken());
        }
        return chain.proceed(builder.build());
    }
}
