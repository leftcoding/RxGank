package com.left.gank.ui.discovered.video;

import android.business.domain.Gank;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.gank.R;
import com.left.gank.listener.MeiziOnClick;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.web.WebVideoViewActivity;
import com.left.gank.utils.StyleUtils;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * 休息视频
 * Create by LingYan on 2016-04-25
 */
public class VideoFragment extends LazyFragment implements MeiziOnClick, SwipeRefreshLayout.OnRefreshListener,
        VideoContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VideoContract.Presenter mPresenter;
    private VideoAdapter mAdapter;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setRefreshing(true);
        //        setSwipeRefreshLayout(mSwipeRefreshLayout);

        mMultipleStatusView.setListener(v -> onLoading());

        mAdapter = new VideoAdapter(getActivity());
        mAdapter.setOnItemClickListener(this);
        mSwipeRefreshLayout.setAdapter(mAdapter);

        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
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
//        mPresenter = new VideoPresenter(GankDataSource.getInstance(), this);
    }

    @Override
    public void onLazyActivityCreate() {

    }

    private void onLoading() {
//        mPresenter.fetchNew();
    }

    protected void callBackRefreshUi() {
        Resources.Theme theme = getActivity().getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.resourceId;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.goods_rl_title);
            TextView title = (TextView) childView.findViewById(R.id.title);
            view.setBackgroundResource(background);
            title.setTextColor(textColor);
        }
        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void onClick(View view, int position) {
        Bundle bundle = new Bundle();
        List<Gank> list = mAdapter.getResults();
        bundle.putString(WebVideoViewActivity.TITLE, list.get(position).desc);
        bundle.putString(WebVideoViewActivity.URL, list.get(position).url);
        WebVideoViewActivity.startWebActivity(getActivity(), bundle);
    }

    @Override
    public void onRefresh() {
        onLoading();
    }

    @Override
    public void refillData(List<Gank> list) {
        mAdapter.refillItems(list);
    }

    @Override
    public void appendData(List<Gank> list) {
        mAdapter.appendItems(list);
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
}
