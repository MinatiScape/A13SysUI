package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.preference.R$id;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public class PhoneStatusBarView extends FrameLayout {
    public DarkIconDispatcher.DarkReceiver mBattery;
    public DarkIconDispatcher.DarkReceiver mClock;
    public View mCutoutSpace;
    public DisplayCutout mDisplayCutout;
    public TouchEventHandler mTouchEventHandler;
    public int mRotationOrientation = -1;
    public int mCutoutSideNudge = 0;
    public final StatusBarContentInsetsProvider mContentInsetsProvider = (StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class);

    /* loaded from: classes.dex */
    public interface TouchEventHandler {
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mClock);
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mClock);
        this.mDisplayCutout = null;
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        NotificationPanelViewController.AnonymousClass18 r0 = (NotificationPanelViewController.AnonymousClass18) this.mTouchEventHandler;
        Objects.requireNonNull(r0);
        NotificationPanelViewController.this.mStatusBar.onTouchEvent(motionEvent);
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        TouchEventHandler touchEventHandler = this.mTouchEventHandler;
        if (touchEventHandler == null) {
            Log.w("PhoneStatusBarView", String.format("onTouch: No touch handler provided; eating gesture at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
            return true;
        }
        NotificationPanelViewController.AnonymousClass18 r5 = (NotificationPanelViewController.AnonymousClass18) touchEventHandler;
        Objects.requireNonNull(r5);
        NotificationPanelViewController.this.mStatusBar.onTouchEvent(motionEvent);
        if (!NotificationPanelViewController.this.mCommandQueue.panelsEnabled()) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            int i = PanelViewController.$r8$clinit;
            Log.v("PanelView", String.format("onTouchForwardedFromStatusBar: panel disabled, ignoring touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
            return false;
        } else if (motionEvent.getAction() != 0 || NotificationPanelViewController.this.mView.isEnabled()) {
            return NotificationPanelViewController.this.mView.dispatchTouchEvent(motionEvent);
        } else {
            int i2 = PanelViewController.$r8$clinit;
            Log.v("PanelView", String.format("onTouchForwardedFromStatusBar: panel view disabled, eating touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
            return true;
        }
    }

    public final boolean updateOrientationAndCutout() {
        boolean z;
        int exactRotation = R$id.getExactRotation(((FrameLayout) this).mContext);
        if (exactRotation != this.mRotationOrientation) {
            this.mRotationOrientation = exactRotation;
            z = true;
        } else {
            z = false;
        }
        if (Objects.equals(getRootWindowInsets().getDisplayCutout(), this.mDisplayCutout)) {
            return z;
        }
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        return true;
    }

    public final void updateStatusBarHeight() {
        int i;
        DisplayCutout displayCutout = this.mDisplayCutout;
        if (displayCutout == null) {
            i = 0;
        } else {
            i = displayCutout.getWaterfallInsets().top;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = SystemBarUtils.getStatusBarHeight(((FrameLayout) this).mContext) - i;
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131167072);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(2131167071);
        findViewById(2131428925).setPaddingRelative(dimensionPixelSize2, dimensionPixelSize, getResources().getDimensionPixelSize(2131167070), 0);
        findViewById(2131428514).setPaddingRelative(0, dimensionPixelSize2, 0, 0);
        setLayoutParams(layoutParams);
    }

    public PhoneStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
            requestLayout();
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mBattery = (DarkIconDispatcher.DarkReceiver) findViewById(2131427571);
        this.mClock = (DarkIconDispatcher.DarkReceiver) findViewById(2131427717);
        this.mCutoutSpace = findViewById(2131427796);
        updateResources();
    }

    public final boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    public final void updateLayoutForCutout() {
        updateStatusBarHeight();
        if (this.mCutoutSpace != null) {
            boolean currentRotationHasCornerCutout = this.mContentInsetsProvider.currentRotationHasCornerCutout();
            DisplayCutout displayCutout = this.mDisplayCutout;
            if (displayCutout == null || displayCutout.isEmpty() || currentRotationHasCornerCutout) {
                this.mCutoutSpace.setVisibility(8);
            } else {
                this.mCutoutSpace.setVisibility(0);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
                Rect boundingRectTop = this.mDisplayCutout.getBoundingRectTop();
                int i = boundingRectTop.left;
                int i2 = this.mCutoutSideNudge;
                boundingRectTop.left = i + i2;
                boundingRectTop.right -= i2;
                layoutParams.width = boundingRectTop.width();
                layoutParams.height = boundingRectTop.height();
            }
        }
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), getPaddingTop(), ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), getPaddingBottom());
    }

    public final void updateResources() {
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(2131165669);
        updateStatusBarHeight();
    }
}
