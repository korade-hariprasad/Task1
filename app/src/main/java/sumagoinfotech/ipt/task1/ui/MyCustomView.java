package sumagoinfotech.ipt.task1.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.interfaces.ApiService;

public class MyCustomView {
    private static Dialog dialog;
    private static Drawable ALERT, SIGN_IN, SIGN_UP;

    public static void setBlurView(BlurView bv, Context context) {
        float radius = 13f;
        View decorView = ((Activity) context).getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        bv.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        bv.setClipToOutline(true);
        bv.setupWith(rootView, new RenderScriptBlur(context)) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(radius);
    }

    public static void showToast(String s, int imageDrawable, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);
        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        Button okButton = dialogView.findViewById(R.id.okButton);
        ImageView img = dialogView.findViewById(R.id.iv);
        Drawable image = ContextCompat.getDrawable(context, imageDrawable);
        img.setImageDrawable(image);
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        img.setAdjustViewBounds(true);
        messageTextView.setText(s);
        okButton.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.alert_img);
        img.startAnimation(anim);
        dialog.show();
    }

    public static void showLoadingDialog(String message, Context context) {
        dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_loading_dialog, null);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.edittext_dark_container_bg);
        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        messageTextView.setText(message);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showTextDialog(String message, Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView textView = new TextView(context);
        textView.setText(message);
        dialog.setContentView(textView);
        dialog.show();
    }

    public static void dismissLoadingDialog() {
        if(dialog.isShowing()) dialog.dismiss();
    }
}
