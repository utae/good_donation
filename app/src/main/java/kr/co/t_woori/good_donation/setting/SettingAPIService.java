package kr.co.t_woori.good_donation.setting;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface SettingAPIService {

    @GET("/user/profile")
    Call<HashMap<String, Object>> getMyProfile();

    @FormUrlEncoded
    @POST("/user/profile")
    Call<HashMap<String, Object>> saveMyProfile(@Field("name") String name, @Field("birth") String birth);
}
