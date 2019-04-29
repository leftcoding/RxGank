package com.left.gank.ui.welfare;

import android.content.Intent;
import android.ly.business.domain.Gank;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.left.gank.R;
import com.left.gank.config.MeiziArrayList;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.gallery.GalleryActivity;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.MultipleStatusView;
import com.left.gank.widget.recyclerview.OnFlexibleScrollListener;

import java.util.List;

import butterknife.BindView;

/**
 * 干货 - 妹子
 * Create by LingYan on 2016-5-12
 */
public class WelfareFragment extends LazyFragment implements WelfareContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private WelfareAdapter welfareAdapter;
    private WelfareContract.Presenter presenter;

    private int curPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_welfare;
    }

    public static WelfareFragment newInstance() {
        WelfareFragment fragment = new WelfareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        welfareAdapter = new WelfareAdapter(getContext());
        welfareAdapter.setListener(itemClickListener);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        OnFlexibleScrollListener scrollListener = new OnFlexibleScrollListener();
        scrollListener.setOnScrollListener(this.scrollListener);
        recyclerView.addOnScrollListener(scrollListener);
        recyclerView.setAdapter(welfareAdapter);

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        multipleStatusView.setListener(onMultipleClick);
    }

    @Override
    public void onLazyActivityCreate() {
        presenter = new WelfarePresenter(getContext(), this);
        presenter.loadWelfare(curPage);
    }

    private final OnFlexibleScrollListener.ScrollListener scrollListener = new OnFlexibleScrollListener.ScrollListener() {
        @Override
        public void onLoadMore() {
            loadWelfare();
        }
    };

    private final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            curPage = 1;
            loadWelfare();
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
//            startActivity(intent);

            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, getString(R.string.transition_welfare_image));
            startActivity(intent, activityOptionsCompat.toBundle());
        }
    };

    private void loadWelfare() {
        if (presenter != null) {
            presenter.loadWelfare(curPage);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
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
        if (welfareAdapter != null) {
            if (page == 1 && ListUtils.isEmpty(list)) {
                showEmpty();
                return;
            }

            showContent();
            curPage = page + 1;
            if (page == 1) {
                welfareAdapter.fillItems(list);
            } else {
                welfareAdapter.appendItems(list);
            }
            welfareAdapter.notifyDataSetChanged();
            MeiziArrayList.getInstance().addImages(list, page);
        }
    }

    @Override
    public void loadWelfareFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            presenter.destroy();
        }
    }
}
