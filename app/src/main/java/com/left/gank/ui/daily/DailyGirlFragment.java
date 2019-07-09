package com.left.gank.ui.daily;

import android.app.ProgressDialog;
import android.business.domain.Gift;
import android.business.domain.Girl;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.left.gank.R;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.gallery.GalleryActivity;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 妹子 - 每日更新
 * Create by LingYan on 2016-07-01
 */
public class DailyGirlFragment extends LazyFragment implements DailyGirlContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    private DailyGirlAdapter dailyGirlAdapter;
    private DailyGirlPresenter dailyPresenter;

    private ProgressDialog mDialog;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyGirlAdapter = new DailyGirlAdapter();
        swipeRefresh.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        swipeRefresh.setAdapter(dailyGirlAdapter);

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnListener() {
            @Override
            public void onRefresh() {
                dailyPresenter.fetchNew();
            }

            @Override
            public void onLoadMore() {
            }
        });

        dailyGirlAdapter.setOnItemClickListener((position, object) -> {

        });
    }

    private void showLoadingDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(getContext());
        }

        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(getContext().getString(R.string.loading_meizi_images));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setOnCancelListener(dialog -> dailyPresenter.destroy());

        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    @Override
    public void openBrowseActivity(@NonNull ArrayList<Gift> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY);
        intent.putExtra(GalleryActivity.EXTRA_LIST, list);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void hideProgress() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showContent() {
        multipleStatusView.showContent();
    }

    @Override
    public void showEmpty() {

    }

    private void showLoading() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
    }

    //    @Override
    public void onClick(int position, Object object) {
//        DailyGirl dailyGirl = (DailyGirl) object;
//        String url = dailyGirl.getUrl();
//        if (!TextUtils.isEmpty(url)) {
//            showLoadingDialog();
//            dailyPresenter.girlsImages(url);
//        }
    }

    @Override
    public void refillData(List<Girl> list) {
        dailyGirlAdapter.refillItem(list);
    }

    @Override
    public void appendItem(List<Girl> list) {
        dailyGirlAdapter.appendItem(list);
    }

    @Override
    public void setMaxProgress(int value) {
        if (mDialog != null) {
            mDialog.setMax(value);
        }
    }

    @Override
    public void disProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dailyGirlAdapter != null) {
            dailyGirlAdapter.destroy();
        }
    }

    @Override
    public void onLazyActivityCreate() {
        dailyPresenter = new DailyGirlPresenter(getContext(), this);
        multipleStatusView.showLoading();
        dailyPresenter.fetchNew();
    }

    @Override
    public void shortToast(String string) {

    }
}
