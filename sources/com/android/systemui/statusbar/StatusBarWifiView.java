package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class StatusBarWifiView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    public View mAirplaneSpacer;
    public StatusBarIconView mDotView;
    public ImageView mIn;
    public View mInoutContainer;
    public ImageView mOut;
    public View mSignalSpacer;
    public String mSlot;
    public StatusBarSignalPolicy.WifiIconState mState;
    public int mVisibleState = -1;
    public LinearLayout mWifiGroup;
    public ImageView mWifiIcon;

    public StatusBarWifiView(Context context) {
        super(context);
    }

    public final void applyWifiState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z = true;
        int i11 = 8;
        if (wifiIconState == null) {
            if (getVisibility() == 8) {
                z = false;
            }
            setVisibility(8);
            this.mState = null;
        } else {
            StatusBarSignalPolicy.WifiIconState wifiIconState2 = this.mState;
            if (wifiIconState2 == null) {
                StatusBarSignalPolicy.WifiIconState copy = wifiIconState.copy();
                this.mState = copy;
                setContentDescription(copy.contentDescription);
                int i12 = this.mState.resId;
                if (i12 >= 0) {
                    this.mWifiIcon.setImageDrawable(((FrameLayout) this).mContext.getDrawable(i12));
                }
                ImageView imageView = this.mIn;
                if (this.mState.activityIn) {
                    i6 = 0;
                } else {
                    i6 = 8;
                }
                imageView.setVisibility(i6);
                ImageView imageView2 = this.mOut;
                if (this.mState.activityOut) {
                    i7 = 0;
                } else {
                    i7 = 8;
                }
                imageView2.setVisibility(i7);
                View view = this.mInoutContainer;
                StatusBarSignalPolicy.WifiIconState wifiIconState3 = this.mState;
                if (wifiIconState3.activityIn || wifiIconState3.activityOut) {
                    i8 = 0;
                } else {
                    i8 = 8;
                }
                view.setVisibility(i8);
                View view2 = this.mAirplaneSpacer;
                if (this.mState.airplaneSpacerVisible) {
                    i9 = 0;
                } else {
                    i9 = 8;
                }
                view2.setVisibility(i9);
                View view3 = this.mSignalSpacer;
                if (this.mState.signalSpacerVisible) {
                    i10 = 0;
                } else {
                    i10 = 8;
                }
                view3.setVisibility(i10);
                if (this.mState.visible) {
                    i11 = 0;
                }
                setVisibility(i11);
            } else if (!wifiIconState2.equals(wifiIconState)) {
                StatusBarSignalPolicy.WifiIconState copy2 = wifiIconState.copy();
                setContentDescription(copy2.contentDescription);
                int i13 = this.mState.resId;
                int i14 = copy2.resId;
                if (i13 != i14 && i14 >= 0) {
                    this.mWifiIcon.setImageDrawable(((FrameLayout) this).mContext.getDrawable(i14));
                }
                ImageView imageView3 = this.mIn;
                if (copy2.activityIn) {
                    i = 0;
                } else {
                    i = 8;
                }
                imageView3.setVisibility(i);
                ImageView imageView4 = this.mOut;
                if (copy2.activityOut) {
                    i2 = 0;
                } else {
                    i2 = 8;
                }
                imageView4.setVisibility(i2);
                View view4 = this.mInoutContainer;
                if (copy2.activityIn || copy2.activityOut) {
                    i3 = 0;
                } else {
                    i3 = 8;
                }
                view4.setVisibility(i3);
                View view5 = this.mAirplaneSpacer;
                if (copy2.airplaneSpacerVisible) {
                    i4 = 0;
                } else {
                    i4 = 8;
                }
                view5.setVisibility(i4);
                View view6 = this.mSignalSpacer;
                if (copy2.signalSpacerVisible) {
                    i5 = 0;
                } else {
                    i5 = 8;
                }
                view6.setVisibility(i5);
                boolean z2 = copy2.activityIn;
                StatusBarSignalPolicy.WifiIconState wifiIconState4 = this.mState;
                if (z2 == wifiIconState4.activityIn && copy2.activityOut == wifiIconState4.activityOut) {
                    z = false;
                }
                boolean z3 = wifiIconState4.visible;
                boolean z4 = copy2.visible;
                if (z3 != z4) {
                    z |= true;
                    if (z4) {
                        i11 = 0;
                    }
                    setVisibility(i11);
                }
                this.mState = copy2;
            } else {
                z = false;
            }
        }
        if (z) {
            requestLayout();
        }
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final boolean isIconVisible() {
        StatusBarSignalPolicy.WifiIconState wifiIconState = this.mState;
        if (wifiIconState == null || !wifiIconState.visible) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setVisibleState(int i, boolean z) {
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            if (i == 0) {
                this.mWifiGroup.setVisibility(0);
                this.mDotView.setVisibility(8);
            } else if (i != 1) {
                this.mWifiGroup.setVisibility(8);
                this.mDotView.setVisibility(8);
            } else {
                this.mWifiGroup.setVisibility(8);
                this.mDotView.setVisibility(0);
            }
        }
    }

    @Override // android.view.View
    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StatusBarWifiView(slot=");
        m.append(this.mSlot);
        m.append(" state=");
        m.append(this.mState);
        m.append(")");
        return m.toString();
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static StatusBarWifiView fromContext(Context context, String str) {
        StatusBarWifiView statusBarWifiView = (StatusBarWifiView) LayoutInflater.from(context).inflate(2131624528, (ViewGroup) null);
        Objects.requireNonNull(statusBarWifiView);
        statusBarWifiView.mSlot = str;
        statusBarWifiView.mWifiGroup = (LinearLayout) statusBarWifiView.findViewById(2131429256);
        statusBarWifiView.mWifiIcon = (ImageView) statusBarWifiView.findViewById(2131429267);
        statusBarWifiView.mIn = (ImageView) statusBarWifiView.findViewById(2131429258);
        statusBarWifiView.mOut = (ImageView) statusBarWifiView.findViewById(2131429262);
        statusBarWifiView.mSignalSpacer = statusBarWifiView.findViewById(2131429268);
        statusBarWifiView.mAirplaneSpacer = statusBarWifiView.findViewById(2131429249);
        statusBarWifiView.mInoutContainer = statusBarWifiView.findViewById(2131428128);
        StatusBarIconView statusBarIconView = new StatusBarIconView(((FrameLayout) statusBarWifiView).mContext, statusBarWifiView.mSlot, null);
        statusBarWifiView.mDotView = statusBarIconView;
        statusBarIconView.setVisibleState(1);
        int dimensionPixelSize = ((FrameLayout) statusBarWifiView).mContext.getResources().getDimensionPixelSize(2131167067);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 8388627;
        statusBarWifiView.addView(statusBarWifiView.mDotView, layoutParams);
        statusBarWifiView.setVisibleState(0, false);
        return statusBarWifiView;
    }

    @Override // android.view.View
    public final void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (rect.left + translationX);
        rect.right = (int) (rect.right + translationX);
        rect.top = (int) (rect.top + translationY);
        rect.bottom = (int) (rect.bottom + translationY);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public final void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        ColorStateList valueOf = ColorStateList.valueOf(tint);
        this.mWifiIcon.setImageTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mDotView.setDecorColor(tint);
        this.mDotView.setIconColor(tint, false);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mWifiIcon.setImageTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public StatusBarWifiView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final String getSlot() {
        return this.mSlot;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final int getVisibleState() {
        return this.mVisibleState;
    }
}
