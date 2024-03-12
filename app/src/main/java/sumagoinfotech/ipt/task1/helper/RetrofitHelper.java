package sumagoinfotech.ipt.task1.helper;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.interfaces.ApiService;
import sumagoinfotech.ipt.task1.ui.MyCustomView;

public class RetrofitHelper {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.6:8080/Task1GradleBackend/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static ApiService apiService = retrofit.create(ApiService.class);

    public static void callSendOtpApi(Context context, String email, final OtpCallback callback) {
        Call<ResponseBody> call = apiService.sendOtp(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String result = null;
                    try { result = response.body().string(); } catch (IOException e) {throw new RuntimeException(e);}
                    //if(result.equals("Otp Sent")){}else{}
                    callback.onSuccess(true);
                } else {
                    Toast.makeText(context, "Invalid response", Toast.LENGTH_LONG).show();
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyCustomView.showTextDialog(t.getMessage().toString(), context);
                //Toast.makeText(context, "call failed", Toast.LENGTH_LONG).show();
                callback.onSuccess(false);
            }
        });
    }

    public static void callVerifyOtpApi(Context context, String email, String otp,  final OtpCallback callback) {
        Call<ResponseBody> call = apiService.verifyOtp(email, otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle response
                if (response.isSuccessful()) {
                    String result = null;
                    try {result = response.body().string();} catch (IOException e) {throw new RuntimeException(e);}
                    if(result.equals("Email verified")){
                        callback.onSuccess(true);
                    }else{
                        callback.onSuccess(false);
                    }
                } else {
                    MyCustomView.showToast("Please check your connection", R.drawable.no_internet, context);
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Toast.makeText(context, "Call Failed", Toast.LENGTH_LONG).show();
                callback.onSuccess(false);
            }
        });
    }

    public interface OtpCallback {
        void onSuccess(boolean isSuccess);
    }
}
