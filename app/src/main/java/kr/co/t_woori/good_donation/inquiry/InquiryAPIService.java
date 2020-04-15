package kr.co.t_woori.good_donation.inquiry;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface InquiryAPIService {

    @FormUrlEncoded
    @POST("/common/inquiry/q")
    Call<HashMap<String, Object>> insertQuestion(@Field("title") String title, @Field("question") String question);

    @GET("/common/inquiry/my")
    Call<HashMap<String, Object>> getMyInquiry();
}
