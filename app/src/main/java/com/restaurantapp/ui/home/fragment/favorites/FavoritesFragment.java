package com.restaurantapp.ui.home.fragment.favorites;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseFragment;
import com.restaurantapp.ui.helper.RecyclerTouchListener;
import com.restaurantapp.ui.home.fragment.nearby.RestaurantListAdapter;
import com.restaurantapp.ui.restaurant.detail.RestaurantDetailsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesFragment extends BaseFragment implements FavoritesContract.View {

    @Inject FavoritesPresenter mPresenter;
    @Inject RestaurantListAdapter mAdapter;

    public static FavoritesFragment newInstance() {
        Bundle args = new Bundle();

        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_favorites, null);
    }

    @Override
    protected void attachView(View view) {
        getComponent().inject(this);
        ButterKnife.bind(this, view);

        mPresenter.attachView(this);
        super.attachPresenter(mPresenter);
    }

    @Override
    public void initRecyclerView(List<Restaurant> restaurants) {
        mAdapter.setData(restaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayout.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        mPresenter.onRecyclerViewItemClick(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void updateRecyclerView(List<Restaurant> restaurants) {
        mAdapter.setData(restaurants);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayEmptySnackBar(int messageResId) {
        Snackbar snackbar = Snackbar.make(mParentLayout, getResources().getString(messageResId),
                Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.dismiss_text, view -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void gotoRestaurantsDetails(Restaurant restaurantResponse) {
        Bundle args = RestaurantDetailsActivity.createExtras(restaurantResponse);
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @BindView(R.id.fragment_favorites_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_favorites_parent_container) RelativeLayout mParentLayout;
}