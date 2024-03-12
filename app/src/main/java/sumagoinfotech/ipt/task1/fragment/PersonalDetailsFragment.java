package sumagoinfotech.ipt.task1.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import eightbitlab.com.blurview.BlurView;
import sumagoinfotech.ipt.task1.R;
import sumagoinfotech.ipt.task1.ui.MyCustomView;

public class PersonalDetailsFragment extends Fragment {

    TextView tv_heading;
    Context context;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal_details, container, false);
        context = getActivity();
        tv_heading = rootView.findViewById(R.id.tv);
        tv_heading.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        return rootView;
    }
}
