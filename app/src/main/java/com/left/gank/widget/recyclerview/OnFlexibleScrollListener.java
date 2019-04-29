package com.left.gank.widget.recyclerview;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class OnFlexibleScrollListener extends RecyclerView.OnScrollListener {
    private int lastPosition = RecyclerView.NO_POSITION;
    private ScrollListener scrollListener;

    public OnFlexibleScrollListener() {
        super();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                final int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastPosition != lastVisibleItemPosition && layoutManager.getItemCount() <= lastVisibleItemPosition + 1) {
                    lastPosition = lastVisibleItemPosition;
                    onLoadMore();
                }
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager =
                        (StaggeredGridLayoutManager) layoutManager;
                int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(positions);
                for (int position : positions) {
                    if (position == staggeredGridLayoutManager.getItemCount() - 1) {
                        onLoadMore();
                    }
                }
            }
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == 0) {
                    onLoadMore();
                }
            }
        }
    }

    private void onLoadMore() {
        if (scrollListener != null) {
            scrollListener.onLoadMore();
        }
    }

    public void setOnScrollListener(ScrollListener listener) {
        this.scrollListener = listener;
    }

    /**
     * 加载更多的监听
     */
    public interface ScrollListener {
        void onLoadMore();
    }
}
