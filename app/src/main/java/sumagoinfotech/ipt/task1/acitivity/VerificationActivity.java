package sumagoinfotech.ipt.task1.acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.helper.GoogleHelper;
import sumagoinfotech.ipt.task1.helper.RetrofitHelper;
import sumagoinfotech.ipt.task1.ui.MyCustomView;

public class VerificationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 100;
    TextView tv_heading, tv_resendPhoneTime, tv_resendEmailTime;
    TextInputEditText et_phone, et_email;
    EditText otpe1, otpe2, otpe3, otpe4, otpe5, otpe6,  otpp1, otpp2, otpp3, otpp4, otpp5, otpp6;
    LinearLayout ll_otp_email_container, ll_otp_phone_container;
    TextInputLayout til_phone, til_email;
    Button btn_phone_otp, btn_email_otp, btn_verify_phone, btn_verify_email;
    FirebaseAuth mAuth;
    String phoneVerificationId;
    BlurView bv;
    private CountDownTimer countDownTimerPhone, countDownTimerEmail;
    private long timeLeftInMillisPhone, timeLeftInMillisEmail;
    private static final long COUNTDOWN_TIME = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        tv_heading = findViewById(R.id.tv);
        tv_resendEmailTime = findViewById(R.id.tv_resendEmailTime);
        tv_resendPhoneTime = findViewById(R.id.tv_resendPhoneTime);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        otpe1 = findViewById(R.id.et_otp_e1);
        otpe2 = findViewById(R.id.et_otp_e2);
        otpe3 = findViewById(R.id.et_otp_e3);
        otpe4 = findViewById(R.id.et_otp_e4);
        otpe5 = findViewById(R.id.et_otp_e5);
        otpe6 = findViewById(R.id.et_otp_e6);
        otpp1 = findViewById(R.id.et_otp_p1);
        otpp2 = findViewById(R.id.et_otp_p2);
        otpp3 = findViewById(R.id.et_otp_p3);
        otpp4 = findViewById(R.id.et_otp_p4);
        otpp5 = findViewById(R.id.et_otp_p5);
        otpp6 = findViewById(R.id.et_otp_p6);
        ll_otp_email_container = findViewById(R.id.ll_otp_email_container);
        ll_otp_phone_container = findViewById(R.id.ll_otp_phone_container);
        til_phone = findViewById(R.id.til_phone);
        til_email = findViewById(R.id.til_email);
        btn_phone_otp = findViewById(R.id.btn_phone_OTP);
        btn_email_otp = findViewById(R.id.btnGetEmailOtp);
        btn_verify_phone = findViewById(R.id.btn_verify_phone);
        btn_verify_email = findViewById(R.id.btnVerifyEmail);
        bv = findViewById(R.id.blurView);
        mAuth = new GoogleHelper(this).getFirebaseAuth();

        MyCustomView.setBlurView(bv, this);
        tv_heading.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        setFloatFunctionEditText(et_email, til_email);
        setFloatFunctionEditText(et_phone, til_phone);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        } else {
            // Permission is already granted
            // You can perform SMS related operations here
        }
        
        //otp edittext behaviour
        setEditTextWatcher(otpe1, otpe2);
        setEditTextWatcher(otpe2, otpe3);
        setEditTextWatcher(otpe3, otpe4);
        setEditTextWatcher(otpe4, otpe5);
        setEditTextWatcher(otpe5, otpe6);
        setEditTextWatcher(otpe6, null);
        setEditTextWatcher(otpp1, otpp2);
        setEditTextWatcher(otpp2, otpp3);
        setEditTextWatcher(otpp3, otpp4);
        setEditTextWatcher(otpp4, otpp5);
        setEditTextWatcher(otpp5, otpp6);
        setEditTextWatcher(otpp6, null);

        //phone get otp
        btn_phone_otp.setOnClickListener(v->{
            if (TextUtils.isEmpty(et_phone.getText().toString())) {
                et_phone.setError("Please enter a valid number");
            }else {
                startCountDownPhone();
                MyCustomView.showLoadingDialog("Please wait", this);
                String phoneNumber = "+91"+et_phone.getText().toString().trim();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(30L, TimeUnit.SECONDS) // Timeout duration
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                // This method will be invoked when verification is done automatically
                                MyCustomView.dismissLoadingDialog();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // This method will be invoked if verification fails
                                MyCustomView.dismissLoadingDialog();
                                Toast.makeText(VerificationActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // This method will be invoked when the code is successfully sent
                                // You can store the verification ID to verify the code later
                                MyCustomView.dismissLoadingDialog();
                                phoneVerificationId = verificationId;
                                ll_otp_phone_container.setVisibility(View.VISIBLE);
                                btn_verify_phone.setVisibility(View.VISIBLE);
                                tv_resendPhoneTime.setVisibility(View.VISIBLE);
                            }
                        }).build();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(options);
            }
        });

        btn_verify_phone.setOnClickListener(v-> {
            if(otpp1.getText().toString().isEmpty()||otpp2.getText().toString().isEmpty()||otpp3.getText().toString().isEmpty()||otpp4.getText().toString().isEmpty()||otpp5.getText().toString().isEmpty()||otpp6.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            }else{
                String enteredCode = otpp1.getText().toString()+otpp2.getText().toString()+otpp3.getText().toString()+otpp4.getText().toString()+otpp5.getText().toString()+otpp6.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, enteredCode);
                if(credential.getSmsCode().equals(enteredCode)) {
                    MyCustomView.showToast("Phone verified", R.drawable.sign_up, this);
                    new Handler().postDelayed(() -> {
                        Intent toSignUp = new Intent(VerificationActivity.this, SetPassword.class);
                        toSignUp.putExtra("email", et_email.getText().toString());
                        startActivity(toSignUp);
                        finish();
                    }, 3000);
                }
            }
        });

        //phone get otp
        btn_email_otp.setOnClickListener(v-> {
            if (TextUtils.isEmpty(et_email.getText().toString())) {
                et_email.setError("Please enter a valid email ID");
                et_email.requestFocus();
            } else {
                startCountDownEmail();
                String email = et_email.getText().toString().trim();
                RetrofitHelper.callSendOtpApi(this, email, isSuccess -> {
                    if (isSuccess) {
                        Toast.makeText(VerificationActivity.this, "Otp send, please check your email", Toast.LENGTH_SHORT).show();
                        btn_verify_email.setVisibility(View.VISIBLE);
                        ll_otp_email_container.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        btn_verify_email.setOnClickListener(v-> {
            if(otpe1.getText().toString().isEmpty()||otpe2.getText().toString().isEmpty()||otpe3.getText().toString().isEmpty()||otpe4.getText().toString().isEmpty()||otpe5.getText().toString().isEmpty()||otpe6.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
            }else{
                String enteredCode = otpe1.getText().toString()+otpe2.getText().toString()+otpe3.getText().toString()+otpe4.getText().toString()+otpe5.getText().toString()+otpe6.getText().toString();
                String email = et_email.getText().toString().trim();
                RetrofitHelper.callVerifyOtpApi(this, email, enteredCode, isSuccess -> {
                    if (isSuccess) {
                        MyCustomView.showToast("Email Verified", R.drawable.sign_up, this);
                        til_phone.setVisibility(View.VISIBLE);
                        btn_phone_otp.setVisibility(View.VISIBLE);
                        tv_resendPhoneTime.setVisibility(View.VISIBLE);
                    }else MyCustomView.showToast("Incorrect OTP entered", R.drawable.alert, this);
                });
            }
        });
    }

    private void startCountDownPhone() {
        countDownTimerPhone = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisPhone = millisUntilFinished;
                updateCountDownTextPhone();
                btn_phone_otp.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btn_phone_otp.setEnabled(true);
                tv_resendPhoneTime.setText("");
            }
        }.start();
    }

    private void updateCountDownTextPhone() {
        int seconds = (int) (timeLeftInMillisPhone / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "New OTP in %02d seconds", seconds);
        tv_resendPhoneTime.setText(timeLeftFormatted);
    }

    private void cancelCountdownPhone() {
        if (countDownTimerPhone != null) {
            tv_resendPhoneTime.setText("Resend OTP");
            btn_phone_otp.setEnabled(true);
            countDownTimerPhone.cancel();
        }
    }

    private void startCountDownEmail() {
        countDownTimerEmail = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisEmail = millisUntilFinished;
                updateCountDownTextEmail();
                btn_email_otp.setEnabled(false);
            }

            @Override
            public void onFinish() {
                btn_email_otp.setEnabled(true);
                tv_resendEmailTime.setText("");
            }
        }.start();
    }

    private void updateCountDownTextEmail() {
        int seconds = (int) (timeLeftInMillisEmail / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "New OTP in %02d seconds", seconds);
        tv_resendEmailTime.setText(timeLeftFormatted);
    }

    private void cancelCountdownEmail() {
        if (countDownTimerEmail != null) {
            tv_resendEmailTime.setText("Resend OTP");
            btn_email_otp.setEnabled(true);
            countDownTimerEmail.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelCountdownPhone();
        cancelCountdownEmail();
    }

    private void setFloatFunctionEditText(EditText et, TextInputLayout til) {
        et.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                til.setHintEnabled(true);
                til.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            } else {
                if (et.getText().toString().isEmpty()) {
                    til.setHintEnabled(false);
                    til.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_NONE);
                }
            }
        });
    }

    private void setEditTextWatcher(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not needed
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1 && nextEditText != null) {
                    // Move focus to the next EditText
                    nextEditText.requestFocus();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                // You can perform SMS related operations here
            } else {
                // Permission is denied
                // You may need to handle this situation accordingly
            }
        }
    }
}