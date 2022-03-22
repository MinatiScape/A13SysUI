package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/* loaded from: classes.dex */
public class WidgetView extends FrameLayout {
    public LinearLayout mContentView;
    public final int mDefaultBackgroundColor;
    public TextView mDescription;
    public ImageView mIcon;
    public boolean mLoading;
    public LinearLayout mLoadingView;
    public final Drawable mOvalBackgroundDrawable;
    public TextView mTipText;
    public TextView mTitle;

    public final void setLoading(boolean z) {
        if (z != this.mLoading) {
            this.mLoading = z;
            if (z) {
                this.mLoadingView.setVisibility(0);
                this.mContentView.setVisibility(8);
                return;
            }
            this.mLoadingView.setVisibility(8);
            this.mContentView.setVisibility(0);
        }
    }

    public final void update(Drawable drawable, int i, int i2, View.OnClickListener onClickListener) {
        int i3 = this.mDefaultBackgroundColor;
        this.mIcon.setImageDrawable(drawable);
        this.mOvalBackgroundDrawable.setColorFilter(new PorterDuffColorFilter(i3, PorterDuff.Mode.SRC_ATOP));
        this.mIcon.setBackground(this.mOvalBackgroundDrawable);
        this.mTitle.setText(i);
        this.mDescription.setText(i2);
        setOnClickListener(onClickListener);
    }

    public WidgetView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOvalBackgroundDrawable = context.getDrawable(2131231714);
        this.mDefaultBackgroundColor = context.getColor(2131099866);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mIcon = (ImageView) findViewById(2131428102);
        this.mTipText = (TextView) findViewById(2131429056);
        this.mTitle = (TextView) findViewById(2131429057);
        this.mDescription = (TextView) findViewById(2131427815);
        this.mContentView = (LinearLayout) findViewById(2131427745);
        this.mLoadingView = (LinearLayout) findViewById(2131428270);
        this.mLoading = false;
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        float f;
        super.setEnabled(z);
        if (isEnabled()) {
            f = 1.0f;
        } else {
            f = 0.5f;
        }
        setAlpha(f);
    }
}
