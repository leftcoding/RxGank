package com.left.gank.ui.discovered.teamBlog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.base.LazyFragment;
import com.left.gank.config.Constants;
import com.left.gank.domain.JianDanBean;
import com.left.gank.listener.ItemClick;
import com.left.gank.ui.discovered.technology.TechnologyContract;
import com.left.gank.ui.web.JiandanWebActivity;
import com.left.gank.ui.web.normal.WebActivity;
import com.left.gank.utils.theme.RecyclerViewColor;
import com.left.gank.utils.theme.ThemeColor;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-11-23
 */

public class TeamBlogFragment extends LazyFragment implements TechnologyContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private TeamBlogAdapter mAdapter;

    private TechnologyContract.Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mPresenter = new TeamBlogPresenter(TeamBlogDataSource.getInstance(), this);
    }

    @Override
    public void onLazyActivityCreate() {

    }

    @Override
    protected int fragmentLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    protected void initValues() {
        mAdapter = new TeamBlogAdapter();
        mAdapter.setListener(this);
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
    public void refillData(List<JianDanBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendData(List<JianDanBean> list) {
        mAdapter.appendItem(list);
    }

    @Override
    public void onClick(int position, Object object) {
        JianDanBean bean = (JianDanBean) object;
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, bean.getTitle());
        bundle.putString(WebActivity.URL, bean.getUrl());
        bundle.putString(WebActivity.TYPE, Constants.TECHNOLOGY);
        bundle.putString(WebActivity.AUTHOR, "");
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtras(bundle);
        JiandanWebActivity.Companion.startWebActivity(getActivity(), bundle);
    }

    protected void callBackRefreshUi() {
        RecyclerViewColor recyclerViewColor = new RecyclerViewColor(mRecyclerView);
        recyclerViewColor.setItemBackgroundColor(R.id.team_ll_body, R.attr.lyItemSelectBackground);
        recyclerViewColor.setItemColor(R.id.team_blog_txt_title, R.attr.baseAdapterItemTextColor);

        ThemeColor themeColor = new ThemeColor(this);
        themeColor.setBackgroundResource(R.attr.themeBackground, mRecyclerView);
        themeColor.swipeRefresh(mSwipeRefreshLayout);
        themeColor.recyclerViewColor(recyclerViewColor);
        themeColor.start();
    }

    @Override
    public void shortToast(String string) {

    }
}
