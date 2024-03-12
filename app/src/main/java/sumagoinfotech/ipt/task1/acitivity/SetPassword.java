package sumagoinfotech.ipt.task1.acitivity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.helper.DbHelper;
import sumagoinfotech.ipt.task1.helper.GoogleHelper;
import sumagoinfotech.ipt.task1.ui.MyCustomView;
import sumagoinfotech.ipt.task1.R;

public class SetPassword extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    TextInputEditText et_email, et_pass, et_rePass;
    DbHelper dbHelper;
    GoogleHelper googleHelper;
    BlurView bv;

    TextInputLayout til_email, til_pass, til_repass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_rePass = findViewById(R.id.et_repass);
        til_email = findViewById(R.id.til_email);
        til_pass = findViewById(R.id.til_pass);
        til_repass = findViewById(R.id.til_repass);

        dbHelper = new DbHelper(this);

        googleHelper = new GoogleHelper(this);

        et_email.setText(getIntent().getStringExtra("email"));
        //set blur bg
        bv = findViewById(R.id.blurView);
        MyCustomView.setBlurView(bv, this);

        /////////////////////////////set edittext functions for float
        setFloatFunctionEditText(et_email, til_email);
        setFloatFunctionEditText(et_rePass, til_repass);
        setFloatFunctionEditText(et_pass, til_pass);
        /////////////////////////////function for show & hide password
        setShowHideBehaviour(et_pass);
        setShowHideBehaviour(et_rePass);

        findViewById(R.id.btnSignUp).setOnClickListener(v->{
            String e=et_email.getText().toString(), p=et_pass.getText().toString(), rp=et_rePass.getText().toString();
            if(e.isEmpty() || !e.matches(emailPattern)){
                et_email.setError("Please enter a valid email");
                et_email.requestFocus();
            }else if(p.isEmpty() || !p.matches(passwordPattern)){
                et_pass.setError("Please enter valid password");
                et_pass.requestFocus();
            }else if (!p.equals(rp)){
                et_rePass.setError("New password does not matches");
                et_rePass.requestFocus();
            }else{
                //enter signUpWithPasswordAndEmail method here
                if(dbHelper.userExists(e)){
                    MyCustomView.showToast("User already registered", R.drawable.alert ,this);
                }else{
                        googleHelper.getFirebaseAuth().createUserWithEmailAndPassword(e, p)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        if (dbHelper.insertUser(e, p)) {
                                            MyCustomView.showToast("User created, please login", R.drawable.sign_up, SetPassword.this);
                                            sendVerificationEmail();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startActivity(new Intent(SetPassword.this, SignInPage.class));
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    finish();
                                                }
                                            },3000);
                                        } else {
                                            MyCustomView.showToast("User cannot be created, please try again", R.drawable.alert, SetPassword.this);
                                        }
                                    } else {
                                        MyCustomView.showToast("User already registered", R.drawable.alert, SetPassword.this);
                                    }
                                });
                }
            }
        });

        /*
        findViewById(R.id.btnGoogleSignUp).setOnClickListener(v->{
            MyCustomView.showLoadingDialog("Loading", SetPassword.this);
                googleHelper.signIn();
        });
        */
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setShowHideBehaviour(TextInputEditText et) {
        et.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
        et.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (et.getRight() - et.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // toggle password visibility
                    if (et.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        et.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_crossed, 0);
                    } else {
                        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                    }
                    et.requestFocus();
                    return true;
                }
            }
            return false;
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = googleHelper.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if(dbHelper.userExists(account.getEmail())){
                        googleHelper.signOut();
                        MyCustomView.showToast("User already registered", R.drawable.alert ,this);
                    }else {
                            googleHelper.firebaseAuthWithGoogle(account, new GoogleHelper.GoogleSignInListener() {
                                @Override
                                public void onSignInSuccess() {
                                    if (dbHelper.insertUser(account.getEmail(), account.getDisplayName())) {
                                        sendVerificationEmail();
                                        googleHelper.signOut();
                                        MyCustomView.showToast("User created, please login", R.drawable.sign_up, SetPassword.this);
                                        //finish();
                                    } else {
                                        MyCustomView.showToast("User cannot be created, please try again", R.drawable.sign_up, SetPassword.this);
                                    }
                                }

                                @Override
                                public void onSignInFailure() {
                                    googleHelper.signOut();
                                    MyCustomView.showToast("User not authorized, please try again", R.drawable.alert, SetPassword.this);
                                }
                            });

                    }
                }
            } catch (ApiException e) {
                // Sign-in failed, handle error
                googleHelper.signOut();
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
        MyCustomView.dismissLoadingDialog();
    }

    private void sendVerificationEmail(){
        FirebaseUser user = googleHelper.getFirebaseAuth().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                if (task.isSuccessful()) Log.d(TAG, "Verification email sent.");
                else Log.e(TAG, "Failed to send verification email.", task.getException());
            });
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Confirmation")
                .setMessage("Are you sure you want to go back? You will have to reverify your email & phone number")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    super.onBackPressed();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // User canceled, do nothing
                }).show();
    }
}