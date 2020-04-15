package kr.co.t_woori.good_donation.signup;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by utae on 2017-07-27.
 */

interface SignupAPIService {

    @FormUrlEncoded
    @POST("/user/signup")
    Call<HashMap<String, Object>> signup(@FieldMap HashMap<String, String> signupInfo);
}
