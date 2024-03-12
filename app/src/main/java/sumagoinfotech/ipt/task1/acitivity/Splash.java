package sumagoinfotech.ipt.task1.acitivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.data.UserBean;
import sumagoinfotech.ipt.task1.helper.GoogleHelper;

public class Splash extends AppCompatActivity {

    GoogleHelper gsh;
    GoogleSignInAccount account;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        gsh = new GoogleHelper(this);

        MotionLayout ml = findViewById(R.id.motion_layout);
        ml.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                if(gsh.isUserSignedIn()){
                    account = gsh.getSignedInAccount();
                    intent = new Intent(Splash.this, Dashboard.class);
                    intent.putExtra("user", new UserBean(account.getEmail(), account.getDisplayName(), Splash.this).getId());
                }else {
                    intent = new Intent(Splash.this, MainActivity.class);
                }
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }
}