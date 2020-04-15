package kr.co.t_woori.good_donation.rank;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-25.
 */

public interface RankAPIService {

    @GET("/user/rank")
    Call<HashMap<String, Object>> getRank();
}
