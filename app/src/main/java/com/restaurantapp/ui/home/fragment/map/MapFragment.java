package com.restaurantapp.ui.home.fragment.map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restaurantapp.R;
import com.restaurantapp.ui.base.BaseFragment;

import butterknife.ButterKnife;

public class MapFragment extends BaseFragment {

    public static MapFragment newInstance() {
        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_map, null);
    }

    @Override
    protected void attachView(View view) {
        ButterKnife.bind(this, view);
    }
}
