package android.left.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by LingYan on 2017-11-27
 */

public abstract class BasicHolder<II extends ViewItem> extends RecyclerView.ViewHolder {
    public BasicHolder(View view) {
        super(view);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract void bindHolder(II item);

    protected View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false);
    }
}
