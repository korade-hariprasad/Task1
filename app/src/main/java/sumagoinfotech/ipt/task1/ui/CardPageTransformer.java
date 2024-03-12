package sumagoinfotech.ipt.task1.ui;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CardPageTransformer implements ViewPager.PageTransformer{
    private static final float MIN_SCALE = 0.85f;
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(0);
        } else if (position <= 1) {
            page.setAlpha(1);
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
        } else {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(0);
        }
        page.animate().setDuration(500);
        //page.invalidate();
    }
}