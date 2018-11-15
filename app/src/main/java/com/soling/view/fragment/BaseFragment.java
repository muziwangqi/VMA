package com.soling.view.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.Objects;

public class BaseFragment extends Fragment {

    protected View findViewById(int id) {
        return Objects.requireNonNull(getView()).findViewById(id);
    }

}
