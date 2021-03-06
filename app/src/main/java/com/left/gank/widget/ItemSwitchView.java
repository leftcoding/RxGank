package com.left.gank.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.left.gank.R;
import com.socks.library.KLog;

import java.util.IllegalFormatException;


/**
 * Create by LingYan on 2016-06-06
 */
public class ItemSwitchView extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView txtTitle;
    private TextView txtSummary;
    private LSwitch lSwitch;
    private View itemView;

    private OnSwitch mOnSwitch;

    private boolean isSummaryVisibility;
    private boolean isCheck;
    private String mTitle;
    private String mSummary;
    private int mTitleSize;
    private int mSummarySize;
    private int mTitleColor;
    private int mSummaryColor;

    public interface OnSwitch {
        void onSwitch(boolean isCheck);
    }

    public ItemSwitchView(Context context) {
        this(context, null);
    }

    public ItemSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final View root = LayoutInflater.from(context).inflate(R.layout.layout_item_text_view_switch, this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ItemSwitchView);
        try {
            mTitle = array.getString(R.styleable.ItemSwitchView_textTitle);
            mSummary = array.getString(R.styleable.ItemSwitchView_textSummary);
            mTitleSize = array.getInt(R.styleable.ItemSwitchView_textTitleSize, 14);
            mSummarySize = array.getInt(R.styleable.ItemSwitchView_textSummarySize, 12);
            mTitleColor = array.getInt(R.styleable.ItemSwitchView_textTitleColor, getResources().getColor(R.color.text_333333));
            mSummaryColor = array.getInt(R.styleable.ItemSwitchView_textSummaryColor, getResources().getColor(R.color.text_999999));
            isSummaryVisibility = array.getBoolean(R.styleable.ItemSwitchView_summaryVisible, false);
        } catch (IllegalFormatException e) {
            KLog.e(e);
        }
        txtTitle = root.findViewById(R.id.item_switch_txt_title);
        txtSummary = root.findViewById(R.id.item_switch_txt_summary);
        lSwitch = root.findViewById(R.id.item_switch_auto_check);
        itemView = root.findViewById(R.id.setting_rl_auto_check);
        array.recycle();
    }

    public void setSwitchChecked(boolean isCheck) {
        this.isCheck = isCheck;
        lSwitch.setChecked(isCheck);
    }

    public void setSwitchListener(OnSwitch onSwitch) {
        this.mOnSwitch = onSwitch;
    }

    public void onViewClick(boolean isCheck) {
        if (mOnSwitch != null) {
            mOnSwitch.onSwitch(isCheck);
        }
    }

    public LSwitch getSwitch() {
        return lSwitch;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_rl_auto_check:
                isCheck = !isCheck;
                lSwitch.setChecked(isCheck);
                onViewClick(isCheck);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        itemView.setOnClickListener(this);
        lSwitch.setOnCheckedChangeListener(this);
        txtTitle.setText(mTitle);
        txtSummary.setText(mSummary);
        txtTitle.setTextColor(mTitleColor);
        txtSummary.setTextColor(mSummaryColor);
        txtTitle.setTextSize(mTitleSize);
        txtSummary.setTextSize(mSummarySize);

        if (!isSummaryVisibility && TextUtils.isEmpty(mSummary)) {
            txtSummary.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.isCheck = isChecked;
        lSwitch.setChecked(isChecked);
        onViewClick(isChecked);
    }
}
