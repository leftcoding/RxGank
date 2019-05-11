package com.left.gank.ui.welfare;

import android.content.Intent;
import android.ly.business.domain.Gank;
import android.ly.business.domain.PageConfig;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.gallery.GalleryActivity;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.MultipleStatusView;
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * 干货 - 妹子
 * Create by LingYan on 2016-5-12
 */
public class WelfareFragment extends LazyFragment implements WelfareContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private WelfareAdapter welfareAdapter;
    private WelfareContract.Presenter presenter;
    private PageConfig pageConfig = new PageConfig();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welfareAdapter = new WelfareAdapter(getContext());
        welfareAdapter.setListener(itemClickListener);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        OnFlexibleScrollListener scrollListener = new OnFlexibleScrollListener(swipeRefresh);
        scrollListener.setOnScrollListener(this.scrollListener);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(welfareAdapter);

        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        presenter = new WelfarePresenter(getContext(), this);
        loadWelfare(true, PageConfig.starPage());
    }

    private final OnFlexibleScrollListener.ScrollListener scrollListener = new OnFlexibleScrollListener.ScrollListener() {
        @Override
        public void onRefresh() {
            loadWelfare(true, PageConfig.starPage());
        }

        @Override
        public void onLoadMore() {
            if (pageConfig != null) {
                loadWelfare(false, pageConfig.getNextPage());
            }
        }
    };

    private final MultipleStatusView.OnMultipleClick onMultipleClick = new MultipleStatusView.OnMultipleClick() {
        @Override
        public void retry(View v) {
            multipleStatusView.showLoading();
        }
    };

    private final WelfareAdapter.ItemClickListener itemClickListener = new WelfareAdapter.ItemClickListener() {
        @Override
        public void onItem(View view, int position) {
            Intent intent = new Intent(getContext(), GalleryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(GalleryActivity.EXTRA_POSITION, position);
            bundle.putInt(GalleryActivity.TYPE, GalleryActivity.TYPE_INDEX);
            intent.putExtras(bundle);

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.transition_welfare_image));
            startActivity(intent, activityOptionsCompat.toBundle());
        }
    };

    private void loadWelfare(boolean useProgress, int page) {
        if (presenter != null) {
            presenter.loadWelfare(true, useProgress, page);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
    }

    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void loadWelfareSuccess(int page, List<Gank> list) {
        final boolean isFirst = PageConfig.isFirstPage(page);
        if (isFirst && ListUtils.isEmpty(list)) {
            showEmpty();
            return;
        }

        showContent();
        if (pageConfig != null) {
            pageConfig.setCurPage(page);
        }
        if (welfareAdapter != null) {
            if (isFirst) {
                welfareAdapter.fillItems(list);
            } else {
                welfareAdapter.appendItems(list);
            }
            welfareAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadWelfareFailure(int page, String msg) {
        shortToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.destroy();
        }
    }

    public static WelfareFragment newInstance() {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
