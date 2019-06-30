package com.left.gank.ui.discovered.jiandan;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.left.gank.R;
import com.left.gank.bean.JianDanBean;
import com.left.gank.config.Constants;
import com.left.gank.listener.ItemClick;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.web.JiandanWebActivity;
import com.left.gank.utils.StyleUtils;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;
import com.left.gank.widget.MyDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 新鲜事
 * Create by LingYan on 2016-11-18
 */

public class JiandanFragment extends LazyFragment implements JiandanContract.View, ItemClick {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView mMultipleStatusView;
    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private JiandanAdapter mAdapter;
    private JiandanContract.Presenter mPresenter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    protected int fragmentLayoutId() {
        return R.layout.layout_swipe_normal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new JiandanAdapter();
        mAdapter.setListener(this);
        mSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setAdapter(mAdapter);
        mRecyclerView = mSwipeRefreshLayout.getRecyclerView();
        mSwipeRefreshLayout.getRecyclerView().setHasFixedSize(true);
        mSwipeRefreshLayout.getRecyclerView().addItemDecoration(new MyDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
//        mSwipeRefreshLayout.setColorSchemeColors(App.getAppColor(R.color.colorPrimary));
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
//        mPresenter = new JiandanPresenter(JiandanDataSource.getInstance(), this);
    }

    @Override
    public void onLazyActivityCreate() {

    }

    protected void callBackRefreshUi() {
        Resources.Theme theme = getActivity().getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(R.attr.baseAdapterItemBackground, typedValue, true);
        int background = typedValue.data;
        theme.resolveAttribute(R.attr.baseAdapterItemTextColor, typedValue, true);
        int textColor = typedValue.data;
        theme.resolveAttribute(R.attr.textSecondaryColor, typedValue, true);
        int authorColor = typedValue.data;
        theme.resolveAttribute(R.attr.themeBackground, typedValue, true);
        int mainColor = typedValue.data;
        mRecyclerView.setBackgroundColor(mainColor);
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);

        int childCount = mRecyclerView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
            View view = childView.findViewById(R.id.jiandan_ll);
            view.setBackgroundColor(background);
            TextView title = (TextView) childView.findViewById(R.id.jiandan_txt_title);
            TextView author = (TextView) childView.findViewById(R.id.jiandan_txt_author);
            title.setTextColor(textColor);
            author.setTextColor(authorColor);
        }

        StyleUtils.clearRecyclerViewItem(mRecyclerView);
        StyleUtils.changeSwipeRefreshLayout(mSwipeRefreshLayout);
    }

    @Override
    public void refillData(List<JianDanBean> list) {
        mAdapter.updateItem(list);
    }

    @Override
    public void appendMoreDate(List<JianDanBean> list) {
        mAdapter.appendItem(list);
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
        JianDanBean bean = (JianDanBean) object;
        Bundle bundle = new Bundle();
        bundle.putString(JiandanWebActivity.TITLE, bean.getTitle());
        bundle.putString(JiandanWebActivity.URL, bean.getUrl());
        bundle.putString(JiandanWebActivity.TYPE, Constants.JIANDAN);
        bundle.putString(JiandanWebActivity.AUTHOR, bean.getType());
        JiandanWebActivity.startWebActivity(getActivity(), bundle);
    }

    @Override
    public void shortToast(String string) {

    }
}
