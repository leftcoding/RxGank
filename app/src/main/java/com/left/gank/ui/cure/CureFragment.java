package com.left.gank.ui.cure;

import android.app.ProgressDialog;
import android.content.Intent;
import android.ly.business.domain.Gift;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.left.gank.R;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.gallery.GalleryActivity;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;

/**
 * 妹子 - 清纯
 * Create by LingYan on 2016-07-01
 */
public class CureFragment extends LazyFragment implements CureContract.View {
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    private CureAdapter cureAdapter;
    private CureContract.Presenter curePresenter;

    private ProgressDialog progressDialog;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cureAdapter = new CureAdapter();
        cureAdapter.setOnItemClickListener(cureCallback);
        swipeRefresh.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefresh.setAdapter(cureAdapter);

        swipeRefresh.setOnScrollListener(new LySwipeRefreshLayout.OnListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
    }

    private void loadData() {
        if (curePresenter != null) {
            curePresenter.loadData(page);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        curePresenter = new CurePresenter(getContext(), this);
    }

    private final CureAdapter.Callback cureCallback = new CureAdapter.Callback() {
        @Override
        public void onClick(final String url) {
            if (!TextUtils.isEmpty(url)) {
                if (curePresenter != null) {
                    curePresenter.loadImages(url);
                }
            }
        }
    };

    @Override
    public void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getContext().getString(R.string.loading_meizi_images));
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setOnCancelListener(dialog -> curePresenter.destroy());
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadImagesSuccess(List<Gift> list) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getContext(), GalleryActivity.class);
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_DAILY);
        intent.putExtra(GalleryActivity.EXTRA_LIST, (ArrayList) list);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    @Override
    public void loadImagesFailure(String msg) {
        shortToast(msg);
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefresh != null && !swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void showContent() {
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
        }
    }

    @Override
    public void loadDataSuccess(int page, int maxPage, List<Gift> list) {
        if (ListUtils.isNotEmpty(list)) {
            this.page = page + 1;
            if (cureAdapter != null) {
                if (page == 1) {
                    cureAdapter.refillItem(list);
                } else {
                    cureAdapter.appendItem(list);
                }
                cureAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadDataFailure(int page, String msg) {
        shortToast(msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cureAdapter != null) {
            cureAdapter.destroy();
        }
        if (curePresenter != null) {
            curePresenter.destroy();
        }
    }

    @Override
    public void onLazyActivityCreate() {
        loadData();
    }
}
