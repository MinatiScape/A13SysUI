package com.android.systemui.qs.carrier;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSCarrier extends LinearLayout {
    public TextView mCarrierText;
    public boolean mIsSingleCarrier;
    public CellSignalState mLastSignalState;
    public View mMobileGroup;
    public ImageView mMobileRoaming;
    public ImageView mMobileSignal;
    public boolean mProviderModelInitialized = false;
    public View mSpacer;

    public QSCarrier(Context context) {
        super(context);
    }

    public final boolean updateState(CellSignalState cellSignalState, boolean z) {
        boolean z2;
        int i;
        int i2;
        boolean z3 = false;
        if (Objects.equals(cellSignalState, this.mLastSignalState) && z == this.mIsSingleCarrier) {
            return false;
        }
        this.mLastSignalState = cellSignalState;
        this.mIsSingleCarrier = z;
        if (!cellSignalState.visible || z) {
            z2 = false;
        } else {
            z2 = true;
        }
        View view = this.mMobileGroup;
        int i3 = 8;
        if (z2) {
            i = 0;
        } else {
            i = 8;
        }
        view.setVisibility(i);
        View view2 = this.mSpacer;
        if (z) {
            i2 = 0;
        } else {
            i2 = 8;
        }
        view2.setVisibility(i2);
        if (z2) {
            ImageView imageView = this.mMobileRoaming;
            if (cellSignalState.roaming) {
                i3 = 0;
            }
            imageView.setVisibility(i3);
            ColorStateList colorAttr = Utils.getColorAttr(((LinearLayout) this).mContext, 16842806);
            this.mMobileRoaming.setImageTintList(colorAttr);
            this.mMobileSignal.setImageTintList(colorAttr);
            if (cellSignalState.providerModelBehavior) {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(((LinearLayout) this).mContext.getDrawable(2131232227));
                }
                this.mMobileSignal.setImageDrawable(((LinearLayout) this).mContext.getDrawable(cellSignalState.mobileSignalIconId));
                this.mMobileSignal.setContentDescription(cellSignalState.contentDescription);
            } else {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(new SignalDrawable(((LinearLayout) this).mContext));
                }
                this.mMobileSignal.setImageLevel(cellSignalState.mobileSignalIconId);
                StringBuilder sb = new StringBuilder();
                String str = cellSignalState.contentDescription;
                if (str != null) {
                    sb.append(str);
                    sb.append(", ");
                }
                if (cellSignalState.roaming) {
                    sb.append(((LinearLayout) this).mContext.getString(2131952236));
                    sb.append(", ");
                }
                String str2 = cellSignalState.typeContentDescription;
                if (TextUtils.equals(str2, ((LinearLayout) this).mContext.getString(2131952235)) || TextUtils.equals(str2, ((LinearLayout) this).mContext.getString(2131952099)) || TextUtils.equals(str2, ((LinearLayout) this).mContext.getString(2131952876))) {
                    z3 = true;
                }
                if (z3) {
                    sb.append(cellSignalState.typeContentDescription);
                }
                this.mMobileSignal.setContentDescription(sb);
            }
        }
        return true;
    }

    public QSCarrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mMobileGroup = findViewById(2131428374);
        this.mMobileRoaming = (ImageView) findViewById(2131428379);
        this.mMobileSignal = (ImageView) findViewById(2131428382);
        this.mCarrierText = (TextView) findViewById(2131428645);
        this.mSpacer = findViewById(2131428893);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public View getRSSIView() {
        return this.mMobileGroup;
    }
}
