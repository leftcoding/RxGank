package com.left.gank.ui.pure;

import android.app.ProgressDialog;
import android.business.domain.Gift;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.left.gank.R;
import com.left.gank.ui.base.LazyFragment;
import com.left.gank.ui.gallery.GalleryActivity;
import com.left.gank.utils.ListUtils;
import com.left.gank.widget.LySwipeRefreshLayout;
import com.left.gank.widget.MultipleStatusView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * 妹子 - 清纯
 * Create by LingYan on 2016-05-17
 */
public class PureFragment extends LazyFragment implements PureContract.View {
    private static final int FIRST_PAGE = 1;

    @BindView(R.id.swipe_refresh)
    LySwipeRefreshLayout swipeRefresh;

    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private PureAdapter pureAdapter;
    private PureContract.Presenter purePresenter;
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private int curPage = FIRST_PAGE;

    @Override
    protected int fragmentLayoutId() {
        return R.layout.fragment_gift;
    }

    public static PureFragment getInstance() {
        return new PureFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pureAdapter = new PureAdapter();
        swipeRefresh.setAdapter(pureAdapter);
        swipeRefresh.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        swipeRefresh.setOnScrollListener(listener);
        pureAdapter.setOnItemClickListener(pureCallback);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        purePresenter = new PurePresenter(getContext(), this);
        loadData();
    }

    private final LySwipeRefreshLayout.OnListener listener = new LySwipeRefreshLayout.OnListener() {

        @Override
        public void onRefresh() {
            curPage = FIRST_PAGE;
            loadData();
        }

        @Override
        public void onLoadMore() {
            loadData();
        }
    };

    private final PureAdapter.Callback pureCallback = new PureAdapter.Callback() {
        @Override
        public void onItemClick(final Gift gift) {
            disposable = Observable.just(gift)
                    .throttleFirst(100, TimeUnit.MILLISECONDS)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(gift1 -> gift1.url == null ? "" : gift1.url)
                    .subscribe(url -> {
                        if (purePresenter != null) {
                            purePresenter.loadImages(url);
                        }
                    });
        }
    };

    private void loadData() {
        if (purePresenter != null) {
            purePresenter.loadData(curPage);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showProgress() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
        }
        if (multipleStatusView != null) {
            multipleStatusView.showContent();
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

    @Override
    public void onDestroyView() {
        if (pureAdapter != null) {
            pureAdapter.destroy();
        }
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroyView();
    }

    @Override
    public void onLazyActivityCreate() {

    }

    @Override
    public void loadDataSuccess(int page, List<Gift> list) {
        if (ListUtils.isNotEmpty(list)) {
            if (pureAdapter != null) {
                curPage = page + 1;
                if (page == FIRST_PAGE) {
                    pureAdapter.refillItems(list);
                } else {
                    pureAdapter.appendItems(list);
                }
                pureAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadDataFailure(int page, String msg) {
        shortToast(msg);
    }

    @Override
    public void showLoadingDialog() {
        hideLoadingDialog();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getContext().getString(R.string.loading_meizi_images));
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnCancelListener(dialog -> purePresenter.destroy());

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
        bundle.putString(GalleryActivity.EXTRA_MODEL, GalleryActivity.EXTRA_GIFT);
        intent.putExtra(GalleryActivity.EXTRA_LIST, (ArrayList) list);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void loadImagesFailure(String msg) {
        shortToast(msg);
    }
}
