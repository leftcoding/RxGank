package com.left.gank.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.base.fragment.SupportFragment;
import com.left.gank.data.entity.ReadHistory;
import com.left.gank.listener.ItemClick;
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
import butterknife.BindView;

/**
 * Create by LingYan on 2016-10-31
 */

public class BrowseHistoryFragment extends SupportFragment implements BrowseHistoryContract.View {
    @BindView(R.id.browse_coordinator)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private BrowseHistoryContract.Presenter presenter;
    private BrowseHistoryAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(R.string.mine_browse);
        toolbar.setNavigationOnClickListener(v -> getActivity().finish());

        swipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setILyRecycler(new LyRecyclerView.ILyRecycler() {
            @Override
            public void removeRecycle(int pos) {
                long id = adapter.getReadHistory(pos).getId();
                presenter.deleteHistory(id);
                adapter.removeRecycle(pos);
                if (adapter.getItemCount() == 0) {
                    showEmpty();
                }
            }

            @Override
            public void onClick(int pos) {
                ReadHistory readHistory = adapter.getReadHistory(pos);
                openWebActivity(readHistory);
            }
        });

        adapter = new BrowseHistoryAdapter();
        adapter.setOnItemClick(itemClick);
        swipeRefreshLayout.setAdapter(adapter);
        swipeRefreshLayout.setOnScrollListener(onListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_browse;
    }

    private final LySwipeRefreshLayout.OnListener onListener = new LySwipeRefreshLayout.OnListener() {
        @Override
        public void onRefresh() {
        }

        @Override
        public void onLoadMore() {
        }
    };

    private final ItemClick itemClick = (position, object) -> openWebActivity((ReadHistory) object);

    @Override
    public void refillData(List<ReadHistory> history) {
        adapter.updateList(history);
    }

    @Override
    public void appendData(List<ReadHistory> history) {
        adapter.appendList(history);
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showContent() {
        multipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {
        multipleStatusView.showEmpty();
    }

    private void openWebActivity(ReadHistory readHistory) {
        Intent intent = new Intent(getContext(), WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WebActivity.TITLE, readHistory.getComment());
        bundle.putString(WebActivity.URL, readHistory.getUrl());
        bundle.putString(WebActivity.TYPE, readHistory.getG_type());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
