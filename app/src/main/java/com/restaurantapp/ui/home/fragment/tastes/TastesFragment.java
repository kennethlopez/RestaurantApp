package com.restaurantapp.ui.home.fragment.tastes;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restaurantapp.R;
import com.restaurantapp.ui.base.BaseFragment;

public class TastesFragment extends BaseFragment {

    public static TastesFragment newInstance() {
        Bundle args = new Bundle();

        TastesFragment fragment = new TastesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_tastes, null);
    }

    @Override
    protected void attachView(View view) {

    }
}
