package com.restaurantapp.ui.home.fragment.nearby;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseFragment;
import com.restaurantapp.ui.home.HomeActivity;
import com.restaurantapp.ui.restaurant.detail.RestaurantDetailsActivity;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.ui.helper.RecyclerTouchListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NearbyFragment extends BaseFragment implements NearbyContract.View {

    @Inject NearbyPresenter mPresenter;
    @Inject RestaurantListAdapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;

    public static NearbyFragment newInstance() {
        Bundle args = new Bundle();

        NearbyFragment fragment = new NearbyFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return View.inflate(getContext(), R.layout.fragment_nearby, null);
    }

    @Override
    protected void attachView(View view) {
        getComponent().inject(this);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mPresenter.attachView(this);
        super.attachPresenter(mPresenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_nearby_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onOptionsItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(getContext(), data);
                    mPresenter.onCurrentLocationPicked(new LocationService.Location(place));
                }
                break;
        }
    }

    @Override
    public void startLocationPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gotoRestaurantsDetails(Restaurant restaurantResponse) {
        Bundle args = RestaurantDetailsActivity.createExtras(restaurantResponse);
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void onResolvableApiException(ResolvableApiException resolvable) {
        // Location settings are not satisfied, but this can be fixed
        // by showing the user a dialog.
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            resolvable.startResolutionForResult(getActivity(), HomeActivity.REQUEST_TURN_ON_LOCATION);
        } catch (IntentSender.SendIntentException sendEx) {
            // Ignore the error.
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void setActionBarTitle(int resId) {
        setActionBarTitle(getResources().getString(resId));
    }

    @Override
    public void setSpinner(int textArrayResId, int position) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                textArrayResId, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                mPresenter.onSpinnerItemSelected(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        new Handler().postDelayed(() -> mSpinner.setSelection(position, true), 100);
    }

    @Override
    public void initRecyclerView(List<Restaurant> restaurants) {
        mAdapter.setData(restaurants);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
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
    public void setSwipeRefresh() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorAccent,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefresh.setOnRefreshListener(() -> mPresenter.onRefresh());
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mSwipeRefresh.setRefreshing(refreshing);
    }

    @Override
    public void updateRecyclerView(List<Restaurant> places) {
        mAdapter.setData(places);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void scrollToPosition(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    @BindView(R.id.fragment_nearby_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.select_radius_spinner) AppCompatSpinner mSpinner;
    @BindView(R.id.fragment_nearby_swipe_refresh) SwipeRefreshLayout mSwipeRefresh;
}