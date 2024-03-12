package sumagoinfotech.ipt.task1.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import sumagoinfotech.ipt.task1.fragment.ConsentFragment;
import sumagoinfotech.ipt.task1.fragment.ContactDetailsFragment;
import sumagoinfotech.ipt.task1.fragment.InterestsFragment;
import sumagoinfotech.ipt.task1.fragment.PersonalDetailsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final int NUM_PAGES = 4;
    private static int USER_ID;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int b) {
        super(fm);
        USER_ID = b;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return createFragmentWithData(new PersonalDetailsFragment());
            case 1:
                return createFragmentWithData(new ContactDetailsFragment());
            case 2:
                return createFragmentWithData(new InterestsFragment());
            case 3:
                return createFragmentWithData(new ConsentFragment());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    private Fragment createFragmentWithData(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("user_id", USER_ID);
        fragment.setArguments(bundle);
        return fragment;
    }
}