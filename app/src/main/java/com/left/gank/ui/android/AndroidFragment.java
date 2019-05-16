package com.left.gank.ui.android;

import android.content.Context;
import android.content.Intent;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.butterknife.adapter.FootAdapter;
import com.left.gank.config.Constants;
import com.left.gank.ui.MainActivity;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.web.normal.WebActivity;
import com.left.gank.utils.CircularAnimUtils;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.MultipleStatusView;
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * Create by LingYan on 2016-4-26
 */
public class AndroidFragment extends LazyFragment implements AndroidContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context context;
    private AndroidAdapter androidAdapter;
    private AndroidContract.Presenter androidPresenter;

    private PageConfig pageConfig = new PageConfig();

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_android;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        androidAdapter = new AndroidAdapter(context);
        androidAdapter.setErrorListener(errorListener);
        androidAdapter.setCallback(callback);

        recyclerView.setAdapter(androidAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        OnFlexibleScrollListener onFlexibleScrollListener = new OnFlexibleScrollListener(swipeRefreshLayout);
        onFlexibleScrollListener.setOnScrollListener(scrollListener);
        recyclerView.addOnScrollListener(onFlexibleScrollListener);

        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        androidPresenter = new AndroidPresenter(context, this);
        loadAndroid(true, PageConfig.starPage());
    }

    private final FootAdapter.ErrorListener errorListener = () -> {
        if (pageConfig != null) {
            loadAndroid(false, pageConfig.getNextPage());
        }
    };

    private final OnFlexibleScrollListener.ScrollListener scrollListener = new OnFlexibleScrollListener.ScrollListener() {
        @Override
        public void onRefresh() {
            loadAndroid(false, PageConfig.starPage());
        }

        @Override
        public void onLoadMore() {
            if (pageConfig != null) {
                loadAndroid(false, pageConfig.getNextPage());
            }
        }
    };

    private final AndroidAdapter.Callback callback = new AndroidAdapter.Callback() {
        @Override
        public void onItemClick(View view, Gank gank) {
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.TITLE, gank.desc);
            bundle.putString(WebActivity.URL, gank.url);
            bundle.putString(WebActivity.TYPE, Constants.ANDROID);
            bundle.putString(WebActivity.AUTHOR, gank.who);
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtras(bundle);
            CircularAnimUtils.startActivity((MainActivity) context, intent, view, R.color.white_half);
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = v -> {
        showLoading();
        loadAndroid(true, PageConfig.starPage());
    };

    private void loadAndroid(boolean useProgress, int page) {
        if (androidPresenter != null) {
            androidPresenter.loadAndroid(true, useProgress, page);
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            showLoading();
            return;
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    private void showLoading() {
        if (multipleStatusView != null) {
            multipleStatusView.showLoading();
        }
    }

    public static AndroidFragment newInstance() {
        AndroidFragment fragment = new AndroidFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loadAndroidSuccess(int page, List<Gank> list) {
        showContent();
        final boolean isFirst = PageConfig.isFirstPage(page);
        if (ListUtils.isEmpty(list)) {
            if (isFirst) {
                showEmpty();
                return;
            } else {
                if (androidAdapter != null) {
                    androidAdapter.setEnd(true);
                }
            }
        }
        if (pageConfig != null) {
            pageConfig.setCurPage(page);
        }
        if (androidAdapter != null) {
            if (isFirst) {
                androidAdapter.fillItems(list);
            } else {
                androidAdapter.appendItems(list);
            }
            androidAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadAndroidFailure(int page, String msg) {
        shortToast(msg);
        final boolean isFirst = PageConfig.isFirstPage(page);
        if (!isFirst) {
            if (androidAdapter != null) {
                androidAdapter.showError();
                androidAdapter.notifyDataSetChanged();
            }
        } else {
            if (multipleStatusView != null) {
                multipleStatusView.showEmpty();
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (androidAdapter != null) {
            androidAdapter.destroy();
        }

        if (androidPresenter != null) {
            androidPresenter.destroy();
        }
        super.onDestroyView();
    }
}
