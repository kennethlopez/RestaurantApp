package com.restaurantapp.ui.restaurant.detail;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.restaurantapp.R;
import com.restaurantapp.data.api.response.Photo;
import com.restaurantapp.data.api.response.Restaurant;
import com.restaurantapp.data.api.response.Review;
import com.restaurantapp.ui.base.BaseActivity;
import com.restaurantapp.ui.helper.RecyclerTouchListener;
import com.restaurantapp.util.Constants;
import com.restaurantapp.util.ImageUtil;
import com.restaurantapp.util.ResourceUtil;
import com.restaurantapp.util.RxBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsActivity extends BaseActivity implements
        RestaurantDetailsContract.View, Constants.BundleKeys {

    @Inject RestaurantDetailsPresenter mPresenter;
    @Inject PhotoListAdapter mPhotoListAdapter;
    @Inject ReviewListAdapter mReviewListAdapter;
    @Inject ResourceUtil mResourceUtil;
    @Inject RxBus mBus;

    private MenuItem mFavoritesMenuItem;

    public static Bundle createExtras(Restaurant restaurant) {
        Bundle args = new Bundle();
        args.putString(KEY_PLACE_JSON, new Gson().toJson(restaurant));

        return args;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void attachView() {
        getComponent().inject(this);
        ButterKnife.bind(this);
        mPresenter.attachView(this);
        super.attachPresenter(mPresenter);

        Bundle args = getIntent().getExtras();
        String json = args.getString(KEY_PLACE_JSON);
        Restaurant restaurant = new Gson().fromJson(json, Restaurant.class);
        mPresenter.setPlaceResponse(restaurant, mResourceUtil.getGoogleApiKey());
        mPresenter.setApplicationWideBus(mBus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant_details, menu);
        mFavoritesMenuItem = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.optionsItemSelected(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(title);
    }

    @Override
    public void setName(String name) {
        mName.setText(name);
    }

    @Override
    public void setAddress(String address) {
        mAddress.setText(address);
    }

    @Override
    public void setPhoto(String url) {
        ImageUtil.setImage(this, mPhoto, url);
    }

    @Override
    public void setWebsite(String website) {
        mWebSite.setText(website);
    }

    @Override
    public void setPhotos(List<Photo> photos) {
        mPhotoListAdapter.setData(photos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayout.HORIZONTAL, false);

        mPhotosRecyclerView.setVisibility(View.VISIBLE);
        mPhotosRecyclerView.setAdapter(mPhotoListAdapter);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
        mPhotosRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mPhotosRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // TODO view photo on full screen
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void setReviews(List<Review> reviews) {
        mReviewListAdapter.setData(reviews);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mReviewsRecyclerView.setAdapter(mReviewListAdapter);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayout.VERTICAL));
        mReviewsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mReviewsRecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // TODO view full review
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void setFavoritesMenuItemTint(int colorResId) {
        ImageUtil.setTint(this, mFavoritesMenuItem, colorResId);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void showSnackBar(int messageResId) {
        Snackbar snackBar = Snackbar.make(mParentLayout, messageResId, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.undo_text, view ->
                mPresenter.optionsItemSelected(R.id.menu_restaurant_details_favorite));
        snackBar.show();
    }

    @Override
    public void showProgressBars() {
        mPhotosProgressbar.setVisibility(View.VISIBLE);
        mReviewsProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBars() {
        mPhotosProgressbar.setVisibility(View.GONE);
        mReviewsProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void hidePhotosContainer() {
        mPhotosContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideReviewsContainer() {
        mReviewsContainer.setVisibility(View.GONE);
    }

    @BindView(R.id.app_bar_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_restaurant_detail_address) AppCompatTextView mAddress;
    @BindView(R.id.activity_restaurant_details_name) AppCompatTextView mName;
    @BindView(R.id.activity_restaurant_details_photo) ImageView mPhoto;
    @BindView(R.id.activity_restaurant_details_website) AppCompatTextView mWebSite;
    @BindView(R.id.activity_restaurant_parent) LinearLayout mParentLayout;
    @BindView(R.id.activity_restaurant_details_photos_progressbar)
    ProgressBar mPhotosProgressbar;
    @BindView(R.id.activity_restaurant_details_reviews_progressbar)
    ProgressBar mReviewsProgressbar;
    @BindView(R.id.activity_restaurant_details_photos_container)
    LinearLayout mPhotosContainer;
    @BindView(R.id.activity_restaurant_details_reviews_container)
    LinearLayout mReviewsContainer;
    @BindView(R.id.activity_restaurant_details_reviews_recycler_view)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.activity_restaurant_details_photos_recycler_view)
    RecyclerView mPhotosRecyclerView;
}