package kr.co.t_woori.good_donation.login;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface LoginAPIService {

    @FormUrlEncoded
    @POST("/user/login")
    Call<HashMap<String, Object>> login(@Field("id") String id, @Field("pw") String pw, @Field("idType") String idType);

    @FormUrlEncoded
    @POST("/user/login/auto")
    Call<HashMap<String, Object>> autoLogin(@Field("token") String token);
}
