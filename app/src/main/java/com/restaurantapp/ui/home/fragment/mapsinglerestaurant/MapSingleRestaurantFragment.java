package com.restaurantapp.ui.home.fragment.mapsinglerestaurant;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.ui.base.BaseFragment;
import com.restaurantapp.ui.home.fragment.map.MapInfoWindowAdapter;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.LocationService;
import com.restaurantapp.util.ResourceUtil;

import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapSingleRestaurantFragment extends BaseFragment implements
        MapSingleRestaurantContract.View, OnMapReadyCallback, Constants.BundleKeys {

    @Inject MapSingleRestaurantPresenter mPresenter;
    @Inject ResourceUtil mResourceUtil;

    private GoogleMap mGoogleMap;
    private CameraPosition mCameraPosition;
    private Hashtable<String, Boolean> mMarkerSet = new Hashtable<>();
    private Hashtable<String, Restaurant> mRestaurantSet = new Hashtable<>();

    public static MapSingleRestaurantFragment newInstance(Restaurant restaurant) {
        MapSingleRestaurantFragment fragment = new MapSingleRestaurantFragment();
        fragment.setArguments(createArguments(restaurant));

        return fragment;
    }

    public static Bundle createArguments(Restaurant restaurant) {
        Bundle args = new Bundle();
        args.putString(KEY_PLACE_JSON, new Gson().toJson(restaurant));
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  View.inflate(getContext(), R.layout.fragment_map_single_restaurant, null);
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
    public void setActionBarTitle(String title) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void setUpMap() {
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
    }

    @Override
    public void setMapCurrentLocation(LocationService.Location location, float zoom) {
        mCameraPosition = new CameraPosition.Builder()
                .target(location.getLatLng())
                .zoom(zoom)
                .build();
    }

    @Override
    public void addMarker(Restaurant restaurant) {
        LatLng latLng = new LatLng(restaurant.getGeometry().getLocation().getLat(),
                restaurant.getGeometry().getLocation().getLng());

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(restaurant.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_marker_icon));

        Marker marker = mGoogleMap.addMarker(markerOptions);
        mMarkerSet.put(marker.getId(), false);
        mRestaurantSet.put(marker.getId(), restaurant);

        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        mGoogleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getContext(), mMarkerSet,
                mRestaurantSet, mResourceUtil.getGoogleApiKey()));

        mGoogleMap.addMarker(markerOptions);
        marker.showInfoWindow();
    }

    @Override
    public void addPolyLines(List<LatLng> points) {
        PolylineOptions options = new PolylineOptions()
                .width(12)
                .startCap(new RoundCap())
                .color(android.R.color.holo_blue_dark)
                .visible(true)
                .jointType(JointType.ROUND)
                .geodesic(true)
                .zIndex(30)
                .addAll(points);

        mGoogleMap.addPolyline(options);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        Restaurant restaurant = new Gson().fromJson(getArguments().getString(KEY_PLACE_JSON),
                Restaurant.class);

        mPresenter.onMapReady(restaurant);
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

    @BindView(R.id.fragment_map_single_restaurant_map_view) MapView mMapView;
}