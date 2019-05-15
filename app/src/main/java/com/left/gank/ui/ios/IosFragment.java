package com.left.gank.ui.ios;

import android.content.Context;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.config.Constants;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.ios.text.ItemCallback;
import com.left.gank.ui.web.normal.WebActivity;
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
 * ios
 * Create by LingYan on 2016-4-26
 */
public class IosFragment extends LazyFragment implements IosContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private IosAdapter iosAdapter;
    private IosContract.Presenter iosPresenter;
    private PageConfig pageConfig = new PageConfig();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iosAdapter = new IosAdapter(getContext());
        iosAdapter.setOnItemClickListener(onItemClickListener);

        OnFlexibleScrollListener onFlexibleScrollListener = new OnFlexibleScrollListener(swipeRefreshLayout);
        onFlexibleScrollListener.setOnScrollListener(scrollListener);
        recyclerView.addOnScrollListener(onFlexibleScrollListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(iosAdapter);
        multipleStatusView.setListener(multipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        iosPresenter = new IosPresenter(getContext(), this);
        loadIos(true, PageConfig.starPage());
    }

    private final MultipleStatusView.OnMultipleClick multipleClick = new MultipleStatusView.OnMultipleClick() {
        @Override
        public void retry(View v) {
            loadIos(false, PageConfig.starPage());
        }
    };

    private final OnFlexibleScrollListener.ScrollListener scrollListener = new OnFlexibleScrollListener.ScrollListener() {
        @Override
        public void onRefresh() {
            loadIos(true, PageConfig.starPage());
        }

        @Override
        public void onLoadMore() {
            if (pageConfig != null) {
                loadIos(false, pageConfig.getNextPage());
            }
        }
    };

    private final ItemCallback onItemClickListener = (view, gank) -> {
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, gank.desc);
        bundle.putString(WebActivity.URL, gank.url);
        bundle.putString(WebActivity.AUTHOR, gank.who);
        bundle.putString(WebActivity.TYPE, Constants.IOS);
        WebActivity.startWebActivity(getContext(), bundle);
    };

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            if (multipleStatusView != null) {
                multipleStatusView.showLoading();
            }
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

    private void loadIos(boolean useProgress, int page) {
        if (iosPresenter != null) {
            iosPresenter.loadIos(true, useProgress, page);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (iosPresenter != null) {
            iosPresenter.destroy();
        }
        if (iosAdapter != null) {
            iosAdapter.destroy();
        }
    }

    @Override
    public void loadIosSuccess(int page, List<Gank> list) {
        showContent();

        if (list == null || list.isEmpty()) {
            showEmpty();
            return;
        }
        if (pageConfig != null) {
            pageConfig.setCurPage(page);
        }
        if (iosAdapter != null) {
            if (PageConfig.isFirstPage(page)) {
                iosAdapter.fillItems(list);
            } else {
                iosAdapter.appendItems(list);
            }
            iosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadIosFailure(int page, String msg) {
        if (!PageConfig.isFirstPage(page)) {
            if (iosAdapter != null) {
                iosAdapter.showError();
                iosAdapter.notifyDataSetChanged();
            }
        } else {
            if (multipleStatusView != null) {
                multipleStatusView.showError();
            }
        }
    }

    public static IosFragment newInstance() {
        IosFragment fragment = new IosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
