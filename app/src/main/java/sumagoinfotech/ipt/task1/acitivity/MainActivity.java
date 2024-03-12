package sumagoinfotech.ipt.task1.acitivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.ui.MyCustomView;
import sumagoinfotech.ipt.task1.R;

public class MainActivity extends AppCompatActivity {

    BlurView bv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bv = findViewById(R.id.blurView);
        MyCustomView.setBlurView(bv, this);

        findViewById(R.id.btnToSignIn).setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, SignInPage.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        findViewById(R.id.btnToSignUp).setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, VerificationActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}