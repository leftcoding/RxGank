package com.left.gank.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.left.gank.R;
import com.left.gank.data.entity.ReadHistory;
import com.left.gank.listener.ItemClick;
import com.left.gank.ui.base.fragment.SupportFragment;
import com.left.gank.ui.more.MoreActivity;
import com.left.gank.ui.web.normal.WebActivity;
import com.left.gank.widget.LyRecyclerView;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.List;

import butterknife.BindView;

/**
 * Create by LingYan on 2016-10-31
 */

public class BrowseHistoryFragment extends SupportFragment implements BrowseHistoryContract.View, ItemClick {
    private BrowseHistoryContract.Presenter mPresenter;
    private BrowseHistoryAdapter mAdapter;

    @BindView(R.id.browse_coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private MoreActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MoreActivity) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar.setTitle(R.string.mine_browse);
        mActivity.setSupportActionBar(mToolbar);
        ActionBar barLayout = mActivity.getSupportActionBar();
        if (barLayout != null) {
            barLayout.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());

        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeRefreshLayout.setILyRecycler(new LyRecyclerView.ILyRecycler() {
            @Override
            public void removeRecycle(int pos) {
                long id = mAdapter.getReadHistory(pos).getId();
                mPresenter.deleteHistory(id);
                mAdapter.removeRecycle(pos);
                if (mAdapter.getItemCount() == 0) {
                    showEmpty();
                }
            }

            @Override
            public void onClick(int pos) {
                ReadHistory readHistory = mAdapter.getReadHistory(pos);
                openWebActivity(readHistory);
            }
        });

        mAdapter = new BrowseHistoryAdapter();
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mAdapter.setOnItemClick(this);
        mSwipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnListener() {
            @Override
            public void onRefresh() {
//                mPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
//                mPresenter.fetchMore();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mPresenter = new BrowseHistoryPresenter(LocalDataSource.getInstance(), this);
//        mPresenter.fetchNew();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_browse;
    }

    @Override
    public void refillData(List<ReadHistory> history) {
        mAdapter.updateList(history);
    }

    @Override
    public void appendData(List<ReadHistory> history) {
        mAdapter.appendList(history);
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
        mMultipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    private void showLoading() {
        if (mMultipleStatusView != null) {
            mMultipleStatusView.showLoading();
        }
    }

    @Override
    public void onClick(int position, Object object) {
        openWebActivity((ReadHistory) object);
    }

    private void openWebActivity(ReadHistory readHistory) {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, readHistory.getComment());
        bundle.putString(WebActivity.URL, readHistory.getUrl());
        bundle.putString(WebActivity.TYPE, readHistory.getG_type());
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }
}
