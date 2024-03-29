package sumagoinfotech.ipt.task1.interfaces;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("sendOtp")
    Call<ResponseBody> sendOtp(@Field("email") String email);

    @FormUrlEncoded
    @POST("verifyOtp")
    Call<ResponseBody> verifyOtp(@Field("email") String email, @Field("userOTP") String otp);
}
