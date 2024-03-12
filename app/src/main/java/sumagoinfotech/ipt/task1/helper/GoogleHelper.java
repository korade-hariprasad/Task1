package sumagoinfotech.ipt.task1.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.ui.MyCustomView;

public class GoogleHelper {
    private static final String TAG = "GoogleHelper";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Activity mActivity;
    private static final int RC_SIGN_IN = 9001;

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

    public GoogleHelper(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        mActivity = activity;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public Task<GoogleSignInAccount> getSignedInAccountFromIntent(Intent data) {
        return GoogleSignIn.getSignedInAccountFromIntent(data);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct, GoogleSignInListener listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSignInSuccess();
                    } else {
                        listener.onSignInFailure();
                    }
                });
    }

    public interface GoogleSignInListener {
        void onSignInSuccess();

        void onSignInFailure();
    }

    public void signOut() {
        mGoogleSignInClient.signOut();
    }

    public boolean isUserSignedIn() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mActivity);
        return account != null;
    }

    public GoogleSignInAccount getSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(mActivity);
    }
}
