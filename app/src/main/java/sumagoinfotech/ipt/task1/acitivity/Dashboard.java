package sumagoinfotech.ipt.task1.acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.adapter.ViewPagerAdapter;
import sumagoinfotech.ipt.task1.helper.GoogleHelper;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.ui.MyCustomView;

public class Dashboard extends AppCompatActivity {
    private static int USER_ID;
    TextView tvPageNo;
    GoogleHelper googleHelper;
    BlurView bv;
    ImageButton btn_signOut, btnNext, btnPrev;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        USER_ID = getIntent().getIntExtra("user", -1);
        MyCustomView.showToast("Let's get you set up", R.drawable.setup, this);

        tvPageNo = findViewById(R.id.tvPageNo);
        googleHelper = new GoogleHelper(Dashboard.this);
        btn_signOut = findViewById(R.id.btn_signOut);
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        viewPager = findViewById(R.id.viewPager);
        bv = findViewById(R.id.blurView);
        MyCustomView.setBlurView(bv, this);
        //viewPager.setPageTransformer(true, new CardPageTransformer());

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), USER_ID);
        viewPager.setAdapter(viewPagerAdapter);

        btnPrev.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem();
            if (currentPage > 0) {
                viewPager.setCurrentItem(currentPage - 1);
            }
        });

        btnNext.setOnClickListener(v -> {
            int currentPage = viewPager.getCurrentItem();
            if (currentPage < viewPagerAdapter.getCount() - 1) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });

        btn_signOut.setOnClickListener(v -> {
            googleHelper.signOut();
            startActivity(new Intent(Dashboard.this, SignInPage.class));
            finish();
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvPageNo.setText(String.format(Locale.getDefault(), "%d/%d", position + 1, viewPagerAdapter.getCount()));
                btnPrev.setVisibility((position==0)?View.GONE:View.VISIBLE);
                btnNext.setVisibility((position==ViewPagerAdapter.NUM_PAGES-1)?View.GONE:View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleHelper.signOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        googleHelper.signOut();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}