package kr.co.t_woori.good_donation.donation;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface DonationAPIService {

    @GET("/user/donation/my")
    Call<HashMap<String, Object>> getMyDonationLog();

    @GET("/user/accumulation/my")
    Call<HashMap<String, Object>> getMyAccumulation();

    @GET("/user/accumulation/all")
    Call<HashMap<String, Object>> getAllAccumulation();

    @FormUrlEncoded
    @POST("/user/donation")
    Call<HashMap<String, Object>> donate(@Field("token") String token, @Field("charityId") String charityId);

    @GET("/user/info/donation")
    Call<HashMap<String, Object>> getMyDonationInfo();
}
