package com.left.gank.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * GridView ItemDecoration 设置间距
 */
public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "GridDividerItemDeco";

    private Context mContext;

    private int mFirstAndLastColumnW;   //您所需指定的间隔宽度，主要为第一列和最后一列与父控件的间隔；行间距，列间距将动态分配
    private int mFirstRowTopMargin; //第一行顶部是否需要间隔

    private int mLastRowBottomMargin;

    private int mSpanCount = 0;
    private int mScreenW = 0;
    private int mItemDistance;


    /**
     * @param firstAndLastColumnW 间隔宽度
     * @param firstRowTopMargin   第一行顶部是否需要间隔
     */
    public GridDividerItemDecoration(Context context, int firstAndLastColumnW, int firstRowTopMargin, int lastRowBottomMargin) {
        mContext = context;
        mFirstAndLastColumnW = firstAndLastColumnW;
        mFirstRowTopMargin = firstRowTopMargin;
        mLastRowBottomMargin = lastRowBottomMargin;
    }


    public void setFirstRowTopMargin(int firstRowTopMargin) {
        mFirstRowTopMargin = firstRowTopMargin;
    }

    public void setLastRowBottomMargin(int lastRowBottomMargin) {
        mLastRowBottomMargin = lastRowBottomMargin;
    }

    public void setItemDistance(int itemDistance) {
        mItemDistance = itemDistance;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int top = 0;
        int left;
        int right;
        int bottom;

        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        mSpanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        // 屏幕宽度-View的宽度*spanCount 得到屏幕剩余空间
        int maxDividerWidth = getMaxDividerWidth(view);
        int spaceWidth = mFirstAndLastColumnW;//首尾两列与父布局之间的间隔
        // 除去首尾两列，item与item之间的距离
        int eachItemWidth = maxDividerWidth / mSpanCount;
        int dividerItemWidth = (maxDividerWidth - 2 * spaceWidth) / (mSpanCount - 1);//item与item之间的距离

        left = itemPosition % mSpanCount * (dividerItemWidth - eachItemWidth) + spaceWidth;

        right = eachItemWidth - left;
        bottom = dividerItemWidth;

        // 首行
        if (mFirstRowTopMargin > 0 && isFirstRow(parent, itemPosition, mSpanCount, childCount)) {
            top = mFirstRowTopMargin;
        }

        //最后一行
        if (isLastRow(parent, itemPosition, mSpanCount, childCount)) {
            if (mLastRowBottomMargin < 0) {
                bottom = 0;
            } else {
                bottom = mLastRowBottomMargin;
            }
        }

        Log.i(TAG, "getItemOffsets: dividerItemWidth =" + dividerItemWidth + "eachItemWidth = " + eachItemWidth);

        Log.i(TAG, "getItemOffsets: itemPosition =" + itemPosition + " left = " + left + " top = " + top
                + " right = " + right + " bottom = " + bottom);

        outRect.set(left, top, right, bottom);
    }

    /**
     * 获取Item View的大小，若无则自动分配空间
     * 并根据 屏幕宽度-View的宽度*spanCount 得到屏幕剩余空间
     */
    private int getMaxDividerWidth(View view) {
        int itemWidth = view.getLayoutParams().width;
        int itemHeight = view.getLayoutParams().height;
        Log.i(TAG, "getMaxDividerWidth: itemWidth =" + itemWidth);

        int screenWidth = getScreenWidth();

        int maxDividerWidth = screenWidth - itemWidth * mSpanCount;

        if (itemHeight < 0 || itemWidth < 0 || maxDividerWidth <= (mSpanCount - 1) * mFirstAndLastColumnW) {
            view.getLayoutParams().width = getAttachColumnWidth();
            view.getLayoutParams().height = getAttachColumnWidth();
            maxDividerWidth = screenWidth - view.getLayoutParams().width * mSpanCount;
        }

        return maxDividerWidth;
    }

    private int getScreenWidth() {
        Log.i(TAG, "getScreenWidth: mScreenW =" + mScreenW);
        if (mScreenW > 0) {
            return mScreenW;
        }

        mScreenW = mContext.getResources().getDisplayMetrics().widthPixels > mContext.getResources().getDisplayMetrics().heightPixels
                ? mContext.getResources().getDisplayMetrics().heightPixels : mContext.getResources().getDisplayMetrics().widthPixels;
        return mScreenW;
    }

    /**
     * 根据屏幕宽度和item数量分配 item View的width和height
     */
    private int getAttachColumnWidth() {
        int itemWidth = 0;
        int spaceWidth;
        try {
            int width = getScreenWidth();
            spaceWidth = 2 * mFirstAndLastColumnW;
            itemWidth = (width - spaceWidth) / mSpanCount - 40;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemWidth;
    }

    /**
     * 判读是否是第一列
     */
    private boolean isFirstColumn(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos % spanCount == 0) {// 第一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判断是否是最后一列
     */
    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 最后一列
                    return true;
                }
            } else {

            }
        }
        return false;
    }

    /**
     * 判读是否是最后一行
     */
    private boolean isLastRow(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int lines = childCount % spanCount == 0 ? childCount / spanCount : childCount / spanCount + 1;
            return lines == pos / spanCount + 1;
        }
        return false;
    }

    /**
     * 判断是否是第一行
     */
    private boolean isFirstRow(RecyclerView parent, int position, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return (position / spanCount + 1) == 1;
        }
        return false;
    }

    /**
     * 获取列数
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }
}