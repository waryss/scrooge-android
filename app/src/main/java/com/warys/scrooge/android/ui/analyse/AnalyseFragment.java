package com.warys.scrooge.android.ui.analyse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.warys.scrooge.android.R;
import com.warys.scrooge.android.domain.Context;

import static com.warys.scrooge.android.domain.Context.PageFragment.ANALYSE;

public class AnalyseFragment extends Fragment {

    private AnalyseViewModel analyseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Context.getInstance().setCurrentFragment(ANALYSE);
        analyseViewModel = ViewModelProviders.of(this).get(AnalyseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_analyse, container, false);
        final TextView textView = root.findViewById(R.id.text_analyse);
        analyseViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}