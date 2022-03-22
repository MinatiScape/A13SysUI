package com.google.android.systemui.lowlightclock;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$anim;
import com.android.systemui.classifier.HistoryTracker$$ExternalSyntheticLambda2;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda5;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.google.common.base.Preconditions;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class LowLightClockControllerImpl implements LowLightClockController {
    public final boolean mIsLowLightClockEnabled;
    public boolean mIsShowing;
    public final LayoutInflater mLayoutInflater;
    public final int mMaxBurnInOffset;
    public ViewGroup mParent;
    public View mView;
    public final WeakHashMap mViewsToHide = new WeakHashMap();

    @Override // com.android.systemui.lowlightclock.LowLightClockController
    public final void attachLowLightClockView(NotificationShadeWindowView notificationShadeWindowView) {
        boolean z;
        if (this.mParent == null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkState(z, "Attempting to attach low-light clock when it is already attached.");
        Preconditions.checkState(this.mIsLowLightClockEnabled, "Attempting to attach low-light clock when it is not enabled.");
        this.mParent = notificationShadeWindowView;
        View inflate = this.mLayoutInflater.inflate(2131624239, (ViewGroup) null);
        this.mView = inflate;
        inflate.setVisibility(4);
        View findViewById = this.mParent.findViewById(2131428519);
        Objects.requireNonNull(findViewById, "NotificationPanel must not be null");
        this.mViewsToHide.put(findViewById, Float.valueOf(1.0f));
        ViewGroup viewGroup = this.mParent;
        viewGroup.addView(this.mView, viewGroup.indexOfChild(findViewById) + 1);
        View findViewById2 = this.mParent.findViewById(2131428181);
        if (findViewById2 != null) {
            this.mViewsToHide.put(findViewById2, Float.valueOf(1.0f));
        }
    }

    @Override // com.android.systemui.lowlightclock.LowLightClockController
    public final void dozeTimeTick() {
        View view = this.mView;
        if (view != null) {
            view.setTranslationX(R$anim.getBurnInOffset(this.mMaxBurnInOffset * 2, true) - this.mMaxBurnInOffset);
            this.mView.setTranslationY(R$anim.getBurnInOffset(this.mMaxBurnInOffset * 2, false) - this.mMaxBurnInOffset);
        }
    }

    @Override // com.android.systemui.lowlightclock.LowLightClockController
    public final boolean showLowLightClock(boolean z) {
        Object obj;
        View view = this.mView;
        int i = 0;
        if (view == null) {
            return false;
        }
        boolean z2 = this.mIsShowing;
        if (z2 == z) {
            return z2;
        }
        this.mIsShowing = z;
        if (!z) {
            i = 4;
        }
        view.setVisibility(i);
        Set entrySet = this.mViewsToHide.entrySet();
        if (this.mIsShowing) {
            obj = new HistoryTracker$$ExternalSyntheticLambda2(this, 1);
        } else {
            obj = new OverviewProxyService$$ExternalSyntheticLambda5(this, 3);
        }
        entrySet.forEach(obj);
        return this.mIsShowing;
    }

    public LowLightClockControllerImpl(Resources resources, LayoutInflater layoutInflater) {
        this.mLayoutInflater = layoutInflater;
        this.mIsLowLightClockEnabled = resources.getBoolean(2131034171);
        this.mMaxBurnInOffset = resources.getDimensionPixelSize(2131165601);
    }

    @VisibleForTesting
    public View getClockView() {
        return this.mView;
    }

    @Override // com.android.systemui.lowlightclock.LowLightClockController
    public final boolean isLowLightClockEnabled() {
        return this.mIsLowLightClockEnabled;
    }
}
