package com.left.gank.ui.baisi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.rxbus.RxEventBus;
import android.view.View;

import com.left.gank.R;
import com.left.gank.domain.BuDeJieVideo;
import com.left.gank.domain.GallerySize;
import com.left.gank.mvp.source.remote.BuDeJieDataSource;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;
import com.left.gank.widget.SpaceItemDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-30
 */

public class BaiSiVideoFragment extends LazyFragment implements BaiSiVideoContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;

    private BaiSiVideoAdapter mBaiSiVideoAdapter;
    private BaiSiVideoContract.Presenter mPresenter;
    private BaiSiActivity mActivity;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaiSiActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new BaiSiVideoPresenter(BuDeJieDataSource.getInstance(), this);
        mPresenter.fetchNew();
    }

    @Override
    public void onLazyActivityCreate() {

    }

    private void initViews() {
        mBaiSiVideoAdapter = new BaiSiVideoAdapter(mActivity);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setAdapter(mBaiSiVideoAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.recycler_view_space);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new SpaceItemDecoration(spacingInPixels));

        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnListener() {
            @Override
            public void onLoadMore() {
                showProgress();
                mPresenter.fetchMore();
            }

            @Override
            public void onRefresh() {
                mPresenter.fetchNew();
            }
        });

        mBaiSiVideoAdapter.setPlayClick((position, image, url, height, width, title, shareUrl) -> {
            int[] location = new int[2];
            image.getLocationInWindow(location);
            startActivity(new GallerySize(height, width, url, location[1], title, shareUrl));
        });

    }

    private void startActivity(GallerySize gs) {
        RxEventBus.newInstance().postSticky(gs);
        Intent intent = new Intent(mActivity, BaiSiVideoPreViewActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(0, 0);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void refillData(List<BuDeJieVideo.ListBean> list) {
        mBaiSiVideoAdapter.updateItems(list);
    }

    @Override
    public void appendData(List<BuDeJieVideo.ListBean> list) {
        mBaiSiVideoAdapter.addItems(list);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void shortToast(String string) {

    }
}
