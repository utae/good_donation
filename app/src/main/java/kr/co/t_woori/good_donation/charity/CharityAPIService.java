package kr.co.t_woori.good_donation.charity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface CharityAPIService {

    @GET("/common/charity/my")
    Call<HashMap<String, Object>> getMyCharity();

    @GET("/common/charity/recommend")
    Call<HashMap<String, Object>> getRecommendCharity();

    @GET("/common/charity/all")
    Call<HashMap<String, Object>> getAllCharity();

    @GET("/common/project/my")
    Call<HashMap<String, Object>> getMyProject();

    @GET("/common/project/all")
    Call<HashMap<String, Object>> getAllProject();

    @FormUrlEncoded
    @POST("/common/charity/my")
    Call<HashMap<String, Object>> modMyCharity(@Field("addedList") String addedList, @Field("removedList") String removedList);

    @FormUrlEncoded
    @POST("/common/charity/follow")
    Call<HashMap<String, Object>> followCharity(@Field("charityId") String charityId);

    @FormUrlEncoded
    @POST("/common/charity/unfollow")
    Call<HashMap<String, Object>> unFollowCharity(@Field("charityId") String charityId);
}
