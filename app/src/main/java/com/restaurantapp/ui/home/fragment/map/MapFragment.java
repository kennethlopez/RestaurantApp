package com.restaurantapp.ui.home.fragment.map;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import  android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Geometry;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseFragment;
import com.restaurantapp.ui.home.HomeActivity;
import com.restaurantapp.ui.restaurant.detail.RestaurantDetailsActivity;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.ResourceUtil;

import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback {
    @Inject MapPresenter mPresenter;
    @Inject ResourceUtil mResourceUtil;

    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private Hashtable<String, Boolean> mMarkerSet = new Hashtable<>();
    private Hashtable<String, Restaurant> mRestaurantSet = new Hashtable<>();
    private Snackbar mSnackBar;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();

        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  View.inflate(getContext(), R.layout.fragment_map, null);
        ButterKnife.bind(this, view);
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    protected void attachView(View view) {
        getComponent().inject(this);
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
    public void setActionBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void setActionBarTitle(int resId) {
        setActionBarTitle(getResources().getString(resId));
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
    public void setupMap() {
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mPresenter.onMapReady();
    }

    @Override
    public void setMapCurrentLocation(LocationService.Location location, float zoom) {
        mCameraPosition = new CameraPosition.Builder()
                .target(location.getLatLng())
                .zoom(zoom)
                .build();
        mGoogleMap.setOnInfoWindowClickListener(marker ->
                mPresenter.onMarkerClicked(mRestaurantSet.get(marker.getId())));
        mGoogleMap.setOnCameraIdleListener(() ->
                mPresenter.setCurrentZoom(mGoogleMap.getCameraPosition().zoom));
    }

    @Override
    public void addMarkers(List<Restaurant> restaurants) {
        for (Restaurant restaurant : restaurants) addMarker(restaurant);

        mGoogleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getContext(), mMarkerSet,
                mRestaurantSet, mResourceUtil.getGoogleApiKey()));
    }

    @Override
    public void animateCameraPosition() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
    }

    @Override
    public void addMarker(Restaurant restaurant) {
        Geometry.Location location = restaurant.getGeometry().getLocation();
        if (location != null) {
            LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                    restaurant.getGeometry().getLocation().getLng());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .title(restaurant.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker_icon));

            Marker marker = mGoogleMap.addMarker(markerOptions);
            mMarkerSet.put(marker.getId(), false);
            mRestaurantSet.put(marker.getId(), restaurant);
        }
    }

    @Override
    public void clearMap() {
        mGoogleMap.clear();
        mRestaurantSet = new Hashtable<>();
        mMarkerSet = new Hashtable<>();
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
    public void gotoRestaurantDetails(Restaurant restaurant) {
        Bundle args = RestaurantDetailsActivity.createExtras(restaurant);
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }

    @Override
    public void showSnackBar(String message) {
        if (mSnackBar == null) {
            mSnackBar = Snackbar.make(mParentLayout, message, Snackbar.LENGTH_LONG);
            mSnackBar.setAction(R.string.dismiss_text, view -> mSnackBar.dismiss());
        } else mSnackBar.setText(message);
        mSnackBar.show();
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
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @BindView(R.id.fragment_map_map_view) MapView mMapView;
    @BindView(R.id.select_radius_spinner) AppCompatSpinner mSpinner;
    @BindView(R.id.select_radius_spinner_progressbar) ProgressBar mProgressBar;
    @BindView(R.id.fragment_map_parent_container) RelativeLayout mParentLayout;
}