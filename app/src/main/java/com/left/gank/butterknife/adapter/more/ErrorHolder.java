package com.left.gank.butterknife.adapter.more;

import android.view.View;
import android.view.ViewGroup;

import com.left.gank.R;
import com.left.gank.butterknife.adapter.BindHolder;
import com.left.gank.butterknife.adapter.FootAdapter;

public class ErrorHolder extends BindHolder<ErrorItem> {
    private final FootAdapter.ErrorListener errorListener;

    public ErrorHolder(ViewGroup parent, FootAdapter.ErrorListener errorListener) {
        super(parent, R.layout.adapter_more_error);
        this.errorListener = errorListener;
    }

    @Override
    public void bindHolder(ErrorItem item) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorListener != null) {
                    errorListener.onClickError();
                }
            }
        });
    }
}
