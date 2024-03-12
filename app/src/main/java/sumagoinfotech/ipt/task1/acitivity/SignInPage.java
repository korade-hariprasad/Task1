package sumagoinfotech.ipt.task1.acitivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.helper.DbHelper;
import sumagoinfotech.ipt.task1.helper.GoogleHelper;
import sumagoinfotech.ipt.task1.ui.MyCustomView;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.data.UserBean;

public class SignInPage extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    TextInputEditText et_email, et_pass;
    DbHelper dbHelper;
    TextView tv_toSingUp, tv_toForgot;
    GoogleHelper googleHelper;
    BlurView bv;
    TextInputLayout til_email, til_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        til_email = findViewById(R.id.til_email);
        til_pass = findViewById(R.id.til_pass);
        tv_toSingUp = findViewById(R.id.tv_toSignUp);
        tv_toForgot = findViewById(R.id.tv_forgot);

        dbHelper = new DbHelper(this);

        googleHelper = new GoogleHelper(this);

        //underline text links
        tv_toSingUp.setPaintFlags(tv_toSingUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_toForgot.setPaintFlags(tv_toForgot.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //set blur bg
        bv = findViewById(R.id.blurView);
        MyCustomView.setBlurView(bv, this);

        /////////////////////////////set edittext functions for float
        setFloatFunctionEditText(et_email, til_email);
        setFloatFunctionEditText(et_pass, til_pass);

        /////////////////////////////function for show & hide password
        setShowHideBehaviour();

        findViewById(R.id.btnSignIn).setOnClickListener(v -> {
            String e = et_email.getText().toString(), p = et_pass.getText().toString();
            if (e.isEmpty() || !e.matches(emailPattern)) {
                et_email.setError("Please enter a valid email");
                et_email.requestFocus();
                //}else if(p.isEmpty() || !p.matches(passwordPattern)){
            } else if (p.isEmpty()) {
                et_pass.setError("Please enter valid password");
                et_pass.requestFocus();
            } else if (dbHelper.userExists(e)) {
                FirebaseAuth mAuth = googleHelper.getFirebaseAuth();
                mAuth.signInWithEmailAndPassword(e, p)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Intent toDashboard = new Intent(SignInPage.this, Dashboard.class);
                                    int id = new UserBean(e, p, this).getId();
                                    toDashboard.putExtra("user", id);
                                    startActivity(toDashboard);
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    MyCustomView.showToast("Username or password incorrect", R.drawable.alert, SignInPage.this);
                                }
                            });
            } else {
                MyCustomView.showToast("User does not exists. Please register user", R.drawable.alert, this);
                et_email.setText(null);
                et_pass.setText(null);
            }
        });

        tv_toSingUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInPage.this, VerificationActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        });

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> {
            MyCustomView.showLoadingDialog("Loading", SignInPage.this);
            googleHelper.signIn();
        });

        tv_toForgot.setOnClickListener(v->{
            String e = et_email.getText().toString();
            if (e.isEmpty() || !e.matches(emailPattern)) {
                et_email.setError("Please enter a valid email");
                et_email.requestFocus();
            } else if (dbHelper.userExists(e)) {
                googleHelper.getFirebaseAuth().sendPasswordResetEmail(e).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        MyCustomView.showToast("Please check your email for password reset", R.drawable.sign_up, this);
                    } else {
                        // Failed to send password reset email
                        MyCustomView.showToast("Please check your connection", R.drawable.no_internet, this);
                        // You can add your own error handling logic here
                    }
                });
            }else{
                MyCustomView.showToast("Username incorrect", R.drawable.alert, this);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setShowHideBehaviour() {
        et_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
        et_pass.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (et_pass.getRight() - et_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // toggle password visibility
                    if (et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        et_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_crossed, 0);
                    } else {
                        et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                    }
                    et_pass.requestFocus();
                    return true;
                }
            }
            return false;
        });
    }

    private void setFloatFunctionEditText(TextInputEditText et, TextInputLayout til) {
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
        MyCustomView.dismissLoadingDialog();
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = googleHelper.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    if (dbHelper.userExists(account.getEmail())) {
                        googleHelper.firebaseAuthWithGoogle(account, new GoogleHelper.GoogleSignInListener() {
                            @Override
                            public void onSignInSuccess() {
                                Toast.makeText(SignInPage.this, "User Login Successful", Toast.LENGTH_SHORT).show();
                                Intent toDashboard = new Intent(SignInPage.this, Dashboard.class);
                                int id = new UserBean(account.getEmail(), account.getDisplayName(), SignInPage.this).getId();
                                toDashboard.putExtra("user", id);
                                startActivity(toDashboard);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }

                            @Override
                            public void onSignInFailure() {
                                googleHelper.signOut();
                                MyCustomView.showToast("User not authorized, please try again\nOr check your connection", R.drawable.alert, SignInPage.this);
                            }
                        });
                    } else {
                        googleHelper.signOut();
                        MyCustomView.showToast("user does not exists. please register", R.drawable.alert, SignInPage.this);
                    }
                }
            } catch (ApiException e) {
                // Sign-in failed, handle error
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}