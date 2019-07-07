package com.left.gank.ui.collect;

import android.os.Bundle;
import android.rxbus.RxEventBus;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.left.gank.R;
import com.left.gank.data.entity.UrlCollect;
import com.left.gank.domain.RxCollect;
import com.left.gank.mvp.source.LocalDataSource;
import com.left.gank.ui.base.fragment.SupportFragment;
import com.left.gank.ui.web.normal.WebActivity;
import com.left.gank.widget.LyRecyclerView;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 收藏
 * Create by LingYan on 2016-4-25
 */
public class CollectFragment extends SupportFragment implements CollectContract.View {
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private CollectContract.Presenter presenter;
    private CollectAdapter collectAdapter;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.activity_collcet;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(R.string.mine_my_collect);
        toolbar.setNavigationOnClickListener(v -> getActivity().finish());

        RxEventBus.newInstance().toObservable(RxCollect.class)
                .as(bindLifecycle())
                .subscribe(rxCollect -> {
                    if (rxCollect.isCollect()) {
                        onDelete();
                    }
                });

        swipeRefreshLayout.setOnScrollListener(new LySwipeRefreshLayout.OnListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.fetchNew();
                }
            }

            @Override
            public void onLoadMore() {
                if (presenter != null) {
                    presenter.fetchMore();
                }
            }
        });

        collectAdapter = new CollectAdapter(getContext());
        setRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new CollectPresenter(LocalDataSource.getInstance(), this);
        presenter.fetchNew();
    }

    private void setRecyclerView() {
        swipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setAdapter(collectAdapter);
        swipeRefreshLayout.setILyRecycler(new LyRecyclerView.ILyRecycler() {
            @Override
            public void removeRecycle(final int position) {
                final UrlCollect urlCollect = collectAdapter.getUrlCollect(position);
                long id = urlCollect.getId();
                presenter.cancelCollect(id);
                collectAdapter.deleteItem(position);
                Snackbar.make(coordinatorLayout, R.string.collect_revoke, Snackbar.LENGTH_LONG)
                        .setAction(R.string.revoke, v -> {
                            if (presenter != null) {
                                presenter.insertCollect(urlCollect);
                            }
                        })
                        .show();
                if (collectAdapter.getItemCount() == 0) {
                    showEmpty();
                }
            }

            @Override
            public void onClick(int position) {
                openWebActivity(position);
            }
        });

        recyclerView = swipeRefreshLayout.getRecyclerView();
        recyclerView.getItemAnimator().setAddDuration(150);
        recyclerView.getItemAnimator().setRemoveDuration(150);
    }

    private void openWebActivity(int position) {
        UrlCollect urlCollect = collectAdapter.getUrlCollect(position);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, urlCollect.getComment());
        bundle.putString(WebActivity.URL, urlCollect.getUrl());
        bundle.putInt(WebActivity.FROM_TYPE, WebActivity.FROM_COLLECT);
        WebActivity.Companion.startWebActivity(getContext(), bundle);
    }


    @Override
    public void setAdapterList(List<UrlCollect> list) {
        if (collectAdapter != null) {
            collectAdapter.updateItems(list);
        }
    }

    @Override
    public void appendAdapter(List<UrlCollect> list) {
        if (collectAdapter != null) {
            collectAdapter.addItems(list);
        }
    }

    @Override
    public void onDelete() {

    }

    @Override
    public int getItemsCount() {
        return collectAdapter != null ? collectAdapter.getItemCount() : 0;
    }

    @Override
    public void revokeCollect() {
    }

    @Override
    public void showEmpty() {
        if (multipleStatusView != null) {
            multipleStatusView.showEmpty();
        }
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void showProgress() {
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
}
