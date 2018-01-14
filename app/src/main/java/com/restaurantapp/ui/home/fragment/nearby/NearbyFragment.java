package com.restaurantapp.ui.home.fragment.nearby;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ProgressBar;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.restaurantapp.R;
import com.restaurantapp.ui.base.BaseFragment;
import com.restaurantapp.data.api.response.PlaceResponse;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.ui.adapter.RestaurantListAdapter;
import com.restaurantapp.ui.helper.RecyclerTouchListener;
import com.restaurantapp.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NearbyFragment extends BaseFragment implements NearbyContract.View {
    private static final int PLACE_PICKER_REQUEST = 101;
    public static final int REQUEST_TURN_ON_LOCATION = 102;

    @Inject NearbyPresenter mPresenter;
    @Inject RxBus mBus;
    @Inject LocationService mLocationService;

    private RestaurantListAdapter mAdapter;
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

        mAdapter = new RestaurantListAdapter(new ArrayList<>(), mBus);

        mPresenter.attachView(this);
        super.attachPresenter(mPresenter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_find_place_fragment, menu);
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
    public void onResolvableApiException(ResolvableApiException resolvable) {
        // Location settings are not satisfied, but this can be fixed
        // by showing the user a dialog.
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            resolvable.startResolutionForResult(getActivity(), REQUEST_TURN_ON_LOCATION);
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
    public void setSpinner(int textArrayResId, int position) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                textArrayResId, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(position);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                mPresenter.onSpinnerItemSelected(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void initRecyclerView(List<PlaceResponse> places) {
        mAdapter.setData(places);
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
                        mPresenter.onRecyclerViewItemClick();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void updateRecyclerView(List<PlaceResponse> places) {
        mAdapter.setData(places);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public LocationService getLocationService() {
        return mLocationService;
    }

    @Override
    public String getApiKey(int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void scrollToPosition(int position) {
        mLayoutManager.scrollToPosition(position);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showSmallProgressBar() {
        mSmallProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSmallProgressBar() {
        mSmallProgressBar.setVisibility(View.GONE);
    }

    @BindView(R.id.fragment_nearby_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_nearby_progressbar) ProgressBar mProgressBar;
    @BindView(R.id.fragment_nearby_spinner) AppCompatSpinner mSpinner;
    @BindView(R.id.fragment_nearby_progressbar_small) ProgressBar mSmallProgressBar;
}