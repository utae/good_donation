package kr.co.t_woori.good_donation.home;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface HomeAPIService {

    @GET("/user/no1/user")
    Call<HashMap<String, Object>> getNo1User();

    @GET("/user/no1/place")
    Call<HashMap<String, Object>> getNo1Place();

    @GET("/common/home/banner")
    Call<HashMap<String, Object>> getHomeBanner();
}
