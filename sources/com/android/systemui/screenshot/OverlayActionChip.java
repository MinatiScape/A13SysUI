package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Objects;
/* loaded from: classes.dex */
public class OverlayActionChip extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public ImageView mIconView;
    public boolean mIsPending;
    public TextView mTextView;

    public OverlayActionChip(Context context) {
        this(context, null);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void setIcon(Icon icon, boolean z) {
        this.mIconView.setImageIcon(icon);
        if (!z) {
            this.mIconView.setImageTintList(null);
        }
    }

    public final void setIsPending(boolean z) {
        this.mIsPending = z;
        setPressed(z);
    }

    @Override // android.view.View
    public final void setPressed(boolean z) {
        boolean z2;
        if (this.mIsPending || z) {
            z2 = true;
        } else {
            z2 = false;
        }
        super.setPressed(z2);
    }

    public final void setText(CharSequence charSequence) {
        boolean z;
        this.mTextView.setText(charSequence);
        if (charSequence.length() > 0) {
            z = true;
        } else {
            z = false;
        }
        updatePadding(z);
    }

    public final void updatePadding(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mTextView.getLayoutParams();
        if (z) {
            int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131166732);
            int dimensionPixelSize2 = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131166734);
            layoutParams.setMarginStart(dimensionPixelSize);
            layoutParams.setMarginEnd(dimensionPixelSize2);
            layoutParams2.setMarginEnd(dimensionPixelSize);
        } else {
            int dimensionPixelSize3 = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131166728);
            layoutParams.setMarginStart(dimensionPixelSize3);
            layoutParams.setMarginEnd(dimensionPixelSize3);
        }
        this.mIconView.setLayoutParams(layoutParams);
        this.mTextView.setLayoutParams(layoutParams2);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        boolean z;
        ImageView imageView = (ImageView) findViewById(2131428548);
        Objects.requireNonNull(imageView);
        this.mIconView = imageView;
        TextView textView = (TextView) findViewById(2131428549);
        Objects.requireNonNull(textView);
        this.mTextView = textView;
        if (textView.getText().length() > 0) {
            z = true;
        } else {
            z = false;
        }
        updatePadding(z);
    }

    public OverlayActionChip(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsPending = false;
    }
}
