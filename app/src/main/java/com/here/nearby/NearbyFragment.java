package com.here.nearby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.here.R;
import com.here.adapter.NearAdapter;
import com.here.base.MvpFragment;
import com.here.bean.ImActivity;
import com.here.bean.Kind;
import com.here.going.GoingActivity;
import com.here.imdetails.ImDetailsActivity;
import com.here.immediate.NewImmediateActivity;
import com.here.util.BitmapUtil;
import com.here.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyc on 2017/6/21 14:50
 */

public class NearbyFragment extends MvpFragment<NearbyPresenter> implements NearbyContract, AMap.OnMarkerClickListener {


    @Bind(R.id.map_nearby)
    TextureMapView mapNearby;
    @Bind(R.id.iv_refresh_nearby)
    ImageView ivRefreshNearby;
    @Bind(R.id.fb_add_activity)
    FloatingActionButton fbAddActivity;
    @Bind(R.id.cv_map_head)
    CircleImageView cvMapHead;
    @Bind(R.id.tv_publisher_name)
    TextView tvPublisherName;
    @Bind(R.id.tv_activity_address)
    TextView tvActivityAddress;
    @Bind(R.id.tv_join_number)
    TextView tvJoinNumber;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_over_time)
    TextView tvOverTime;
    @Bind(R.id.tv_activity_name)
    TextView tvActivityName;
    @Bind(R.id.tv_activity_describe)
    TextView tvActivityDescribe;
    @Bind(R.id.v_down)
    View vDown;
    @Bind(R.id.rl_im_activity)
    RelativeLayout rlImActivity;
    @Bind(R.id.rl_view_down)
    RelativeLayout rlViewDown;
    @Bind(R.id.rcv_near)
    RecyclerView rcvNear;
    private UiSettings mUiSettings;

    private AMap aMap;

    List<Marker> markers;

    private boolean isGoing = false;

    private Marker lastClickMarker;

    private boolean isTouchMap = true;

    private MyLocationStyle myLocationStyle;

    private ImActivity selectedImActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        mvpPresenter.attachView(this);
        ButterKnife.bind(this, view);
        mapNearby.onCreate(savedInstanceState);
        initView();
        return view;
    }

    private void initView() {
        rcvNear.setLayoutManager(new GridLayoutManager(getActivity(),2));
        List<Kind> list = new ArrayList<>();
        list.add(new Kind(R.drawable.yd,"运动"));
        list.add(new Kind(R.drawable.gw,"购物"));
        list.add(new Kind(R.drawable.cg,"唱歌"));
        list.add(new Kind(R.drawable.dy,"电影"));
        list.add(new Kind(R.drawable.hw,"户外"));
        list.add(new Kind(R.drawable.jb,"酒吧"));
        list.add(new Kind(R.drawable.mr,"美容"));
        list.add(new Kind(R.drawable.ms,"美食"));
        list.add(new Kind(R.drawable.xp,"棋牌"));
        list.add(new Kind(R.drawable.yx,"游戏"));
        list.add(new Kind(R.drawable.zy,"桌游"));
        list.add(new Kind(R.drawable.qt,"其他"));
        final NearAdapter adapter = new NearAdapter(list);
        rcvNear.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                rcvNear.removeAllViews();
                rcvNear.setVisibility(View.GONE);
                mvpPresenter.setKind(adapter.getData().get(i).getName());
                aMap = mapNearby.getMap();
                mUiSettings = aMap.getUiSettings();
                mUiSettings.setZoomControlsEnabled(false);
                mvpPresenter.checkMyPublisher();
                initMap();
            }
        });
    }

    private void initMap() {
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        myLocationStyle.strokeColor(Color.argb(180, 3, 145, 255));
        myLocationStyle.strokeWidth(5);
        myLocationStyle.radiusFillColor(Color.argb(10, 0, 0, 180));
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMyLocationChangeListener(mvpPresenter);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (motionEvent.getY() < rlImActivity.getY()) {
                        isTouchMap = false;
                    } else {
                        isTouchMap = true;
                    }
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE && !isTouchMap) {
                    if (rlImActivity.getVisibility() == View.VISIBLE) {
                        downTheDetail();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.checkMyPublisher();
        mapNearby.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapNearby.onPause();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapNearby != null) {
            mapNearby.onSaveInstanceState(outState);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapNearby != null) {
            mapNearby.onDestroy();
        }

    }

    @Override
    protected NearbyPresenter createPresenter() {
        return new NearbyPresenter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv_refresh_nearby, R.id.fb_add_activity, R.id.cv_map_head, R.id.v_down, R.id.rl_view_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh_nearby:
                mvpPresenter.checkMyPublisher();
                if (!mvpPresenter.isLoading()) {
                    mvpPresenter.queryNearByImActivity();
                }
                break;
            case R.id.fb_add_activity:
                if (isGoing) {
                    startActivity(new Intent(mActivity, GoingActivity.class));
                    mActivity.overridePendingTransition(R.anim.push_up_in,
                            R.anim.push_up_out);
                } else {
                    startActivity(new Intent(mActivity, NewImmediateActivity.class));
                    mActivity.overridePendingTransition(R.anim.push_up_in,
                            R.anim.push_up_out);
                }
                break;
            case R.id.cv_map_head:
                Intent intent = new Intent(getActivity(), ImDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("imActivity", selectedImActivity);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_view_down:
                downTheDetail();
                break;
        }
    }

    @Override
    public void isGoing(boolean going) {
        isGoing = going;
        fbAddActivity.setImageResource(isGoing ? R.drawable.going : R.drawable.add);
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void stopLoading() {
        dissmiss();
    }

    @Override
    public void loadingSuccess(final List<ImActivity> imActivities) {
        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
        }
        markers = new ArrayList<>();
        for (int i = 0; i < imActivities.size(); i++) {
            if (!TextUtils.isEmpty(imActivities.get(i).getPublisher().getHeadImageUrl())) {
                BitmapUtil.drawMark(imActivities.get(i), 144, true, new BitmapUtil.OnGetMapHeadListener() {
                    @Override
                    public void success(Bitmap bitmap, ImActivity imActivity) {
                        markers.add(aMap.addMarker(new MarkerOptions().position(
                                Constants.ZHENGZHOU).icon(
                                BitmapDescriptorFactory.fromBitmap(bitmap))));
                        markers.get(markers.size() - 1).setPosition(new LatLng(imActivities.get(markers.size() - 1)
                                .getLatitude(), imActivities.get(markers.size() - 1).getLongitude()));
                        startGrowAnimation(markers.get(markers.size() - 1));
                        markers.get(markers.size() - 1).setObject(imActivity);
                        if (markers.size() == imActivities.size()) {
                            mvpPresenter.loadingComplete();
                        }

                    }

                    @Override
                    public void fail(ImActivity imActivity) {
                        markers.add(aMap.addMarker(new MarkerOptions().position(
                                Constants.ZHENGZHOU).icon(
                                BitmapDescriptorFactory.fromBitmap(BitmapUtil.drawMark(BitmapFactory
                                        .decodeResource(getActivity().getResources(), R.drawable.grils), 144, true)))));
                        markers.get(markers.size() - 1).setPosition(new LatLng(imActivities.get(markers.size() - 1)
                                .getLatitude(), imActivities.get(markers.size() - 1).getLongitude()));
                        startGrowAnimation(markers.get(markers.size() - 1));
                        markers.get(markers.size() - 1).setObject(imActivity);
                        if (markers.size() == imActivities.size()) {
                            mvpPresenter.loadingComplete();
                        }
                    }
                });
            } else {
                markers.add(aMap.addMarker(new MarkerOptions().position(
                        Constants.ZHENGZHOU).icon(
                        BitmapDescriptorFactory.fromBitmap(BitmapUtil.drawMark(BitmapFactory.decodeResource(getActivity()
                                .getResources(), R.drawable.grils), 144, true)))));
                markers.get(markers.size() - 1).setPosition(new LatLng(imActivities.get(markers.size() - 1).getLatitude(),
                        imActivities.get(markers.size() - 1).getLongitude()));
                startGrowAnimation(markers.get(markers.size() - 1));
                markers.get(markers.size() - 1).setObject(imActivities.get(i));
                if (markers.size() == imActivities.size()) {
                    mvpPresenter.loadingComplete();
                }
            }
        }
    }

    @Override
    public void loadingFail(String error) {
        toastShow(error);
    }

    @Override
    public void downTheDetail() {
        rlImActivity.setVisibility(View.GONE);
        android.view.animation.Animation operatingAnim = AnimationUtils
                .loadAnimation(getActivity(), R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        rlImActivity.startAnimation(operatingAnim);

    }

    @Override
    public void upTheDetail() {
        android.view.animation.Animation operatingAnim = AnimationUtils
                .loadAnimation(getActivity(), R.anim.map_tip_in);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        rlImActivity.startAnimation(operatingAnim);
        rlImActivity.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadActivityDetail(ImActivity imActivity) {
        if (rlImActivity.getVisibility() == View.GONE) {
            upTheDetail();
        }
        if (!TextUtils.isEmpty(imActivity.getPublisher().getHeadImageUrl())) {
            Glide.with(getActivity())
                    .load(imActivity.getPublisher().getHeadImageUrl())
                    .into(cvMapHead);
        } else {
            Glide.with(getActivity())
                    .load(R.drawable.grils)
                    .into(cvMapHead);
        }

        if (!TextUtils.isEmpty(imActivity.getPublisher().getNickname())) {
            tvPublisherName.setText(imActivity.getPublisher().getNickname());
        } else {
            tvPublisherName.setText("木头人");
        }

        tvActivityAddress.setText(imActivity.getLocation());
        tvJoinNumber.setText(0 + "/" + imActivity.getNumber());
        tvDistance.setText((int) AMapUtils.calculateLineDistance(mvpPresenter.getMyLatLng()
                , new LatLng(imActivity.getLatitude(), imActivity.getLongitude())) + "米");
        tvOverTime.setText("结束时间：" + imActivity.getOverTime());
        tvActivityName.setText("活动名称：" + imActivity.getTitle());
        tvActivityDescribe.setText("活动简介：" + imActivity.getDescribe());
        selectedImActivity = imActivity;


    }


    @Override
    public void cancelLocation() {
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
        aMap.reloadMap();
    }


    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(Marker growMarker) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (!(marker.getObject() instanceof ImActivity)) {
            return true;
        }
        if (lastClickMarker != null && ((ImActivity) marker.getObject()).getObjectId()
                .equals(((ImActivity) lastClickMarker.getObject()).getObjectId()) &&
                rlImActivity.getVisibility() == View.VISIBLE) {
            return true;
        }
        lastClickMarker = marker;
        loadActivityDetail((ImActivity) marker.getObject());
        return true;
    }

}
